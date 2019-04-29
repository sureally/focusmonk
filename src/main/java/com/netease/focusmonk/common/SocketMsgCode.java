package com.netease.focusmonk.common;

import com.google.gson.Gson;

/**
 * @author hejiecheng
 * @Date 2019-04-29
 */
public enum SocketMsgCode {

    SUCCESS_CONNECT("000000", "socket连接成功"),
    FAIL_CONNECT("000001", "socket连接失败"),
    SUCCESS_LOGOUT("000002", "用户以下线"),
    FORCE_LOFOUT_MSG("999999", "您的账号在另外一个设备登录，您被迫下线，当前学习过程自动结束!");

    private String code;
    private String message;

    private SocketMsgCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
