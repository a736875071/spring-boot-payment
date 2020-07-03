package com.suncd.epm.cm.service;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.suncd.epm.cm.config.WxMyConfig;
import com.suncd.epm.cm.enums.WxTradeStatus;
import com.suncd.epm.cm.enums.WxTradeType;
import com.suncd.epm.cm.utils.ZxingUtils;
import com.suncd.epm.cm.utils.wx.SignatureUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            if (WxTradeStatus.SUCCESS.getValue().equals(resp.get("return_code"))) {
                Map<String, String> datas = new HashMap<>(8);
                String prepayId = resp.get("prepay_id");
                datas.put("appId", wxMyConfig.getAppID());
                datas.put("package", "prepay_id=" + prepayId);
                datas.put("signType", WXPayConstants.SignType.MD5.toString());
                datas.put("timeStamp", Long.toString(System.currentTimeMillis()).substring(0, 10));
                datas.put("nonceStr", String.valueOf(System.currentTimeMillis()));
                String sign = SignatureUtils.signature(datas, wxMyConfig.getKey());
                datas.put("paySign", sign);
                System.out.println(resp);
                return datas;
                // 前台接收到返回值过后，调用JSAPI支付接口(onBridgeReady)，弹出模态框输入密码
            } else {
                throw new RuntimeException(resp.get("return_msg"));
            }

        } catch (Exception e) {
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
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            if (resp.get("return_code").equals(WxTradeStatus.SUCCESS.getValue())) {
                String filePath = String.format("C:/Users/change/Desktop/qr-%s.png", outTradeNo);
                log.info("filePath:" + filePath);
                ZxingUtils.getQrCodeImage(resp.get("code_url"), 256, filePath);
            }
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
