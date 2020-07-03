package com.suncd.epm.cm.service;

import java.util.Map;

/**
 * @author YangQ
 * @date 2020/5/27 13:50
 */
public interface WxPayOrderService {
    /**
     * js-api统一下单(必填参数)
     * api列表 : https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
     *
     * @param outTradeNo 外部订单号
     * @param money      下单金额
     * @param openId     用户标识(需要微信授权通过code 来换取 openId ,MessageActController实现)
     * @return 结果
     */
    Map<String, String> jsApiCreateOrderPayQrCode(String outTradeNo, String money, String openId);

    /**
     * native 统一下单(必填参数)
     *
     * @param outTradeNo 外部订单号
     * @param money      下单金额
     * @return 结果
     */
    Map<String, String> nativeCreatePayQrCode(String outTradeNo, String money);


}
