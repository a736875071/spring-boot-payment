package com.suncd.epm.cm.domain.ali.trans;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author YangQ
 * @date 2020-07-15 11:38
 */
@Data
public class AliAccountQueryParm {
    @SerializedName("alipay_user_id")
    private String alipayUserId;
    @SerializedName("account_type")
    private String accountType;
    @SerializedName("merchant_user_id")
    private String merchantUserId;
    @SerializedName("account_product_code")
    private String accountProductCode;
    @SerializedName("account_scene_code")
    private String accountSceneCode;
}
