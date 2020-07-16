package com.suncd.epm.cm.service;

import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.suncd.epm.cm.domain.ali.trans.AliAccountQueryParm;
import com.suncd.epm.cm.domain.ali.trans.AliTransCommonQueryParm;
import com.suncd.epm.cm.domain.ali.trans.AliTransferParm;

/**
 * @author YangQ
 * @date 2020/5/27 13:50
 */
public interface AliTransService {
    /**
     * 单笔转账接口
     *
     * @param transferParm 参数
     * @return 结果
     */
    AlipayFundTransUniTransferResponse transfer(AliTransferParm transferParm);

    /**
     * 转账业务单据查询接口
     *
     * @param queryParm 参数
     * @return 结果
     */
    AlipayFundTransCommonQueryResponse transferCommonQuery(AliTransCommonQueryParm queryParm);

    /**
     * 支付宝资金账户资产查询接口
     *
     * @param queryParm 参数
     * @return 结果
     */
    AlipayFundAccountQueryResponse transferAccountQuery(AliAccountQueryParm queryParm);
}
