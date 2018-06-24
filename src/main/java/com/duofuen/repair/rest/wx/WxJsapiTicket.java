package com.duofuen.repair.rest.wx;

import java.time.Instant;

public class WxJsapiTicket {

    private String ticket;
    private Instant expireTime;

    public static WxJsapiTicket newWxJsapiTicket(String accessToken) {
        WxJsapiTicket wxJsapiTicket = new WxJsapiTicket();
        wxJsapiTicket.setTicket(accessToken);
        wxJsapiTicket.setExpireTime(Instant.now().plusSeconds(7200));
        return wxJsapiTicket;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Instant getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
    }
}
