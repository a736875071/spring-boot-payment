package com.suncd.epm.cm.domain.ali.pay;

import lombok.Data;

import java.util.List;

/**
 * @author YangQ
 * @date 2020/5/26 15:56
 */
@Data
public class EcOrderPayQrCode {
    private Long orderId;
    private Long operatorId;
    private String money;
}
