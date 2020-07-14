package com.suncd.epm.cm.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YangQ
 * @date 2020-07-09 15:29
 */
@Data
@NoArgsConstructor
public class AliBaseCondition {
    private String tradeNo;
    private String outTradeNo;

    public AliBaseCondition(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }


}
