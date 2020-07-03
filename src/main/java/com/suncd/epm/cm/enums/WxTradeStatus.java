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
     * 成功
     */
    SUCCESS("SUCCESS"),

    /**
     * 失败
     */
    FAIL("FAIL");
    private String value;


}
