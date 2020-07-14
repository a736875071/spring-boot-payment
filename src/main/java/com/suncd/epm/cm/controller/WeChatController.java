package com.suncd.epm.cm.controller;

import com.suncd.epm.cm.domain.WxChatBaseCondition;
import com.suncd.epm.cm.domain.WxChatRefundCondition;
import com.suncd.epm.cm.service.WxPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author YangQ
 * @date 2020-07-01 17:43
 */
@RestController
@Slf4j
public class WeChatController {
    /**
     * 返回成功xml
     */
    public static final String RES_SUCCESS_XML = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    /**
     * 返回失败xml
     */
    public static final String RES_FAIL_XML = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";

    @Autowired
    private WxPayOrderService wxPayOrderService;

    @RequestMapping(value = "trade/wap/open-id", method = RequestMethod.GET)
    public String getOpenId(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return wxPayOrderService.handleWeChatBrow(httpServletRequest, httpServletResponse);
    }

    /**
     * 微信js-api统一下单
     *
     * @param openId openId
     * @return
     */
    @RequestMapping(value = "/wx/js-api/place", method = RequestMethod.GET)
    public Map<String, String> weChatJsApiPlace(String openId) {
        return wxPayOrderService.jsApiCreateOrderPayQrCode(String.valueOf(System.currentTimeMillis()), "1", openId);
    }

    /**
     * 微信native统一下单
     *
     * @return
     */
    @RequestMapping(value = "/wx/native/place", method = RequestMethod.GET)
    public Map<String, String> weChatNativePlace() {
        return wxPayOrderService.nativeCreatePayQrCode(String.valueOf(System.currentTimeMillis()), "1");
    }

    /**
     * 微信h5统一下单
     *
     * @return
     */
    @RequestMapping(value = "/wx/h5/place", method = RequestMethod.GET)
    public Map<String, String> weChatH5Place() {
        return wxPayOrderService.nativeCreatePayQrCode(String.valueOf(System.currentTimeMillis()), "1");
    }

    /**
     * 微信对账文件下载
     *
     * @return
     */
    @RequestMapping(value = "/wx/down-load-bill", method = RequestMethod.GET)
    public Map<String, String> downLoadBill(String billDate) {
        return wxPayOrderService.downLoadBill(billDate);
    }

    /**
     * 通过微信订单号查询订单信息
     * (交易成功订单:4200000583202007099025731587)
     * (交易成功订单:4200000593202007140505791362)
     *
     * @param condition 条件
     * @return
     */
    @RequestMapping(value = "/wx/order-query", method = RequestMethod.GET)
    public Map<String, String> orderQuery(WxChatBaseCondition condition) {
        return wxPayOrderService.orderQuery(condition);
    }

    /**
     * 关闭订单
     *
     * @param outTradeNo 条件
     * @return
     */
    @RequestMapping(value = "/wx/close-order", method = RequestMethod.GET)
    public Map<String, String> closeOrder(String outTradeNo) {
        return wxPayOrderService.closeOrder(outTradeNo);
    }

    /**
     * 申请退款
     * 这个需要证书wx.certPath=apiclient_cert.p12
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
     *
     * @param condition 条件
     * @return
     */
    @RequestMapping(value = "/wx/refund", method = RequestMethod.GET)
    public Map<String, String> refund(WxChatRefundCondition condition) {
        return wxPayOrderService.refund(condition);
    }

    /**
     * 微信支付回调
     *
     * @return
     */
    @RequestMapping(value = "/wx/notifyUrl", method = RequestMethod.POST)
    public String weChatNotifyUrl(@RequestBody String notify) {
        log.debug("微信支付成功回调结果:" + notify);
        return RES_SUCCESS_XML;
    }


}
