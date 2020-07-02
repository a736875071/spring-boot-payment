package com.suncd.epm.cm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liurui
 * @date 2019/8/28 14:28
 */
@Getter
@AllArgsConstructor
public enum AliTradeStatus {
    /**
     * 交易创建，等待买家付款
     */
    WAIT_BUYER_PAY("WAIT_BUYER_PAY"),

    /**
     * 未付款交易超时关闭，或支付完成后全额退款
     */
    TRADE_CLOSED("TRADE_CLOSED"),

    /**
     * 交易支付成功
     */
    TRADE_SUCCESS("TRADE_SUCCESS"),

    /**
     * 交易结束，不可退款
     */
    TRADE_FINISHED("TRADE_FINISHED"),
    ;
    private String value;


}
