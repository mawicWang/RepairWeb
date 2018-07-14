package com.duofuen.repair.configuration;

import com.duofuen.repair.rest.BaseResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

@ControllerAdvice(basePackages = "com.duofuen.repair.rest")
public class RestControllerAdvice {

    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse<?> handleException(Exception e) {
        LOGGER.info("handling Exception {}, {}", e.getClass(), e.getMessage());
        LOGGER.error(e);
        return BaseResponse.fail(e.toString());
    }

    // valid exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseResponse<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("Invalid Request:");

        for (FieldError fieldError : bindingResult.getFieldErrors())
            errorMessage.append(fieldError.getDefaultMessage()).append(", ");

        LOGGER.info(bindingResult.getFieldError().getDefaultMessage());
        LOGGER.info(errorMessage.toString());
        LOGGER.error(ex);
        return BaseResponse.fail(errorMessage.toString());
    }

    // JSON convert exception
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public BaseResponse<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        LOGGER.error(ex);
        return BaseResponse.fail("json convert failure!");
    }
}
