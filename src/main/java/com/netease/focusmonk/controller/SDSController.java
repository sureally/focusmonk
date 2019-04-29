package com.netease.focusmonk.controller;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.SocketMsgCode;
import com.netease.focusmonk.handler.DeviceWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author hejiecheng
 * @Date 2019-04-29
 */

@RestController
@RequestMapping("/SDSController")
public class SDSController {

    private DeviceWebSocketHandler deviceWebSocketHandler;

    @Autowired
    public SDSController(DeviceWebSocketHandler deviceWebSocketHandler) {
        this.deviceWebSocketHandler = deviceWebSocketHandler;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResult login(@RequestParam(value = "username") String username, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        // 生成登录token
        session.setAttribute("username", username);
        return JsonResult.getSuccessResult();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public JsonResult logout(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = session.getAttribute("username").toString();
            deviceWebSocketHandler.sendMessageToUser(username, SocketMsgCode.SUCCESS_LOGOUT.getJson());
            session.invalidate();
        }
        return JsonResult.getSuccessResult();
    }

}
