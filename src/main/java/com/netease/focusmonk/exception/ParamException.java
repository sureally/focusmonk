package com.netease.focusmonk.exception;

/**
 * @Description 请求参数异常
 * @ClassName ParamException
 * @Author konghaifeng
 * @Date 2019/4/30 11:22
 **/
public class ParamException extends RuntimeException {

    public ParamException() {
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }

    public ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
