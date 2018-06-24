package com.duofuen.repair.rest.wx;

import java.time.Instant;

public class WxAccessToken {

    private String accessToken;
    private Instant expireTime;

    public static WxAccessToken newWxAccessToken(String accessToken) {
        WxAccessToken wxAccessToken = new WxAccessToken();
        wxAccessToken.setAccessToken(accessToken);
        wxAccessToken.setExpireTime(Instant.now().plusSeconds(7200));
        return wxAccessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Instant getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
    }
}
