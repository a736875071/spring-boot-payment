package com.suncd.epm.cm.controller;

import com.alipay.api.response.AlipayDataBillSellQueryResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.google.gson.Gson;
import com.suncd.epm.cm.domain.EcOrderPayQrCode;
import com.suncd.epm.cm.domain.TradeBillSellQuery;
import com.suncd.epm.cm.service.AliPayOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static java.lang.Math.random;

/**
 * @author YangQ
 * @date 2020/5/26 17:41
 */
@RestController
@Log4j2
public class AliPayOrderController {
    @Autowired
    private AliPayOrderService aliPayOrderService;


    /**
     * 交易预创建
     * 收银员通过收银台或商户后台调用支付宝接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付
     *
     * @param ecOrderPayQrCode
     * @return
     */
    @PostMapping(value = "/create-qr-code")
    public AlipayTradePrecreateResponse preCreateOrderPayQrCode(@Validated @RequestBody EcOrderPayQrCode ecOrderPayQrCode) {
        return aliPayOrderService.createOrderPayQrCode(ecOrderPayQrCode);
    }

    /**
     * 交易预创建,直接生成二维码,不做数据校验
     *
     * @param orderId
     * @return
     */
    @PostMapping(value = "/create-qr-code-test")
    public AlipayTradePrecreateResponse preCreatePayQrCode(Long orderId) {
        return aliPayOrderService.createPayQrCode(orderId);
    }

    /**
     * 取消二维码
     * 如果生成的二维码没有扫码,则删除本地支付单
     * 如果已经扫码,则取消支付侧订单
     *
     * @param outTradeNo
     * @return
     */
    @PostMapping(value = "/trades/cancel-qr-code")
    public String tradeCancelQrCodeByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo) {
        return aliPayOrderService.tradeCancelQrCodeByOutTradeNo(outTradeNo);
    }

    /**
     * 收单交易撤销
     * 支付交易返回失败或支付系统超时，调用该接口撤销交易。
     * 如果此订单用户支付失败，支付宝系统会将此订单关闭；
     * 如果用户支付成功，支付宝系统会将此订单资金退还给用户。
     * 注意：只有发生支付系统超时或者支付结果未知时可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API。
     * 提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
     *
     * @param outTradeNo
     * @return
     */
    @PutMapping("/trades/cancel")
    public String tradeCancelByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo) {
        return aliPayOrderService.tradeCancelByOutTradeNo(outTradeNo);
    }

    /**
     * 收单交易退款(一笔全退)
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 支付宝将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * 交易超过约定时间（签约时设置的可退款时间）的订单无法进行退款 支付宝退款支持单笔交易分多次退款，
     * 多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。
     * 一笔退款失败后重新提交，要采用原来的退款单号。总退款金额不能超过用户实际支付金额
     *
     * @param outTradeNo
     * @return
     */
    @PutMapping("/trades/refund")
    public String tradeRefundByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo) {
        return aliPayOrderService.tradeRefundByOutTradeNo(outTradeNo);
    }

    /**
     * 收单线下交易查询
     * 该接口提供所有支付宝支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
     * 需要调用查询接口的情况： 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
     * 调用支付接口后，返回系统错误或未知交易状态情况；
     * 调用alipay.trade.pay，返回INPROCESS的状态；
     * 调用alipay.trade.cancel之前，需确认支付状态；
     *
     * @param outTradeNo
     * @return
     */
    @GetMapping("/trades/trade-no")
    public AlipayTradeQueryResponse getTradesByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo) {
        return aliPayOrderService.getTradesByOutTradeNo(outTradeNo);
    }

    /**
     * 支付宝商家账户卖出交易查询(支付宝侧还未开放财务API)
     *
     * @param tradeBillSellQuery
     * @return
     */
    @GetMapping("/trades/bill-sell-query")
    public AlipayDataBillSellQueryResponse tradesBillSellQuery(TradeBillSellQuery tradeBillSellQuery) {
        return aliPayOrderService.tradesBillSellQuery(tradeBillSellQuery);
    }

    /**
     * 交易账单下载(有问题,每次下载下来的文件都是一样........)
     *
     * @param billDate
     * @return
     */
    @GetMapping("/trades/bill-download")
    public String getTradesBillDownload(@RequestParam("billDate") String billDate) {
        return aliPayOrderService.tradesBillDownload(billDate);
    }

    /**
     * 交易回调(现在只处理支付成功的)
     * <p>
     * 服务器异步通知页面特性
     * 必须保证服务器异步通知页面（notify_url）上无任何字符，如空格、HTML标签、开发系统自带抛出的异常提示信息等，并且异步通知地址必须为用户外网可访问，异步通知地址不能重定向；
     * <p>
     * 支付宝是用 POST 方式发送通知信息，因此该页面中获取参数的方式，如：request.Form(“out_trade_no”)、$_POST[‘out_trade_no’]；
     * <p>
     * 支付宝主动发起通知，该方式才会被启用；
     * <p>
     * 只有在支付宝的交易管理中存在该笔交易，且发生了交易状态的改变，支付宝才会通过该方式发起服务器通知（即时到账交易状态为“等待买家付款”的状态默认是不会发送通知的）；
     * <p>
     * 服务器间的交互，不像页面跳转同步通知可以在页面上显示出来，这种交互方式是不可见的；
     * <p>
     * 第一次交易状态改变（即时到账中此时交易状态是交易完成）时，不仅会返回同步处理结果，而且服务器异步通知页面也会收到支付宝发来的处理结果通知；
     * <p>
     * 程序执行完后必须打印输出“success”（不包含引号）。如果商户反馈给支付宝的字符不是success这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟。一般情况下，25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h）；
     * <p>
     * 程序执行完成后，该页面不能执行页面跳转。如果执行页面跳转，支付宝会收不到success字符，会被支付宝服务器判定为该页面程序运行出现异常，而重发处理结果通知；
     * <p>
     * cookies、session等在此页面会失效，即无法获取这些数据；
     * <p>
     * 该方式的调试与运行必须在服务器上，即互联网上能访问；
     * <p>
     * 该方式的作用主要防止订单丢失，即页面跳转同步通知没有处理订单更新，它则去处理；
     * <p>
     * 当商户收到服务器异步通知并打印出success时，服务器异步通知参数notify_id才会失效。也就是说在支付宝发送同一条异步通知时（包含商户并未成功打印出success导致支付宝重发数次通知），服务器异步通知参数notify_id是不变的。
     *
     * @param request
     */
    @PostMapping("/trades/payment/notify-url")
    public String paymentAliNotifyUrl(HttpServletRequest request) {
        log.debug("支付宝异步侧回调开始...");
        //模拟回调异常
        int a = (int) (random() * 10);
        if (a / 2 == 0) {
            aliPayOrderService.paymentAliCallBack(request);
            return "success";
        }
        log.debug("支付宝侧异步回调结束...");
        return "failure";
    }

    @PostMapping("/trades/payment/return-url")
    public String paymentAliReturnUrl(HttpServletRequest request) {
        log.debug("支付宝侧同步回调开始...");
        //模拟回调异常
        int a = (int) (random() * 10);
        if (a / 2 == 0) {
            Map<String, String[]> parmMap = request.getParameterMap();
            log.debug("接受到同步回调信息:{},开始处理相关业务", parmMap);
            return "success";
        }
        log.debug("支付宝侧同步回调结束...");
        return "failure";
    }

    /**
     * 手机支付api(通过生成的二维码唤醒支付宝支付)
     * 1.需要调用ZxingUtils main方法生成我二维码
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @RequestMapping("trade/wap/pay")
    public void wapPay(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String userAgent = httpServletRequest.getHeader("User-Agent");
        log.debug("User-Agent:{}", userAgent);
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        log.debug("请求参数是:{}", new Gson().toJson(parameterMap));
        String outTradeNo = parameterMap.get("outTradeNo")[0];
        String from = "null";
        if (userAgent.contains("MicroMessenger")) {
            log.debug("微信扫码支付");
            from = "<p style=\"text-align:center\">" +
                "我没有进行企业认证,目前没有对接微信..." +
                "</p>";
        } else if (userAgent.contains("Alipay")) {
            //用户使用支付宝访问页面
            log.debug("支付宝扫码支付");
            from = aliPayOrderService.aliWapPay(outTradeNo);
            //直接将完整的表单html输出到页面
            httpServletResponse.setContentType("text/html;charset=utf-8");
            httpServletResponse.getWriter().write(from);
            httpServletResponse.getWriter().flush();
        } else {
            from = "<p style=\"text-align:center\">" +
                "使用微信或支付宝支付" +
                "</p>";
            //直接将完整的表单html输出到页面
            httpServletResponse.setContentType("text/html;charset=utf-8");
            httpServletResponse.getWriter().write(from);
            httpServletResponse.getWriter().flush();
        }

    }

}
