package com.suncd.epm.cm.service;

import com.suncd.epm.cm.domain.wx.WxChatBaseCondition;
import com.suncd.epm.cm.domain.wx.WxChatRefundCondition;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * native 统一下单(必填参数)
     *
     * @param outTradeNo 外部订单号
     * @param money      下单金额
     * @return 结果
     */
    Map<String, String> h5CreatePayQrCode(String outTradeNo, String money);

    /**
     * 下载交易账单
     *
     * @param billDate 账单日
     * @return 结果
     */
    Map<String, String> downLoadBill(String billDate);

    /**
     * 订单详情查询
     *
     * @param condition 条件
     * @return 结果
     */
    Map<String, String> orderQuery(WxChatBaseCondition condition);

    /**
     * 关闭订单
     *
     * @param outTradeNo
     * @return
     */
    Map<String, String> closeOrder(String outTradeNo);

    /**
     * 退款
     *
     * @param condition
     * @return
     */
    Map<String, String> refund(WxChatRefundCondition condition);

    /**
     * 获取用户openId
     * @param request
     * @param response
     * @return
     */
    String handleWeChatBrow(HttpServletRequest request, HttpServletResponse response);
}
