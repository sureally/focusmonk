package com.netease.focusmonk.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.utils.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author hejiecheng
 * @Date 2019-05-05
 */
public class JWTInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwt = request.getParameter("jwt");
        if (jwt != null && !jwt.isEmpty() && JWTUtil.checkJWT(jwt)) {
            return true;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.write(JSONObject.toJSONString(JsonResult.getCustomResult(ResultCode.JWT_ERROR, "jwt失效")));
        } catch (Exception e) {
            response.sendError(500);
            e.printStackTrace();
            return false;
        } finally {
            if(printWriter != null) {
                try {
                    printWriter.close();
                } catch (Exception e) {
                    response.sendError(500);
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }
}
