package com.suncd.epm.cm.domain.wx;

import lombok.Data;

/**
 * @author YangQ
 * @date 2020-07-09 15:29
 */
@Data
public class WxChatRefundCondition extends WxChatBaseCondition {
    private String totalFee;
    private String refundFee;
    private String outRefundNo;
}
