package com.suncd.epm.cm.service;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import com.suncd.epm.cm.config.WxMyConfig;
import com.suncd.epm.cm.domain.wx.WxChatBaseCondition;
import com.suncd.epm.cm.domain.wx.WxChatRefundCondition;
import com.suncd.epm.cm.enums.WxResultCode;
import com.suncd.epm.cm.enums.WxReturnCode;
import com.suncd.epm.cm.enums.WxTradeStatus;
import com.suncd.epm.cm.enums.WxTradeType;
import com.suncd.epm.cm.utils.ZxingUtils;
import com.suncd.epm.cm.utils.wx.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YangQ
 * @date 2020-07-03 09:59
 */
@Slf4j
@Service
public class WxPayOrderServiceImpl implements WxPayOrderService {
    @Autowired
    private WxMyConfig wxMyConfig;
    private Gson gson = new Gson();


    @Override
    public Map<String, String> jsApiCreateOrderPayQrCode(String outTradeNo, String money, String openId) {
        WXPay wxpay = new WXPay(wxMyConfig, WXPayConstants.SignType.MD5);
        Map<String, String> data = new HashMap<>(8);
        data.put("body", "我只是测试哈哈哈");
        data.put("out_trade_no", outTradeNo);
        data.put("fee_type", "CNY");
        //单位是分,不是圆
        data.put("total_fee", money);
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", wxMyConfig.getNotifyUrl());
        data.put("trade_type", WxTradeType.JSAPI.getValue());
        data.put("openid", openId);
        log.debug("微信JsApi下单参数:{}", gson.toJson(data));
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            log.debug("微信JsApi下单结果:{}", gson.toJson(resp));
            if (WxReturnCode.SUCCESS.getValue().equals(resp.get("return_code"))) {
                Map<String, String> datas = new HashMap<>(8);
                String prepayId = resp.get("prepay_id");
                datas.put("appId", wxMyConfig.getAppID());
                datas.put("package", "prepay_id=" + prepayId);
                datas.put("signType", WXPayConstants.SignType.MD5.toString());
                datas.put("timeStamp", Long.toString(System.currentTimeMillis()).substring(0, 10));
                datas.put("nonceStr", String.valueOf(System.currentTimeMillis()));
                String sign = WXPayUtil.generateSignature(datas, wxMyConfig.getKey());
                datas.put("paySign", sign);
                log.debug("微信JsApi返回前端信息:{}", gson.toJson(datas));
                return datas;
                // 前台接收到返回值过后，调用JSAPI支付接口(onBridgeReady)，弹出模态框输入密码
            } else {
                log.error("JsApi下单请求参数:{},出现异常:{}", gson.toJson(data), resp.get("return_msg"));
                throw new RuntimeException(resp.get("return_msg"));
            }
        } catch (Exception e) {
            log.error("JsApi下单请求参数:{},出现异常:{}", gson.toJson(data), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> nativeCreatePayQrCode(String outTradeNo, String money) {
        WXPay wxpay = new WXPay(wxMyConfig, WXPayConstants.SignType.MD5);
        Map<String, String> data = new HashMap<>(8);
        data.put("body", "我只是测试哈哈哈");
        data.put("out_trade_no", outTradeNo);
        data.put("fee_type", "CNY");
        //单位是分,不是圆
        data.put("total_fee", money);
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", wxMyConfig.getNotifyUrl());
        data.put("trade_type", WxTradeType.NATIVE.getValue());
        log.debug("微信Native下单参数:{}", gson.toJson(data));
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            log.debug("微信Native下单结果:{}", gson.toJson(resp));
            if (resp.get("return_code").equals(WxReturnCode.SUCCESS.getValue())) {
                String filePath = String.format("C:/Users/change/Desktop/qr-%s.png", outTradeNo);
                log.info("filePath:" + filePath);
                ZxingUtils.getQrCodeImage(resp.get("code_url"), 256, filePath);
            }
            return resp;
        } catch (Exception e) {
            log.error("Native下单请求参数:{},出现异常:{}", gson.toJson(data), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> h5CreatePayQrCode(String outTradeNo, String money) {
        WXPay wxpay = new WXPay(wxMyConfig, WXPayConstants.SignType.MD5);
        Map<String, String> data = new HashMap<>(8);
        data.put("body", "我只是测试哈哈哈");
        data.put("out_trade_no", outTradeNo);
        data.put("fee_type", "CNY");
        //单位是分,不是圆
        data.put("total_fee", money);
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", wxMyConfig.getNotifyUrl());
        data.put("trade_type", WxTradeType.MWEB.getValue());
        log.debug("微信H5下单参数:{}", gson.toJson(data));
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            log.debug("微信H5下单结果:{}", gson.toJson(resp));
//            if (resp.get("return_code").equals(WxTradeStatus.SUCCESS.getValue())) {
//                String filePath = String.format("C:/Users/change/Desktop/qr-%s.png", outTradeNo);
//                log.info("filePath:" + filePath);
//                ZxingUtils.getQrCodeImage(resp.get("code_url"), 256, filePath);
//            }
            return resp;
        } catch (Exception e) {
            log.error("H5下单请求参数:{},出现异常:{}", gson.toJson(data), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> orderQuery(WxChatBaseCondition condition) {
        WXPay wxpay = new WXPay(wxMyConfig, WXPayConstants.SignType.MD5);
        Map<String, String> data = new HashMap<>(8);
        if (condition.getTransactionId() != null) {
            data.put("transaction_id", condition.getTransactionId());
        } else if (condition.getOutTradeNo() != null) {
            data.put("out_trade_no", condition.getOutTradeNo());
        } else {
            throw new RuntimeException("微信订单号与商户订单号不能同时为空");
        }
        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            log.debug("微信订单查询结果:{}", gson.toJson(resp));
            return resp;
        } catch (Exception e) {
            log.error("查询订单:{},出现异常:{}", gson.toJson(condition), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> downLoadBill(String billDate) {
        WXPay wxpay = new WXPay(wxMyConfig, WXPayConstants.SignType.MD5);
        Map<String, String> data = new HashMap<>(8);
        data.put("bill_date", billDate);
        data.put("bill_type", "ALL");
        try {
            return wxpay.downloadBill(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> closeOrder(String outTradeNo) {
        WXPay wxpay = new WXPay(wxMyConfig, WXPayConstants.SignType.MD5);
        Map<String, String> data = new HashMap<>(8);
        data.put("out_trade_no", outTradeNo);
        log.debug("关闭订单:{}", outTradeNo);
        try {
            Map<String, String> resp = wxpay.closeOrder(data);
            log.debug("关闭订单:{},结果:{}", outTradeNo, gson.toJson(resp));
            return resp;
        } catch (Exception e) {
            log.error("关闭订单:{},出现异常:{}", outTradeNo, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> refund(WxChatRefundCondition condition) {
        //查询订单是否存在
        Map<String, String> order = orderQuery(condition);
        log.debug("退款时查询订单:{},结果是:{}", gson.toJson(condition), gson.toJson(order));
        if (order.get("result_code").equals(WxResultCode.FAIL.getValue())) {
            throw new RuntimeException(order.get("err_code_des"));
        }
        if (!order.get("trade_state").equals(WxTradeStatus.SUCCESS.getValue())) {
            throw new RuntimeException("该笔订单不能退款");
        }
        WXPay wxpay = new WXPay(wxMyConfig, WXPayConstants.SignType.MD5);
        Map<String, String> data = new HashMap<>(8);
        data.put("out_refund_no", String.valueOf(System.currentTimeMillis()));
        data.put("refund_fee", order.get("total_fee"));
        data.put("total_fee", order.get("total_fee"));
        data.put("transaction_id", order.get("transaction_id"));
        log.debug("退款时请求参数:{}", gson.toJson(data));
        try {
            Map<String, String> resp = wxpay.refund(data);
            log.debug("退款时请求结果:{}", gson.toJson(resp));
            return resp;
        } catch (Exception e) {
            log.error("退款请求:{},发送异常:{}", gson.toJson(data), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String handleWeChatBrow(HttpServletRequest request, HttpServletResponse response) {
        try {
            String code = request.getParameter("code");
            if (StringUtils.isNotEmpty(code)) {
                return getWxOpenid(code);
            } else {
                String queryurl = request.getQueryString();
                String redirectUrl = wxMyConfig.getAuthorizeUrl();
                if (null != queryurl) {
                    redirectUrl += "?" + queryurl;
                }
                String redirectUri = URLEncoder.encode(redirectUrl, "UTF-8");
                String redirect = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + wxMyConfig.getAppID()
                    + "&redirect_uri=" + redirectUri
                    + "&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
                response.sendRedirect(redirect);
            }
        } catch (IOException e) {
            log.error("获取用户信息出现异常:{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微信内置浏览器 获取openid
     *
     * @param code
     * @return
     */
    private String getWxOpenid(String code) {
        try {
            String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                wxMyConfig.getAppID(), wxMyConfig.getAppSecret(), code);
            String wxUser = HttpClientUtil.getInstance().get(url);
            log.debug("获取用户信息结果:{}", wxUser);
            Map<String, String> o = gson.fromJson(wxUser, Map.class);
            return o.get("openid");
        } catch (Exception e) {
            log.error("授权异常", e);
        }
        return null;
    }
}
