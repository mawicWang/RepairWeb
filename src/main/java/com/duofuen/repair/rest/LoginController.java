package com.duofuen.repair.rest;

import com.duofuen.repair.service.LoginService;
import com.duofuen.repair.util.Const;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.duofuen.repair.util.Const.Rest.INVALID_PHONE_NUMBER;
import static com.duofuen.repair.util.Const.Rest.ROOT_PATH;

@RestController
@RequestMapping(path = ROOT_PATH)
public class LoginController {

    private static final Logger LOGGER = LogManager.getLogger();

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(path = "/getValidateCode")
    public BaseResponse<RbNull> getValidateCode(@RequestBody HashMap<String, String> map) {
        LOGGER.info("==>restful method getValidateCode called, parameter: " + map);
        BaseResponse<RbNull> baseResponse;

        String phoneNum = map.get(Const.Rest.LOGIN_PHONENUM);
        if (StringUtils.isEmpty(phoneNum)) {
            baseResponse = BaseResponse.fail("empty parameter phoneNum");
        } else if (!loginService.checkPhoneNumExists(phoneNum)) {
            baseResponse = new BaseResponse<>(INVALID_PHONE_NUMBER,
                    "后台无此号码，请联系系统管理员", null);
        } else {
            try {
                // 发送验证码，返回true表示成功发送，false表示5分钟内重复发送
                // 其他失败由exception处理
                boolean sent = loginService.sendValidateCode(phoneNum);
                if (sent) {
                    baseResponse = BaseResponse.success(new RbNull());
                } else {
                    baseResponse = new BaseResponse<>(Const.Rest.VARIFY_CODE_SENT_TOO_FREQUENT,
                            "验证码5分钟内有效，请不要频繁操作。", null);
                }
            } catch (Throwable t) {
                LOGGER.error(t);
                baseResponse = BaseResponse.fail("短信发送失败");
            }
        }
        return baseResponse;
    }

    @PostMapping(path = "/loginByPhoneNum")
    public BaseResponse<RbLogin> loginByPhoneNum(@RequestBody HashMap<String, String> map) {
        LOGGER.info("==>restful method loginByPhoneNum called, parameter: " + map);
        BaseResponse<RbLogin> baseResponse;

        String phoneNum = map.get(Const.Rest.LOGIN_PHONENUM);
        String pwd = map.get(Const.Rest.LOGIN_PASSWORD);
        String openId = map.get(Const.Rest.LOGIN_OPEN_ID);
        if (StringUtils.isEmpty(phoneNum) || StringUtils.isEmpty(pwd) || StringUtils.isEmpty(openId)) {
            baseResponse = BaseResponse.fail("empty parameter phoneNum/pwd/openId");
        } else {
            RbLogin rbLogin = loginService.loginByPhoneNum(phoneNum, pwd, openId);
            baseResponse = BaseResponse.packResultBody(rbLogin, "no phoneNum or pwd wrong");
        }
        return baseResponse;

    }

    @PostMapping(path = "/loginByOpenId")
    public BaseResponse<RbLogin> loginByOpenId(@RequestBody HashMap<String, String> map) {
        LOGGER.info("==>restful method loginByOpenId called, parameter: " + map);
        BaseResponse<RbLogin> baseResponse;

        String openId = map.get(Const.Rest.LOGIN_OPEN_ID);
        if (StringUtils.isEmpty(openId)) {
            baseResponse = BaseResponse.fail("empty parameter openId");
        } else {
            RbLogin rbLogin = loginService.loginByOpenId(openId);
            baseResponse = BaseResponse.packResultBody(rbLogin, "invalid openId");
        }
        return baseResponse;
    }

}
