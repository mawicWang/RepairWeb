package com.duofuen.repair.Configuration;

import com.duofuen.repair.rest.BaseResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "com.duofuen.repair.rest")
public class RestControllerAdvice {

    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(Exception.class)
    @ResponseBody
    BaseResponse<?> handleException(Exception e) {
        LOGGER.info("handling Exception {}, {}", e.getClass(), e.getMessage());
        return BaseResponse.fail(e.toString());
    }
}
