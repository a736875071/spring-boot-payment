package com.suncd.epm.cm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**参与方的标识类型
 * @author
 * @date 2019/8/28 14:28
 */
@Getter
@AllArgsConstructor
public enum AliIdentityType {
    /**
     * 支付宝的会员ID
     */
    ALIPAY_USER_ID("ALIPAY_USER_ID"),

    /**
     * 支付宝登录号，支持邮箱和手机号格式
     */
    ALIPAY_LOGON_ID("ALIPAY_LOGON_ID"),

    ;
    private String value;


}
