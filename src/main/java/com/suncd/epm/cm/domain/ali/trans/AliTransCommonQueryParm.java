package com.suncd.epm.cm.domain.ali.trans;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author YangQ
 * @date 2020-07-15 11:38
 */
@Data
public class AliTransCommonQueryParm {
    @SerializedName("out_biz_no")
    private String outBizNo;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("product_code")
    private String productCode;
    @SerializedName("biz_scene")
    private String bizScene;
    @SerializedName("pay_fund_order_id")
    private String payFundOrderId;

}
