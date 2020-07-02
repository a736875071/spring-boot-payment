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
    @Value("${open_api_domain}")
    private String serverUrl;
    @Value("${appid}")
    private String appId;
    @Value("${private_key}")
    private String privateKey;
    @Value("${alipay_public_key}")
    private String aliPayPublicKey;
    @Value("${sign_type}")
    private String signType;
    @Value("${notify_url}")
    private String notifyUrl;
    @Value("${format}")
    private String format;
    @Value("${charset}")
    private String charset;
    @Value("${timeout_express}")
    private String timeoutExpress;

    @Bean
    public AlipayClient getAliPayClient() {
        return new DefaultAlipayClient
            (serverUrl, appId, privateKey, format,
                charset, aliPayPublicKey, signType);
    }
}
