package com.suncd.epm.cm.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.google.gson.Gson;
import com.suncd.epm.cm.config.AliPayConfig;
import com.suncd.epm.cm.domain.EcOrderPayQrCode;
import com.suncd.epm.cm.domain.PayBizContent;
import com.suncd.epm.cm.domain.TradeBillSellQuery;
import com.suncd.epm.cm.enums.AliTradeStatus;
import com.suncd.epm.cm.utils.ZxingUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.random;

/**
 * @author YangQ
 * @date 2020/5/27 13:54
 */
@Service
@Log4j2
public class AliPayOrderServiceImpl implements AliPayOrderService {
    @Autowired
    private AliPayConfig aliPayConfig;
    @Autowired
    private AlipayClient alipayClient;
    private Gson gson = new Gson();


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AlipayTradePrecreateResponse createOrderPayQrCode(EcOrderPayQrCode ecOrderPayQrCode) {
        //生成唯一支付id
        String outTradeNo = String.valueOf(System.currentTimeMillis());
        //组装支付请求
        return createOrderPayQrCode(ecOrderPayQrCode.getMoney(), outTradeNo, ecOrderPayQrCode);
    }

    @Override
    public AlipayTradePrecreateResponse createPayQrCode(Long orderId) {
        int rand = (int) (random() * 10);
        int money = rand == 0 ? 1 : rand;
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        PayBizContent payBizContent = new PayBizContent();
        payBizContent.setOutTradeNo(String.valueOf(orderId));
        payBizContent.setStoreId("NJ_001");
        payBizContent.setSubject("订单金额" + money);
        payBizContent.setTimeoutExpress(aliPayConfig.getTimeoutExpress());
        payBizContent.setTotalAmount(String.valueOf(money));
        String orderIds = String.valueOf(orderId);
        payBizContent.setBody(orderIds);
        String toString = gson.toJson(payBizContent);
        //订单允许的最晚付款时间
        request.setBizContent(toString);
        System.out.println(request.getBizContent());
        System.out.println(request.getNotifyUrl());
        try {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            System.out.println(response.getCode());
            if (response.isSuccess()) {
                dumpResponse(response);
                // 需要修改为运行机器上的路径
                String filePath = String.format("C:/Users/change/Desktop/qr-%s.png",
                    System.currentTimeMillis());
                log.info("filePath:" + filePath);
                ZxingUtils.getQrCodeImage(response.getQrCode(), 256, filePath);
            } else {
                log.error("不支持的交易状态，交易返回异常!!!");
                throw new RuntimeException("不支持的交易状态，交易返回异常!!!");
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public AlipayTradePrecreateResponse createOrderPayQrCode(String money, String outTradeNo, EcOrderPayQrCode ecOrderPayQrCode) {
        //创建API对应的request类
        PayBizContent payBizContent = new PayBizContent();
        payBizContent.setOutTradeNo(outTradeNo);
        payBizContent.setStoreId("NJ_001");
        payBizContent.setSubject("订单金额" + money);
        payBizContent.setTimeoutExpress(aliPayConfig.getTimeoutExpress());
        payBizContent.setTotalAmount(money);
        String orderIds = String.valueOf(ecOrderPayQrCode.getOrderId());
        payBizContent.setBody(orderIds);
        payBizContent.setStoreId(outTradeNo);
        payBizContent.setOperatorId(String.valueOf(ecOrderPayQrCode.getOperatorId()));
        payBizContent.setTerminalId(outTradeNo);
        String toString = gson.toJson(payBizContent);
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        //订单允许的最晚付款时间
        request.setBizContent(toString);
        System.out.println(request.getBizContent());
        System.out.println(request.getNotifyUrl());
        try {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            System.out.println(response.getCode());
            if (response.isSuccess()) {
                dumpResponse(response);
                // 需要修改为运行机器上的路径
                String filePath = String.format("C:/Users/change/Desktop/qr-%s.png",
                    response.getOutTradeNo());
                log.info("filePath:" + filePath);
                ZxingUtils.getQrCodeImage(response.getQrCode(), 256, filePath);
            } else {
                log.error("不支持的交易状态，交易返回异常!!!");
                throw new RuntimeException("不支持的交易状态，交易返回异常!!!");
            }
            payBizContent.setTradeStatus("WAIT_BUYER_PAY");
            return response;
        } catch (AlipayApiException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                    response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String tradeCancelQrCodeByOutTradeNo(String outTradeNo) {
        //查询支付宝侧是否存在这笔订单
        AlipayTradeQueryResponse queryResponse = getTradesByOutTradeNo(outTradeNo);
        //取消支付宝侧订单
        if (queryResponse.isSuccess()) {
            tradeCancelByOutTradeNo(outTradeNo);
        }
        return "success";
    }

    @Override
    public String tradeCancelByOutTradeNo(String outTradeNo) {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizContent("{" +
            "    \"out_trade_no\":\"" + outTradeNo + "\" }");
        try {
            AlipayTradeCancelResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return Boolean.TRUE.toString();
            } else {
                log.error("不支持的交易状态，交易返回异常!!!");
                return Boolean.FALSE.toString();
            }
        } catch (AlipayApiException e) {
            log.error("不支持的交易状态，交易返回异常!!!,原因:{}", e.getMessage());
            return Boolean.FALSE.toString();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String tradeRefundByOutTradeNo(String outTradeNo) {
        //查询支付宝侧订单记录是否存在
        AlipayTradeQueryResponse queryResponse = getTradesByOutTradeNo(outTradeNo);
        //判断交易状态
        if (queryResponse == null) {
            return "没有此订单";
        }
        if (!queryResponse.isSuccess()) {
            return queryResponse.getSubMsg();
        }
        if (!queryResponse.getTradeStatus().equals(AliTradeStatus.TRADE_FINISHED.getValue())) {
            return "此订单不可退款";
        }
        //支付宝侧退款
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        PayBizContent returnPayBizContent = new PayBizContent();
        returnPayBizContent.setOutTradeNo(outTradeNo);
        returnPayBizContent.setRefundAmount(queryResponse.getTotalAmount());
        request.setBizContent(gson.toJson(returnPayBizContent));
        try {
            AlipayTradeRefundResponse refundResponse = alipayClient.execute(request);
            if (refundResponse.isSuccess()) {
                System.out.println(gson.toJson(refundResponse));
                return "退款成功";
            } else {
                log.error("不支持的交易状态，交易返回异常!!!");
                throw new RuntimeException(refundResponse.getSubMsg());
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AlipayDataBillSellQueryResponse tradesBillSellQuery(TradeBillSellQuery tradesBillSellQuery) {
        AlipayDataBillSellQueryRequest request = new AlipayDataBillSellQueryRequest();
        String bizContent = gson.toJson(tradesBillSellQuery);
        log.debug("参数:{}", bizContent);
        request.setBizContent(bizContent);
        System.out.println(gson.toJson(request));
        try {
            AlipayDataBillAccountlogQueryRequest request1 = new AlipayDataBillAccountlogQueryRequest();
            request.setBizContent(bizContent);
            AlipayDataBillAccountlogQueryResponse response1 = alipayClient.execute(request1);
            AlipayDataBillSellQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
                return response;
            } else {
                System.out.println("调用失败");
                return response;
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AlipayTradeQueryResponse getTradesByOutTradeNo(String outTradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
            "    \"out_trade_no\":\"" + outTradeNo + "\" }");
        try {
            System.out.println(request.getBizContent());
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return response;
            } else {
                log.error("不支持的交易状态，交易返回异常!!!");
                return response;
            }
        } catch (AlipayApiException e) {
            log.error("不支持的交易状态，交易返回异常!!!,原因:{}", e.getMessage());
            return null;
        }
    }

    @Override
    public String tradesBillDownload(String billDate) {
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        request.setBizContent("{" +
            "\"bill_type\":\"trade\"," +
            "\"bill_date\":\"" + billDate + "\"" +
            "  }");
        try {
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
                System.out.println(response.getBillDownloadUrl());
                return response.getBillDownloadUrl();
            } else {
                System.out.println("调用失败");
                return response.getSubMsg();
            }
        } catch (AlipayApiException e) {
            return e.getErrMsg();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paymentAliCallBack(HttpServletRequest request) {
        Map<String, String[]> parmMap = request.getParameterMap();
        Map<String, String> paMap = getParm(parmMap);
        boolean verify = verifyAliAsyncCallBackParams(paMap);
        if (!verify) {
            log.error("支付宝支付回调发现异常回调,请立即排查!!!");
            return;
        }
        String tradeStatus = paMap.get("trade_status");
        if (AliTradeStatus.WAIT_BUYER_PAY.getValue().equals(tradeStatus)) {
            log.debug("交易待支付业务");
        } else if (tradeStatus.equals(AliTradeStatus.TRADE_CLOSED.getValue())) {
            log.debug("交易关闭业务");
        } else if (tradeStatus.equals(AliTradeStatus.TRADE_SUCCESS.getValue())) {
            log.debug("交易成功业务");
            String outTradeNo = paMap.get("out_trade_no");
            log.debug("交易成功业务,订单号:{}", outTradeNo);
        } else if (tradeStatus.equals(AliTradeStatus.TRADE_FINISHED.getValue())) {
            log.debug("交易完成业务");
        } else {
            log.debug("不知名状态");
        }
    }

    private Boolean verifyAliAsyncCallBackParams(Map<String, String> map) {
        String sign = map.get("sign");
        map.remove("sign_type");
        String signContent = AlipaySignature.getSignCheckContentV2(map);
        try {
            return AlipaySignature.rsa256CheckContent(signContent, sign, aliPayConfig.getAliPayPublicKey(), "utf-8");
        } catch (AlipayApiException e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    private Map<String, String> getParm(Map<String, String[]> parmMap) {
        Map<String, String> map = new HashMap<>(24);
        parmMap.keySet().forEach(key -> {
            String[] value = parmMap.get(key);
            map.put(key, value[0]);
        });
        return map;
    }

    @Override
    public String aliWapPay(String outTradeNo) {
        AlipayTradeWapPayRequest aliPayRequest = new AlipayTradeWapPayRequest();
        //同步通知
        aliPayRequest.setReturnUrl(aliPayConfig.getNotifyUrl());
        // 异步通知
        aliPayRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());
        PayBizContent payBizContent = new PayBizContent();
        payBizContent.setOutTradeNo(outTradeNo);
        int rand = (int) (random() * 10);
        int money = rand == 0 ? 1 : rand;
        payBizContent.setTotalAmount(String.valueOf(money));
        payBizContent.setSubject("手机支付");
        aliPayRequest.setBizContent(gson.toJson(payBizContent));
        // 调用SDK生成表单
        String form = null;
        try {
            log.debug("手机支付请求参数:{}", gson.toJson(aliPayRequest));
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(aliPayRequest);
            log.debug("创建支付订单结果:{}", gson.toJson(response));
            form = response.getBody();
            log.debug("外部商户创建订单并支付:{}", form);
        } catch (AlipayApiException e) {
            log.error("发生了异常:{}", e.getMessage());
            e.printStackTrace();
        }
        return form;
    }
}
