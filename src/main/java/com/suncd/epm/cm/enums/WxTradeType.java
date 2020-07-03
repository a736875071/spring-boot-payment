package com.suncd.epm.cm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author
 * @date 2019/8/28 14:28
 */
@Getter
@AllArgsConstructor
public enum WxTradeType {
    /**
     * JSAPI支付（或小程序支付）
     */
    JSAPI("JSAPI"),
    /**
     * Native支付
     */
    NATIVE("NATIVE"),
    /**
     * app支付
     */
    APP("APP"),

    /**
     * H5支付
     */
    MWEB("MWEB"),
    /**
     * -付款码支付
     */
    MICROPAY("MICROPAY");
    private String value;


}
