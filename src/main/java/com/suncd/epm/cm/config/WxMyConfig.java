package com.suncd.epm.cm.config;

import com.alipay.api.internal.util.file.IOUtils;
import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;

/**
 * @author YangQ
 * @date 2020-06-29 14:13
 */
@Data
@Configuration
@Slf4j
public class WxMyConfig implements WXPayConfig {
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.mchId}")
    private String mchId;
    @Value("${wx.key}")
    private String key;
    @Value("${wx.appSecret}")
    private String appSecret;
    @Value("${wx.certPath}")
    private String certPath;
    @Value("${wx.authorizeUrl}")
    private String authorizeUrl;
    private static final String CLASS_PATH = "classpath:";
    private byte[] certData;

    @PostConstruct
    private void initCert() {
        try {
            InputStream certStream ;
            if (certPath.startsWith(CLASS_PATH)) {
                certStream = new ClassPathResource(certPath).getInputStream();
            } else {
                certStream = Files.newInputStream(ResourceUtils.getFile(certPath).toPath());
            }
            certData = IOUtils.toByteArray(certStream);
        } catch (IOException e) {
            log.error("初始化微信证数失败", e);
        }
    }

    @Override
    public String getAppID() {
        return appId;
    }

    @Override
    public String getMchID() {
        return mchId;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
