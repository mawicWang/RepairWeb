package com.duofuen.repair.service;

import com.duofuen.repair.rest.RbLogin;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static String openId;

    public RbLogin loginByPhoneNum(String phoneNum, String pwd, String openId) {
        RbLogin rbLogin = null;
        if (phoneNum.equals("123456789") && pwd.equals("9999")) {
            // bind openId
            LoginService.openId = openId;
            rbLogin = new RbLogin("00", "88");
        }
        return rbLogin;
    }

    public RbLogin loginByOpenId(String openId) {
        RbLogin rbLogin = null;
        if (openId.equals(LoginService.openId)) {
            rbLogin = new RbLogin("00", "88");
        }
        return rbLogin;
    }

}
