package com.suncd.epm.cm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author
 * @date 2019/8/28 14:28
 */
@Getter
@AllArgsConstructor
public enum WxTradeStatus {
    /**
     * 支付成功
     */
    SUCCESS("SUCCESS"),

    /**
     * 转入退款
     */
    REFUND("REFUND"),
    /**
     * 未支付
     */
    NOTPAY("NOTPAY"),
    /**
     * 已关闭
     */
    CLOSED("CLOSED"),
    /**
     * 已撤销
     */
    REVOKED("REVOKED"),
    /**
     * 用户支付中
     */
    USERPAYING("USERPAYING"),
    /**
     * 支付失败
     */
    PAYERROR("PAYERROR"),
    ;
    private String value;


}
