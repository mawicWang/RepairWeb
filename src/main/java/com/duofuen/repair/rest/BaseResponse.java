package com.duofuen.repair.rest;

import static com.duofuen.repair.util.Const.Rest.*;

public class BaseResponse<RB extends BaseResultBody> {

    // success - null ; fail - reason;
    private String resultMessage;
    // default : success - 00 ; fail - 99
    private String resultCode;
    // decided by rest api
    private RB result;

    private BaseResponse(String resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public BaseResponse(String resultCode, String resultMessage, RB result) {
        this(resultCode, resultMessage);
        this.result = result;
    }

    private BaseResponse(RB result) {
        this.resultCode = SUCCESS;
        this.resultMessage = null;
        this.result = result;
    }

    public static <R extends BaseResultBody> BaseResponse<R> success(R result) {
        return new BaseResponse<>(result);
    }

    public static <R extends BaseResultBody> BaseResponse<R> fail(String failMsg) {
        return new BaseResponse<>(FAIL, failMsg);
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public RB getResult() {
        return result;
    }

    public void setResult(RB result) {
        this.result = result;
    }
}
