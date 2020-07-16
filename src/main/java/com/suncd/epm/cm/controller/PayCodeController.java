package com.suncd.epm.cm.controller;

import com.google.gson.Gson;
import com.suncd.epm.cm.config.WxMyConfig;
import com.suncd.epm.cm.service.AliPayOrderService;
import com.suncd.epm.cm.service.WxPayOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 微信支付宝一码支付
 *
 * @author YangQ
 * @date 2020/5/26 17:41
 */
@Controller
@Log4j2
public class PayCodeController {
    @Autowired
    private AliPayOrderService aliPayOrderService;
    @Autowired
    private WxPayOrderService wxPayOrderService;
    @Autowired
    private WxMyConfig wxMyConfig;
    private Gson gson = new Gson();


    /**
     * 手机支付api(通过生成的二维码唤醒支付宝或微信app)
     * 1.需要调用ZxingUtils main方法生成我二维码
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @RequestMapping(value = "trade/wap/pay", method = RequestMethod.GET)
    public ModelAndView wapPay(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String userAgent = httpServletRequest.getHeader("User-Agent");
        log.debug("User-Agent:{}", userAgent);
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        log.debug("请求参数是:{}", new Gson().toJson(parameterMap));
        //这个是前端传值(测试改为系统时间)
//        String outTradeNo = parameterMap.get("outTradeNo")[0];
        String outTradeNo = String.valueOf(System.currentTimeMillis());
        String form = "null";
        ModelAndView mav = new ModelAndView();
        if (userAgent.contains("MicroMessenger")) {
            log.debug("微信扫码支付");
            //查询是否有openId(只是为了测试这么写的)
            String openId = wxMyConfig.getOpenId();
            if (openId == null) {
                //获取openId(回调地址需要与备案的域名一致)
                openId = wxPayOrderService.handleWeChatBrow(httpServletRequest, httpServletResponse);
            }
            //创建微信订单
            if (openId != null) {
                Map<String, String> wxMap = wxPayOrderService.jsApiCreateOrderPayQrCode(String.valueOf(System.currentTimeMillis()),
                    "1", openId);
                mav.addAllObjects(wxMap);
            }
            mav.setViewName("wap/pay/temp");
            log.debug("返回信息:" + mav);
        } else if (userAgent.contains("Alipay")) {
            //用户使用支付宝访问页面
            log.debug("支付宝扫码支付");
            form = aliPayOrderService.aliWapPay(outTradeNo);
            //两种返回唤醒支付宝app
            //第一种:返回form到页面temp.html页面,根据js  $("html").append(form);来唤醒app,相比第二种中间多跳转了一次temp.html
//            Map<String, String> aliMap = new HashMap<>();
//            aliMap.put("form", form);
//            mav.addAllObjects(aliMap);
            mav.setViewName("/wap/pay/temp");
            //第二种:直接将完整的表单html输出到页面
            httpServletResponse.setContentType("text/html;charset=utf-8");
            httpServletResponse.getWriter().write(form);
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();
        } else {
            form = "<p style=\"text-align:center\">" +
                "使用微信或支付宝支付扫码支付" +
                "</p>";
            //直接将完整的表单html输出到页面
            httpServletResponse.setContentType("text/html;charset=utf-8");
            httpServletResponse.getWriter().write(form);
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();
        }
        return mav;

    }

    /**
     * 作为跳转二维码的页面接口
     *
     * @return
     */
    @RequestMapping(value = "trade/wap/index", method = RequestMethod.GET)
    public String wapPay() {
        return "/wap/pay/index";
    }

    /**
     * 电脑网站支付页面入口
     *
     * @return
     */
    @RequestMapping(value = "trade/page/index", method = RequestMethod.GET)
    public String pagePay() {
        return "/page/pagePay";
    }


    /**
     * 支付宝支付成功后同步回调接口,此接口返回的同步页面,不是json,需要返回json调用
     * AliPayOrderController:get  /trades/payment/return-url
     *
     * @param request
     * @return
     */
    @GetMapping("/ali/trades/payment/return-url")
    public String aliReturnUrl(HttpServletRequest request) {
        log.debug("支付宝说同步回调不可靠...是否支付成功,需要根据返回的订单号查询订单状态确定");
        log.debug("支付宝侧同步回调开始...");
        Map<String, String[]> parmMap = request.getParameterMap();
        log.debug("同步回调参数是:{}", gson.toJson(parmMap));
        log.debug("支付宝侧同步回调结束...");
        return "/page/pagePay";
    }
}
