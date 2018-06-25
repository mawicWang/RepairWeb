package com.duofuen.repair.rest.wx;

import com.alibaba.fastjson.JSON;
import com.duofuen.repair.rest.BaseResponse;
import com.duofuen.repair.util.Const;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.rmi.UnexpectedException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class WxController implements ServletContextAware {

    private static final Logger LOGGER = LogManager.getLogger();

    private ServletContext servletContext;

    /**
     * Get wx access token from data base
     * if not exists or expired
     * refresh access token
     *
     * @return the accessToken
     */
//    @RequestMapping(path = "/wx/getAccessToken")
    private String getAccessToken() {
        WxAccessToken wxAccessToken = (WxAccessToken) servletContext.getAttribute(Const.Wx.ACCESS_TOKEN);
        // if no access token saved or it has expired (7000s)
        if (null == wxAccessToken || wxAccessToken.getExpireTime().minusSeconds(200).isBefore(Instant.now())) {
            // get access token
            Map<String, String> param = new HashMap<>();
            param.put("grant_type", "client_credential");
            param.put("appid", Const.Wx.APP_ID);
            param.put("secret", Const.Wx.SECRET);

            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type={grant_type}&appid={appid}&secret={secret}";

            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
            AccessTokenResponse resp = restTemplate.getForObject(url, AccessTokenResponse.class, param);
            LOGGER.info("get weixin access token success : {}", JSON.toJSONString(resp));

            // cache access token
            wxAccessToken = WxAccessToken.newWxAccessToken(resp.getAccessToken());
            servletContext.removeAttribute(Const.Wx.ACCESS_TOKEN);
            servletContext.setAttribute(Const.Wx.ACCESS_TOKEN, wxAccessToken);
        }
        LOGGER.info("current access_token will expire in {} seconds, which is {} , {}",
                Duration.between(Instant.now(), wxAccessToken.getExpireTime()).getSeconds(), wxAccessToken.getAccessToken(), wxAccessToken.getExpireTime());
        return wxAccessToken.getAccessToken();
    }


    /**
     * Get wx jsapi_ticket from data base
     * if not exists or expired
     * refresh jsapi_ticket
     *
     * @return the jsapi ticket
     */
//    @RequestMapping(path = "/wx/getJsapiTicket")
    private String getJsapiTicket() throws UnexpectedException {
        WxJsapiTicket wxJsapiTicket = (WxJsapiTicket) servletContext.getAttribute(Const.Wx.JSAPI_TICKET);
        // if no jsapi_ticket saved or it has expired (7000s)
        if (null == wxJsapiTicket || wxJsapiTicket.getExpireTime().minusSeconds(200).isBefore(Instant.now())) {
            // get jsapi_ticket
            String accessToken = getAccessToken();

            String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={accessToken}&type=jsapi";

            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
            JsapiTicketResponse resp = restTemplate.getForObject(url, JsapiTicketResponse.class, accessToken);
            LOGGER.info("get jsapi_ticket success : {}", JSON.toJSONString(resp));

            if (resp.getErrcode() != 0) {
                // some error occurs
                LOGGER.error("error getting jsapi_ticket");
                throw new UnexpectedException("unknown error get weixin jsapi_ticket!");
            }

            // cache jsapi_ticket
            wxJsapiTicket = WxJsapiTicket.newWxJsapiTicket(resp.getTicket());
            servletContext.removeAttribute(Const.Wx.JSAPI_TICKET);
            servletContext.setAttribute(Const.Wx.JSAPI_TICKET, wxJsapiTicket);
        }
        LOGGER.info("current jsapi_ticket will expire in {} seconds, which is {} , {}",
                Duration.between(Instant.now(), wxJsapiTicket.getExpireTime()).getSeconds(), wxJsapiTicket.getTicket(), wxJsapiTicket.getExpireTime());
        return wxJsapiTicket.getTicket();
    }

    /**
     * wx.config({
     *      debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
     *      appId: '', // 必填，公众号的唯一标识
     *      timestamp: , // 必填，生成签名的时间戳
     *      nonceStr: '', // 必填，生成签名的随机串
     *      signature: '',// 必填，签名
     *      jsApiList: [] // 必填，需要使用的JS接口列表
     * });
     *
     * @param url the url
     */
    @RequestMapping("/wx/getWxSignature")
    public BaseResponse<RbWxSignature> getWxSignature(String url) throws UnexpectedException {
        LOGGER.info("==>restful method getWxSignature called, url: " + url);
        BaseResponse<RbWxSignature> baseResponse;
        if (StringUtils.isEmpty(url)) {
            baseResponse = BaseResponse.fail("empty parameter url");
        }   else {
            // build signature
            String jsapiTicket = getJsapiTicket();
            String noncestr = UUID.randomUUID().toString().replace("-", "");
            String timestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

            StringBuffer sb = new StringBuffer();
            sb.append("jsapi_ticket=").append(jsapiTicket);
            sb.append("&noncestr=").append(noncestr);
            sb.append("&timestamp=").append(timestamp);
            sb.append("&url=").append(url);

            String signature = DigestUtils.sha1Hex(sb.toString());

            RbWxSignature wxSignature = new RbWxSignature();
            wxSignature.setAppId(Const.Wx.APP_ID);
            wxSignature.setTimestamp(timestamp);
            wxSignature.setNonceStr(noncestr);
            wxSignature.setSignature(signature);

            baseResponse = BaseResponse.success(wxSignature);
        }

        return baseResponse;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
