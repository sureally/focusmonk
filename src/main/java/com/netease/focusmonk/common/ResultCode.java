package com.netease.focusmonk.common;

/**
 * @author hejiecheng
 * @Date 2019-04-28
 *
 * 通用：000XXX
 * 登录：001XXX
 * 独处学习：002XXX
 * 个人中心：003XXX
 * 多人共同学习：004XXX
 */
public enum ResultCode {

    SUCCESS("000000", "success"),
    PARAM_ERROR("000001", "invalid parameter"),
    BUSINESS_ERROR("000002", "business error"),
    //统一异常
    GENERAL_ERROR("000003", "General error"),
    //前端请求参数异常
    REQUEST_PARAMETER_EXCEPTION("000004", "Http request parameter error"),
    JWT_ERROR("001003", "jwt error"),
    PERSON_CENTER_ERROR("003002","person center error"),
    CODE_INVALID("001001", "code invalid"),
    CODE_ERROR("001002", "code error"),



    // Start Write By KHF.

    //保存学习任务记录接口请求参数异常
    TASK_DETAIL_PARAM_ERROR("002001", "保存学习任务记录接口请求参数有误"),

    //学习任务记录数据完整性异常
    TASK_DETAIL_DATA_ERROR("002002", "学习任务记录数据完整性有误"),

    // End Write By KHF.

    WHITE_NOISE_PARAM_ERROR("005001", "白噪声方案请求参数错误"),
    WHITE_NOISE_ERROR("005002", "白噪声方案错误"),

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
