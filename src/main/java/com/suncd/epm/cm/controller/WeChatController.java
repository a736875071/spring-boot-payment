package com.suncd.epm.cm.controller;

import com.suncd.epm.cm.service.WxPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author YangQ
 * @date 2020-07-01 17:43
 */
@RestController
@Slf4j
public class WeChatController {
    @Autowired
    private WxPayOrderService wxPayOrderService;

    /**
     * 微信js-api统一下单
     *
     * @param openId openId
     * @return
     */
    @RequestMapping(value = "/wx/js-api/place",method = RequestMethod.GET)
    public Map<String, String> weChatJsApiPlace(String openId) {
        return wxPayOrderService.jsApiCreateOrderPayQrCode(String.valueOf(System.currentTimeMillis()), "1", openId);
    }

    /**
     * 微信native统一下单
     *
     * @return
     */
    @RequestMapping(value = "/wx/native/place",method = RequestMethod.GET)
    public Map<String, String> weChatNativePlace() {
        return wxPayOrderService.nativeCreatePayQrCode(String.valueOf(System.currentTimeMillis()), "1");
    }
}
