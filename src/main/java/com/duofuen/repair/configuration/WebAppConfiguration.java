package com.duofuen.repair.configuration;

import com.duofuen.repair.domain.RestTokenRepository;
import com.duofuen.repair.interceptor.RestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Web app configuration.
 */
@Configuration
public class WebAppConfiguration implements WebMvcConfigurer {

    /**
     * The Rest token repository.
     */
    @Autowired
    private RestTokenRepository restTokenRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RestInterceptor(restTokenRepository))
                .addPathPatterns("/rest/**")
                .excludePathPatterns("/rest/loginByOpenId", "/rest/loginByPhoneNum", "/rest/getValidateCode");
    }
}
