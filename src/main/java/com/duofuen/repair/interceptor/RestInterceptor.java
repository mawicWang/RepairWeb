package com.duofuen.repair.interceptor;

import com.alibaba.fastjson.JSON;
import com.duofuen.repair.domain.RestToken;
import com.duofuen.repair.domain.RestTokenRepository;
import com.duofuen.repair.rest.BaseResponse;
import com.duofuen.repair.rest.RbNull;
import com.duofuen.repair.util.Const;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

/**
 * intercept every rest request path like "/rest/**"
 * check if it is authorized
 *
 * @see com.duofuen.repair.configuration.WebAppConfiguration
 */
public class RestInterceptor extends HandlerInterceptorAdapter {

    /**
     * The Rest token repository.
     */
    private RestTokenRepository restTokenRepository;

    /**
     * Instantiates a new Rest interceptor.
     *
     * @param restTokenRepository the rest token repository
     */
    public RestInterceptor(RestTokenRepository restTokenRepository) {
        this.restTokenRepository = restTokenRepository;
    }


    /**
     * fetch header "RestToken" from request
     * and check if the token exists and not expired
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String msg;
        // get token from request
        String token = request.getHeader(Const.Rest.HTTP_HEADER_TOKEN);

        if (StringUtils.isEmpty(token)) {
            msg = "no header names \"RestToken\" is found!";
        } else {
            // check token
            RestToken restToken = restTokenRepository.findByToken(token);
            if (null == restToken) {
                msg = "invalid token : " + token;
            } else if (restToken.getExpireTime().toInstant().isBefore(Instant.now())) {
                // check token active
                msg = "token expired, please login again";
            } else {
                return true;
            }
        }
        response.setContentType("application/json");
        BaseResponse<RbNull> res = new BaseResponse<>(Const.Rest.UNAUTHORIZED, msg, null);
        response.getWriter().append(JSON.toJSONString(res));
        return false;
    }
}
