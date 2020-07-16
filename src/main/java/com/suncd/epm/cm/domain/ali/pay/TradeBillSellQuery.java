package com.suncd.epm.cm.domain.ali.pay;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author YangQ
 * @date 2020/5/28 14:14
 */
@Data
public class TradeBillSellQuery {
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("alipay_order_no")
    private String aliPayOrderNo;
    @SerializedName("merchant_order_no")
    private String merchantOrderNo;
    @SerializedName("store_no")
    private String storeNo;
    @SerializedName("page_no")
    private String pageNo;
    @SerializedName("page_size")
    private String pageSize;
}
