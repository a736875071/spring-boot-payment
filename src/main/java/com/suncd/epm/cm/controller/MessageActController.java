package com.suncd.epm.cm.controller;

import com.alibaba.fastjson.JSONException;
import com.google.gson.Gson;
import com.suncd.epm.cm.config.WxMyConfig;
import com.suncd.epm.cm.utils.wx.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 主要获取微信用户信息
 *
 * @author YangQ
 * @date 2020-06-30 14:39
 */
@Controller
public class MessageActController {

    @Autowired
    private WxMyConfig wxMyConfig;

    /**
     * 微信登录
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/weixinLogin", method = RequestMethod.GET)
    public void weixinLogin(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        //获取code
        if (StringUtils.isBlank(code)) {
            String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMyConfig.getAppID() +
                "&redirect_uri=" + URLEncoder.encode(wxMyConfig.getAuthorizeUrl()) +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE#wechat_redirect";
            System.out.println(redirectUrl);
            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        String[] s = getOpenId(code);
        String openId = s[0];
        String accessToken = s[1];
        //获取用户信息
        String[] userInfo = getUserInfo(openId, accessToken);
        System.out.println("手机登录openId=================================>" + openId);
        System.out.println(new Gson().toJson(userInfo));
        //根据需求返回
    }

    private String getOpenAuthResultRequestParam(String appId, String appSecret, String code) {
        return URLEncodedUtils.format(Arrays.asList(
            new BasicNameValuePair("appid", appId),
            new BasicNameValuePair("secret", appSecret),
            new BasicNameValuePair("code", code),
            new BasicNameValuePair("grant_type", "authorization_code")
        ), StandardCharsets.UTF_8);
    }

    /**
     * 获取用户授权 得到openId,accessToken
     *
     * @param code
     * @return
     */
    public String[] getOpenId(String code) {
        String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        String url = tokenUrl + "?" + getOpenAuthResultRequestParam(wxMyConfig.getAppID(), wxMyConfig.getAppSecret(), code);
        System.out.println(url);
        JSONObject json = null;
        try {
            json = new JSONObject(HttpClientUtil.getInstance().get(url));
            //------------在这里把使用过的code存入到缓存中--------
            System.out.println(json);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        String openId = "";
        String accessToken = "";
        String[] s = new String[2];
        if (json != null) {
            try {
                openId = json.getString("openid");
                System.out.println(openId);
                accessToken = json.getString("access_token");
                s[0] = openId;
                s[1] = accessToken;
            } catch (JSONException e) {
                String errcode = null;
                try {
                    errcode = json.getString("errcode");
                    System.out.println("errcode================>手机登录 获取用户授权失败" + errcode);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return s;
    }


    /**
     * 获取用户信息 拉取用户信息(需scope为 snsapi_userinfo)
     * 只有在用户将公众号绑定到微信开放平台帐号后，可以获取unionid
     *
     * @param
     * @param
     * @return
     */
    public String[] getUserInfo(String openid, String accessToken) {
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?lang=zh_CN";
        userInfoUrl = userInfoUrl + "&access_token=" + accessToken + "&openid=" + openid;
        JSONObject json = null;
        try {
            json = new JSONObject(HttpClientUtil.getInstance().get(userInfoUrl));
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        //用户昵称
        String nickname = "";
        //用户的性别
        String sex = "";
        //用户个人资料填写的省份
        String province = "";
        //普通用户个人资料填写的城市
        String city = "";
        //国家，如中国为CN
        String country = "";
        //用户头像，
        String headimgurl = "";
        //
        String unionid = "";
        String[] s = new String[6];
        if (json != null) {
            try {
                nickname = json.getString("nickname");
                sex = json.getString("sex");
                province = json.getString("province");
                city = json.getString("city");
                country = json.getString("country");
                headimgurl = json.getString("headimgurl");
                s[0] = nickname;
                s[1] = sex;
                s[2] = province;
                s[3] = city;
                s[4] = country;
                s[5] = headimgurl;
            } catch (JSONException e) {
                String errcode = null;
                try {
                    errcode = json.getString("errcode");
                    System.out.println("errcode================>获取用户信息失败" + errcode);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return s;
    }

}
