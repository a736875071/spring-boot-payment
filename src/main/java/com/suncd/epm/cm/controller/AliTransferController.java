package com.suncd.epm.cm.controller;

import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.suncd.epm.cm.domain.ali.trans.AliAccountQueryParm;
import com.suncd.epm.cm.domain.ali.trans.AliTransCommonQueryParm;
import com.suncd.epm.cm.domain.ali.trans.AliTransferParm;
import com.suncd.epm.cm.service.AliTransService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文档 https://opendocs.alipay.com/open/309/106237
 * https://opendocs.alipay.com/open/309/106236
 *
 * @author YangQ
 * @date 2020/5/26 17:41
 */
@RestController
@Log4j2
public class AliTransferController {
    @Autowired
    private AliTransService aliTransService;


    /**
     * 单笔转账接口
     *
     * @param transferParm 请求参数
     * @return 结果
     */
    @PostMapping(value = "/trans/transfer")
    public AlipayFundTransUniTransferResponse transTransfer(@RequestBody AliTransferParm transferParm) {
        return aliTransService.transfer(transferParm);
    }

    /**
     * 转账业务单据查询接口
     *
     * @param queryParm 请求参数
     * @return 结果
     */
    @GetMapping(value = "/trans/common/query")
    public AlipayFundTransCommonQueryResponse transferCommonQuery(AliTransCommonQueryParm queryParm) {
        return aliTransService.transferCommonQuery(queryParm);
    }

    /**
     * 支付宝资金账户资产查询接口
     *
     * @param queryParm 请求参数
     * @return 结果
     */
    @GetMapping(value = "/trans/account/query")
    public AlipayFundAccountQueryResponse transferAccountQuery(AliAccountQueryParm queryParm) {
        return aliTransService.transferAccountQuery(queryParm);
    }


}
