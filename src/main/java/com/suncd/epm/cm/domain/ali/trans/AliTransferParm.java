package com.suncd.epm.cm.domain.ali.trans;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author YangQ
 * @date 2020-07-15 11:38
 */
@Data
public class AliTransferParm {
    @SerializedName("out_biz_no")
    private String outBizNo;
    @SerializedName("trans_amount")
    private String transAmount;
    @SerializedName("product_code")
    private String productCode;
    @SerializedName("biz_scene")
    private String bizScene;
    @SerializedName("payee_info")
    private PayeeInfo payeeInfo;

    @Data
    public static class PayeeInfo {
        @SerializedName("identity")
        private String identity;
        @SerializedName("name")
        private String name;
        @SerializedName("identity_type")
        private String identityType;
    }
}
