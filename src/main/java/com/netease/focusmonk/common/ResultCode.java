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

    // Start Write By KHF.

    //保存学习任务记录接口请求参数异常
    TASK_DETAIL_PARAM_ERROR("002001", "保存学习任务记录接口请求参数有误"),

    //学习任务记录数据完整性异常
    TASK_DETAIL_DATA_ERROR("002002", "学习任务记录数据完整性有误"),

    // End Write By KHF.

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
