package com.duofuen.repair.rest;

import com.duofuen.repair.service.LoginService;
import com.duofuen.repair.util.Const;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class CommonController {

    private static final Logger LOGGER = LogManager.getLogger();

    private final LoginService loginService;

    @Autowired
    public CommonController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(path = "/loginByPhoneNum")
    public BaseResponse<RbLogin> loginByPhoneNum(@RequestBody HashMap<String, String> map) {
        LOGGER.info("restful method loginByPhoneNum called, parameter: " + map);
        BaseResponse<RbLogin> baseResponse;

        String phoneNum = map.get(Const.Rest.LOGIN_PHONENUM);
        String pwd = map.get(Const.Rest.LOGIN_PASSWORD);
        String openId = map.get(Const.Rest.LOGIN_OPEN_ID);
        if (StringUtils.isEmpty(phoneNum) || StringUtils.isEmpty(pwd) || StringUtils.isEmpty(openId)) {
            baseResponse = BaseResponse.fail("empty parameter phoneNum/pwd/openId");
        } else {
            RbLogin rbLogin = loginService.loginByPhoneNum(phoneNum, pwd, openId);
            baseResponse = packResultBody(rbLogin, "no phoneNum or pwd wrong");
        }
        return baseResponse;

    }

    @GetMapping("/loginByOpenId")
    public BaseResponse<RbLogin> loginByOpenId(String openId) {
        LOGGER.info("restful method loginByOpenId called, openId: " + openId);
        BaseResponse<RbLogin> baseResponse;

        if (StringUtils.isEmpty(openId)) {
            baseResponse = BaseResponse.fail("empty parameter openId");
        } else {
            RbLogin rbLogin = loginService.loginByOpenId(openId);
            baseResponse = packResultBody(rbLogin, "invalid openId");
        }
        return baseResponse;
    }

    private <R extends BaseResultBody> BaseResponse<R> packResultBody(R rb, String nullMsg) {
        if (rb == null) {
            return BaseResponse.fail(nullMsg);
        } else {
            return BaseResponse.success(rb);
        }
    }
}
