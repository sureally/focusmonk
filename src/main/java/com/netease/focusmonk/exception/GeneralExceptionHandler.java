package com.netease.focusmonk.exception;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description 通用全局异常处理
 * @ClassName GeneralExceptionHandler
 * @Author konghaifeng
 * @Date 2019/4/30 20:07
 **/

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public JsonResult uniteExceptionHandler(Exception e) {

        log.error("捕获到 Exception 异常", e);

        return JsonResult.getErrorResult();
    }

    @ResponseBody
    @ExceptionHandler(ParamException.class)
    public JsonResult paramExceptionHandler(ParamException pe) {

        log.error("捕获到参数异常ParamException", pe);

        return JsonResult.getCustomResult(ResultCode.TASK_DETAIL_PARAM_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(GeneralException.class)
    public JsonResult generalExceptionHandler(GeneralException ge) {

        log.error("捕获到通用异常GeneralException", ge);

        return JsonResult.getCustomResult(ResultCode.GENERAL_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public JsonResult bindExceptionHandler(BindException be) {

        log.error("捕获到参数绑定异常 BindException", be);

        return JsonResult.getCustomResult(ResultCode.REQUEST_PARAMETER_EXCEPTION);
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public JsonResult requestParameterExceptionHandler(MissingServletRequestParameterException msrpe) {

        log.error("捕获到请求参数异常 MissingServletRequestParameterException", msrpe);

        return JsonResult.getCustomResult(ResultCode.REQUEST_PARAMETER_EXCEPTION);
    }


}
