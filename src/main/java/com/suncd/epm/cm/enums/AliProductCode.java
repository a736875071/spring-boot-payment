package com.suncd.epm.cm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务产品码
 *
 * @author
 * @date 2019/8/28 14:28
 */
@Getter
@AllArgsConstructor
public enum AliProductCode {
    /**
     * 单笔无密转账到支付宝账户固定为
     */
    TRANS_ACCOUNT_NO_PWD("TRANS_ACCOUNT_NO_PWD"),

    /**
     * 单笔无密转账到银行卡固定为:
     */
    TRANS_BANKCARD_NO_PWD("TRANS_BANKCARD_NO_PWD"),
    /**
     * 收发现金红包固定为:
     */
    STD_RED_PACKET("STD_RED_PACKET"),

    ;
    private String value;


}
