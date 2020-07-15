# spring-boot-payment

1.需要开通应用
2.准备沙箱环境
https://openhome.alipay.com/platform/appDaily.htm?tab=info

来源如图: ![image](https://github.com/a736875071/spring-boot-payment/blob/master/src/main/resources/img/%E5%9F%BA%E7%A1%80%E7%8E%AF%E5%A2%83%E9%85%8D%E7%BD%AE%E6%9D%A5%E6%BA%90.png)

#支付宝网关
open_api_domain=https://openapi.alipaydev.com/gateway.do

#商户UID
pid=

#APPID
appid=

# 应用私钥(这个需要下载生成密钥工具地址:https://opendocs.alipay.com/open/291/105971#LDsXr)
private_key=

# 应用公钥
public_key=

#支付宝公钥
alipay_public_key=

# 签名类型: RSA->SHA1withRsa,RSA2->SHA256withRsa
sign_type=RSA2

#回调地址(需要映射到公网才能回调成功)

notify_url=http://xxxx/trades/payment/ali-call-back

format=json

charset=utf-8

#最后付款时间,创建后开始倒计时

timeout_express=5m

3.下载沙箱环境的支付宝app
![image](https://github.com/a736875071/spring-boot-payment/blob/master/src/main/resources/img/%E6%B2%99%E6%BC%8F%E7%8E%AF%E5%A2%83app.jpg)
4.使用沙箱账号登录测试
![image](https://github.com/a736875071/spring-boot-payment/blob/master/src/main/resources/img/%E6%B2%99%E6%BC%8F%E7%8E%AF%E5%A2%83%E8%B4%A6%E5%8F%B7.jpg)

#注意:

本demo很多post,put,patch参数都没有加@RequestBody注解,是因为方便,实际项目请根据自身情况修改

1.生成的付款二维码,如果没有扫描,通过
https://opendocs.alipay.com/apis/api_1/alipay.trade.query
是查询不到订单的

2.创建支付宝预支付订单是传入的out_trade_no商户订单号最好不要用自己系统的订单id或唯一主键,因为可能出现以下问题

 1):第一次生成的二维码因为二维码超时或预支付订单取消后,无论在使用这个id生成多少个二维码,扫描结果都是失败
 
 2):无论通过这个id生成多个订单信息变更的预支付订单二维码,最终都是根据第一个扫描二维码的信息生成支付宝订单,后续扫描其他二维码结果都是失败     
 
 3):本demo没有做定时轮询任务,查询订单完成状态,如果没有完成且符合轮询条件(时间,次数,订单状态等),继续轮询,反之则需要调用取消交易api,关闭订单
    这个根据自身业务自行添加
    
3.支付宝回调后,需要系统返回一个字符串:failure/success

    支付宝回调分为同步和异步回调,同步回调是get请求returnUrl,异步回调是post请求notifyUrl,之前一直同步回调没反应就是这个问题

    success:表示回调成功了,支付宝侧不会触发服务器异步通知页面特性 
    
    failure:表示失败,支付宝侧会触发服务器异步通知页面特性 
    
    我们主要关注 服务器异步通知页面特性的是 异步消息通知频率 支付宝服务器会不断重发通知，直到超过24小时22分钟。
    一般情况下，25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h）
    
     注:因为我是在沙箱环境测试的,我没有返回消息好像也不会触发支付宝异步通知,返回failure也不一定能收到或根据频率收到消息
                  
4.手机支付,通过生成的二维码唤醒支付宝支付(主要目的是想做支付宝和微信二码合一,由于微信个人不能申请微信认证,所以微信...)

    实现方式:
    
    a.将服务器的api映射到外网,调用api,生成一个包含api信息的二维码,提供给用户扫描二维码识别二维码信息中的api连接
    
    b.服务器根据请求头中的User-Agent来识别是支付宝(包含:AlipayClient)还是微信(包含:MicroMessenger)app来的请求,在进行
    下步操作
    
    c.支付宝主要是通过调用api创建订单,然后通过HttpServletResponse返回页面来达到目的
    
    d.微信是使用JsApi来实现唤醒微信app

 1):应用需要添加手机网站支付能力(https://b.alipay.com/signing/productDetailV2.htm?productId=I1011000290000001001)
 
 2)要测试需要先生成二维码(调用ZxingUtils.main()),或者通过 hhtp://localhost:8082/trade/wap/index  进入测试页面
 
 5.电脑网站支付(https://opendocs.alipay.com/open/270/106291)
    
    通过 hhtp://localhost:8082/trade/page/index  进入测试页面
 
 6.新增转账到支付宝api(目前由于文档不清,且沙箱环境有问题,已反馈处理中...后续更新)
 
 
