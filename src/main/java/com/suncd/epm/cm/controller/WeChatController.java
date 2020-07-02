package com.suncd.epm.cm.controller;

import com.github.wxpay.sdk.WXPay;
import com.suncd.epm.cm.config.WxMyConfig;
import com.suncd.epm.cm.utils.wx.SignatureUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YangQ
 * @date 2020-07-01 17:43
 */
@RestController
@Slf4j
public class WeChatController {
    @Autowired
    private WxMyConfig wxMyConfig;

    /**
     * 微信js-api统一下单
     *
     * @param openId openId
     * @return
     */
    @RequestMapping(value = "/wx/js-api/place")
    public Map<String, String> weChatPlace(String openId) {
        return preCreatePayQrCode(String.valueOf(System.currentTimeMillis()), openId);

    }


    public Map<String, String> preCreatePayQrCode(String outTradeNo, String openId) {
        WXPay wxpay = new WXPay(wxMyConfig);

        Map<String, String> data = new HashMap<>();
        data.put("body", "腾讯充值中心-QQ会员充值");
        data.put("out_trade_no", outTradeNo);
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", "11");
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        data.put("trade_type", "JSAPI");
        data.put("product_id", "12");
        data.put("openid", openId);
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            if ("SUCCESS".equals(resp.get("return_code"))) {
                Map<String, String> datas = new HashMap<>();
                String prepayId = resp.get("prepay_id");
                datas.put("appId", wxMyConfig.getAppID());
                datas.put("package", "prepay_id=" + prepayId);
                datas.put("signType", "MD5");
                datas.put("timeStamp", Long.toString(System.currentTimeMillis()).substring(0, 10));
                datas.put("nonceStr", String.valueOf(System.currentTimeMillis()));
                String sign = SignatureUtils.signature(datas, wxMyConfig.getKey());
                datas.put("paySign", sign);
                System.out.println(resp);
                return datas;
                // 前台接收到返回值过后，调用JSAPI支付接口(onBridgeReady)，弹出模态框输入密码
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
