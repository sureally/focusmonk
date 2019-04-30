package com.netease.focusmonk.common;

/**
 * @author hejiecheng
 * @Date 2019-04-28
 */
public enum ResultCode {

    SUCCESS("000000", "success"),
    PARAM_ERROR("000001", "invalid parameter"),
    BUSINESS_ERROR("000002", "business error"),
    WHITE_NOISE_ERROR("005002", "white noise error"),
    CODE_INVALID("001001", "code invalid"),
    CODE_ERROR("001002", "code error"),
    UNKNOWN("999999", "unknow error");

    private String code;
    private String message;

    private ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
