package com.netease.focusmonk.exception;

/**
 * @Description 通用异常
 * @ClassName GeneralException
 * @Author konghaifeng
 * @Date 2019/4/30 11:23
 **/
public class GeneralException extends Exception {

    public GeneralException() {
    }

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralException(Throwable cause) {
        super(cause);
    }

    public GeneralException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
