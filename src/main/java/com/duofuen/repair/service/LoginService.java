package com.duofuen.repair.service;

import com.duofuen.repair.domain.UserRepository;
import com.duofuen.repair.domain.ValCodeRepository;
import com.duofuen.repair.domain.User;
import com.duofuen.repair.domain.ValCode;
import com.duofuen.repair.rest.RbLogin;
import com.duofuen.repair.util.ChuangLanSmsUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
public class LoginService {

    private static final Logger LOGGER = LogManager.getLogger();

    private final UserRepository userRepository;
    private final ValCodeRepository valCodeRepository;

    @Autowired
    public LoginService(UserRepository userRepository, ValCodeRepository valCodeRepository) {
        this.userRepository = userRepository;
        this.valCodeRepository = valCodeRepository;
    }

    @Transactional
    public RbLogin loginByPhoneNum(String phoneNum, String pwd, String openId) {
        RbLogin rbLogin = null;
        User user = userRepository.findByPhoneNum(phoneNum);
        ValCode validateCode = valCodeRepository.findByPhoneNum(phoneNum);
        if (null != user && null != validateCode
                // validate code should be used in 5 minutes
                && Duration.between(validateCode.getCreateTime().toInstant(), Instant.now()).getSeconds() < 300
                && validateCode.getCode().equals(pwd)) {
            LOGGER.info("phone number {} matches validate code, previous openId is {}, current is {}, bind success",
                    phoneNum, user.getOpenId(), openId);
            user.setOpenId(openId);
            userRepository.save(user);
            rbLogin = new RbLogin(user.getRoleCode(), user.getId().toString());
        }
        return rbLogin;
    }

    public RbLogin loginByOpenId(String openId) {
        User user = userRepository.findByOpenId(openId);
        if (null != user) {
            LOGGER.info("openid {} login successful", openId);
            return new RbLogin(user.getRoleCode(), user.getId().toString());
        }
        LOGGER.info("openid {} login fail", openId);
        return null;
    }

    public boolean checkPhoneNumExists(String phoneNum) {
        User u = userRepository.findByPhoneNum(phoneNum);
        return null != u;
    }

    public boolean sendValidateCode(String phoneNum) {
        // if createtime < 300s
        ValCode valCode = valCodeRepository.findByPhoneNum(phoneNum);
        if (null != valCode && Duration.between(valCode.getCreateTime().toInstant(), Instant.now()).getSeconds() < 300) {
            LOGGER.info("phone number {} already sent validate code in 5 minutes", phoneNum);
            return false;
        }

        // save phoneNum + validateCode + createTime
        // cover if already exits
        String code = getVerifyCode();
        if (null == valCode) {
            valCode = new ValCode();
            valCode.setPhoneNum(phoneNum);
        }
        valCode.setCode(code);
        valCode.setCreateTime(new Date());
        valCodeRepository.save(valCode);
        LOGGER.info("phone number {} generated validate code {}", phoneNum, code);

        // TODO send message validate time 300s
        ChuangLanSmsUtil.sendValidateCode(phoneNum, code);
        return true;
    }

    private String getVerifyCode() {
        String[] verifyString = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Random random = new Random(System.currentTimeMillis());
        StringBuilder verifyBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int rd = random.nextInt(10);
            verifyBuilder.append(verifyString[rd]);
        }
        String verifyCode = verifyBuilder.toString();
        return verifyCode;
    }

}
