package com.netease.focusmonk.exception;

import javax.servlet.ServletException;

/**
 * @ClassName JwtLackException
 * @Author konghaifeng
 * @Date 2019/5/10 11:53
 **/
public class JwtLackException extends ServletException {

    public JwtLackException() {
    }

    public JwtLackException(String message) {
        super(message);
    }

    public JwtLackException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public JwtLackException(Throwable rootCause) {
        super(rootCause);
    }
}
