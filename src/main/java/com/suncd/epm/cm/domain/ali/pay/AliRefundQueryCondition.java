package com.suncd.epm.cm.domain.ali.pay;

import lombok.Data;

/**
 * @author YangQ
 * @date 2020-07-09 15:29
 */
@Data
public class AliRefundQueryCondition extends AliBaseCondition {
    private String outRequestNo;

    public AliRefundQueryCondition(String outTradeNo, String outRequestNo) {
        super(outTradeNo);
        this.outRequestNo = outRequestNo;
    }
}
