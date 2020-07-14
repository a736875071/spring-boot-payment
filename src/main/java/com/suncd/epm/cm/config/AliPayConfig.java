package com.suncd.epm.cm.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author smy
 */
@Configuration
@Data
public class AliPayConfig {
    @Value("${ali.open_api_domain}")
    private String serverUrl;
    @Value("${ali.appid}")
    private String appId;
    @Value("${ali.private_key}")
    private String privateKey;
    @Value("${ali.alipay_public_key}")
    private String aliPayPublicKey;
    @Value("${ali.sign_type}")
    private String signType;
    @Value("${ali.notify_url}")
    private String notifyUrl;
    @Value("${ali.return_url}")
    private String returnUrl;
    @Value("${ali.format}")
    private String format;
    @Value("${ali.charset}")
    private String charset;
    @Value("${ali.timeout_express}")
    private String timeoutExpress;

    @Bean
    public AlipayClient getAliPayClient() {
        return new DefaultAlipayClient
            (serverUrl, appId, privateKey, format,
                charset, aliPayPublicKey, signType);
    }
}
