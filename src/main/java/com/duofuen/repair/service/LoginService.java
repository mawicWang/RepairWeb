package com.duofuen.repair.service;

import com.duofuen.repair.domain.Character;
import com.duofuen.repair.domain.*;
import com.duofuen.repair.rest.RbLogin;
import com.duofuen.repair.util.ChuangLanSmsUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class LoginService {

    private static final Logger LOGGER = LogManager.getLogger();

    private final CharacterRepository characterRepository;
    private final ValCodeRepository valCodeRepository;
    private final RestTokenRepository restTokenRepository;

    @Autowired
    public LoginService(CharacterRepository characterRepository, ValCodeRepository valCodeRepository, RestTokenRepository restTokenRepository) {
        this.characterRepository = characterRepository;
        this.valCodeRepository = valCodeRepository;
        this.restTokenRepository = restTokenRepository;
    }

    @Transactional
    public RbLogin loginByPhoneNum(String phoneNum, String pwd, String openId) {
        RbLogin rbLogin = null;
        Character character = characterRepository.findByPhoneNum(phoneNum);
        ValCode validateCode = valCodeRepository.findByPhoneNum(phoneNum);
        if (null != character && null != validateCode
                // validate code should be used in 5 minutes
                && Duration.between(validateCode.getCreateTime().toInstant(), Instant.now()).getSeconds() < 300
                && validateCode.getCode().equals(pwd)) {
            LOGGER.info("phone number {} matches validate code, previous openId is {}, current is {}, bind success",
                    phoneNum, character.getOpenId(), openId);
            character.setOpenId(openId);
            characterRepository.save(character);

            rbLogin = new RbLogin(character.getRoleCode(), character.getId().toString());
            rbLogin.setToken(updateToken(character.getId()));        // update token
        }
        return rbLogin;
    }

    public RbLogin loginByOpenId(String openId) {
        Character character = characterRepository.findByOpenId(openId);
        if (null != character) {
            LOGGER.info("openid {} login successful", openId);
            RbLogin rbLogin = new RbLogin(character.getRoleCode(), character.getId().toString());
            rbLogin.setToken(updateToken(character.getId()));     // update token
            return rbLogin;
        }
        LOGGER.info("openid {} login fail", openId);
        return null;
    }

    @Transactional
    protected String updateToken(Integer characterId) {
        Date expireTime = Date.from(Instant.now().plusSeconds(1800));
        RestToken restToken = restTokenRepository.findByCharacterId(characterId);
        if (null != restToken) {
            // if not expire, update expireTime
            // if expired, generate new token and update expireTime
            if (Instant.now().isAfter(restToken.getExpireTime().toInstant())) {
                String token = Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes());
                restToken.setToken(token);
            }
            restToken.setExpireTime(expireTime);
        } else {
            restToken = new RestToken();
            restToken.setCharacterId(characterId);
            restToken.setToken(Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes()));
            restToken.setExpireTime(expireTime);
        }
        LOGGER.info("rest token updated character id {}, token {}， expireTime {}", characterId, restToken.getToken(), expireTime);
        restTokenRepository.save(restToken);
        return restToken.getToken();
    }


    public boolean checkPhoneNumExists(String phoneNum) {
        Character character = characterRepository.findByPhoneNum(phoneNum);
        return null != character;
    }


    /**
     * Send validate code string.
     *
     * @param phoneNum the phone num
     * @return true if success
     * false if sent too frequent (300s)
     */
    public boolean sendValidateCode(String phoneNum) {
        try {
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

            // send message validate time 300s
            ChuangLanSmsUtil.sendValidateCode(phoneNum, code);
            return true;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new RuntimeException("send validate code msg fail", e);
        }

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
