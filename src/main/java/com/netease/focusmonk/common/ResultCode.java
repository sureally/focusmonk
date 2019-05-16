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

    /**
     * 请求成功
     */
    SUCCESS("000000", "success"),
    /**
     * 参数问题
     */
    PARAM_ERROR("000001", "invalid parameter"),
    /**
     * 业务异常
     */
    BUSINESS_ERROR("000002", "business error"),
    /**
     * 通用错误
     */
    GENERAL_ERROR("000003", "General error"),
    /**
     * 请求参数异常
     */
    REQUEST_PARAMETER_EXCEPTION("000004", "Http request parameter error"),

    /**
     * 验证码非法
     */
    CODE_INVALID("001001", "code invalid"),
    /**
     * 验证码错误
     */
    CODE_ERROR("001002", "code error"),
    /**
     * 登录凭证JWT错误
     */
    JWT_ERROR("001003", "jwt error"),
    /**
     * 手机号非法
     */
    PHONE_ERROR("001004", "手机号不正确"),
    /**
     * 用户名错误
     */
    USERNAME_ERROR("001005", "用户名不能为空"),
    /**
     * 云信验证码发送失败
     */
    SMS_CODE_ERROR("001006","验证码发送失败"),
    /**
     * 请求验证码验证的请求间隔验证
     */
    SMS_CODE_LIMIT("001007", "验证码请求频繁"),
    /**
     * 短信请求间隔验证
     */
    SMS_SEND_LIMIT("001008", "短信请求频繁"),

    /**
     * 保存学习任务记录接口请求参数异常
     */
    TASK_DETAIL_PARAM_ERROR("002001", "保存学习任务记录接口请求参数有误"),
    /**
     * 学习任务记录数据完整性异常
     */
    TASK_DETAIL_DATA_ERROR("002002", "学习任务记录数据完整性有误"),

    /**
     * 个人中心错误
     */
    PERSON_CENTER_ERROR("003002","person center error"),
    /**
     * PUSH推送异常
     */
    PUSH_ERROR("003003","push error"),
    /**
     * 多人房间人满
     */
    FULL_ROOM_ERROR("004001", "该房间已经满员"),
    /**
     * 一个用户只允许进入一个房间
     */
    USER_REPEATEDLY_ENTERS_ROOM_ERROR("004002", "用户不得重复进入房间"),
    /**
     * 用户信息有误
     */
    USER_INFO_PUT_REDIS_HASH_ERROR("004003", "用户信息格式有误"),
    /**
     * 请求房间不存在
     */
    ROOM_NOT_EXIST_ERROR("004004", "该房间不存在"),
    /**
     * 用户退出房间
     */
    USER_EXITED_ROOM("004005", "用户已退出房间"),
    /**
     * 用户退出房间异常
     */
    USER_EXIT_ROOM_ERROR("004006", "用户退出房间异常"),
    /**
     * 多人学习参数异常
     */
    STUDY_TOGETHER_PARAM_ERROR("007001", " 多人学习接口请求参数异常"),
    /**
     * 多人学习通用异常
     */
    STUDY_TOGETHER_GENERAL_ERROR("007002", " 多人学习接口通用异常"),
    /**
     * 多人学习请求成功
     */
    STUDY_TOGETHER_SUCEESS("007000", " 多人学习接口成功"),

    /**
     * 白噪音请求异常
     */
    WHITE_NOISE_PARAM_ERROR("005001", "白噪声方案请求参数错误"),
    /**
     * 白噪音方案信息错误
     */
    WHITE_NOISE_ERROR("005002", "白噪声方案错误"),

    UNKNOWN("999999", "unknow error");

    private String code;
    private String message;

    ResultCode(String code, String message) {
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
