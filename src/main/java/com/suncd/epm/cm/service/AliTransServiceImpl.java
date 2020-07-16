package com.suncd.epm.cm.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayFundAccountQueryRequest;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.google.gson.Gson;
import com.suncd.epm.cm.domain.ali.trans.AliAccountQueryParm;
import com.suncd.epm.cm.domain.ali.trans.AliTransCommonQueryParm;
import com.suncd.epm.cm.domain.ali.trans.AliTransferParm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 这是一个资金类的服务必须使用证书,由于证书生效后不能再选择公钥验证,所以这没有进行更换
 * 证书和秘钥主要区别在于 AlipayClient的注入和 alipayClient.execute改为alipayClient.certificateExecute
 * 1.秘钥
 * AlipayClient alipayClient=new DefaultAlipayClient
 * (serverUrl, appId, privateKey, format,
 * charset, aliPayPublicKey, signType);
 * 2.证书
 * CertAlipayRequest   certAlipayRequest   =   new   CertAlipayRequest ();
 * certAlipayRequest . setServerUrl ( serverUrl );
 * certAlipayRequest . setAppId ( appId );
 * certAlipayRequest . setPrivateKey ( privateKey );
 * certAlipayRequest . setFormat ( format );
 * certAlipayRequest . setCharset ( charset );
 * certAlipayRequest . setSignType ( signType );
 * #开发者应用公钥证书路径
 * certAlipayRequest . setCertPath ( app_cert_path );
 * #支付宝公钥证书文件路径
 * certAlipayRequest . setAlipayPublicCertPath ( alipay_cert_path );
 * #支付宝CA根证书文件路径
 * certAlipayRequest . setRootCertPath ( alipay_root_cert_path );
 * AlipayClient alipayClient=new DefaultAlipayClient(certAlipayRequest);
 * <p>
 * # 构建request内容
 * XXXRequest request= new  XXXRequest ();
 * //
 * xxxResponse response=  alipayClient.certificateExecute ( request );
 * } catch  ( Exception   e ){
 * //
 * }
 *
 * @author YangQ
 * @date 2020-07-15 14:00
 */
@Service
@Slf4j
public class AliTransServiceImpl implements AliTransService {
    @Autowired
    private AlipayClient alipayClient;
    private Gson gson = new Gson();

    @Override
    public AlipayFundTransUniTransferResponse transfer(AliTransferParm transferParm) {
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        String bizContent = gson.toJson(transferParm);
        request.setBizContent(bizContent);
        log.debug("单笔转账请求参数:{}", bizContent);
        try {
            AlipayFundTransUniTransferResponse response = alipayClient.execute(request);
            log.debug("单笔转账请求结果:{}", gson.toJson(response.getBody()));
            if (response.isSuccess()) {
                return response;
            } else {
                log.error("单笔转账请求参数:{},结果出现异常{}", bizContent, response.getSubMsg());
                throw new RuntimeException("单笔转账失败,原因:" + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("单笔转账请求参数:{},结果出现异常{}", bizContent, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AlipayFundTransCommonQueryResponse transferCommonQuery(AliTransCommonQueryParm queryParm) {
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        String bizContent = gson.toJson(queryParm);
        log.debug("转账业务单据查询参数:{}", bizContent);
        request.setBizContent(bizContent);
        try {
            AlipayFundTransCommonQueryResponse response = alipayClient.execute(request);
            log.debug("转账业务单据查询结果:{}", gson.toJson(response.getBody()));
            if (response.isSuccess()) {
                return response;
            } else {
                log.error("转账业务单据查询请求参数:{},结果出现异常{}", bizContent, response.getSubMsg());
                throw new RuntimeException("转账业务单据查询失败,原因:" + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("转账业务单据查询参数:{},结果出现异常{}", bizContent, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AlipayFundAccountQueryResponse transferAccountQuery(AliAccountQueryParm queryParm) {
        AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
        String bizContent = gson.toJson(queryParm);
        log.debug("支付宝资金账户资产查询参数:{}", bizContent);
        request.setBizContent(bizContent);
        try {
            AlipayFundAccountQueryResponse response = alipayClient.execute(request);
            log.debug("支付宝资金账户资产查询结果:{}", gson.toJson(response.getBody()));
            if (response.isSuccess()) {
                return response;
            } else {
                log.error("支付宝资金账户资产查询请求参数:{},结果出现异常{}", bizContent, response.getSubMsg());
                throw new RuntimeException("支付宝资金账户资产查询失败,原因:" + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("支付宝资金账户资产查询参数:{},结果出现异常{}", bizContent, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
