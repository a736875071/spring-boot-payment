package com.suncd.epm.cm.service;

import com.alipay.api.response.*;
import com.suncd.epm.cm.domain.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author YangQ
 * @date 2020/5/27 13:50
 */
public interface AliPayOrderService {
    /**
     * 创建预支付单
     *
     * @param ecOrderPayQrCode 请求参数
     * @return 结果
     */
    AlipayTradePrecreateResponse createOrderPayQrCode(EcOrderPayQrCode ecOrderPayQrCode);

    /**
     * 创建预支付单
     *
     * @param orderId 请求参数
     * @return 结果
     */
    AlipayTradePrecreateResponse createPayQrCode(Long orderId);


    /**
     * 取消支付单
     *
     * @param outTradeNo
     * @return
     */
    String tradeCancelQrCodeByOutTradeNo(String outTradeNo);

    /**
     * 取消交易
     *
     * @param outTradeNo
     * @return
     */
    String tradeCancelByOutTradeNo(String outTradeNo);

    /**
     * 交易关闭
     * @param condition 条件
     * @return 结果
     */
    boolean tradeClose(AliBaseCondition condition);

    /**
     * 商户退款
     *
     * @param returnPayBizContent
     * @return
     */
    String tradeRefundByOutTradeNo(PayBizContent returnPayBizContent);

    /**
     * 交易退款查询
     *
     * @param condition 条件
     * @return 结果
     */
    AlipayTradeFastpayRefundQueryResponse tradeRefundQuery(AliRefundQueryCondition condition);


    /**
     * 通过交易订单号查询支付侧订单
     *
     * @param condition
     * @return
     */
    AlipayTradeQueryResponse getTradesQuery(AliBaseCondition condition);

    /**
     * 支付宝商家账户卖出交易查询
     *
     * @param tradesBillSellQuery
     * @return
     */
    AlipayDataBillSellQueryResponse tradesBillSellQuery(TradeBillSellQuery tradesBillSellQuery);

    /**
     * 交易回调
     *
     * @param request
     * @return
     */
    void paymentAliCallBack(HttpServletRequest request);

    /**
     * 下载对账单
     *
     * @param billDate
     * @return
     */
    String tradesBillDownload(String billDate);


    /**
     * 手机支付
     *
     * @param outTradeNo
     * @return
     */
    String aliWapPay(String outTradeNo);

    /**
     * 电脑网站支付
     *
     * @param payBizContent
     * @return
     */
    AlipayTradePagePayResponse aliPagePay(PayBizContent payBizContent);
}
