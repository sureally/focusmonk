package com.netease.focusmonk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.common.SocketMsgCode;
import com.netease.focusmonk.handler.DeviceWebSocketHandler;
import com.netease.focusmonk.model.User;
import com.netease.focusmonk.service.LoginServiceImpl;
import com.netease.focusmonk.utils.JWTUtil;
import com.netease.focusmonk.utils.RedisUtil;
import com.netease.focusmonk.utils.SMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author hejiecheng
 * @Date 2019-04-29
 */

@Slf4j
@RestController
@RequestMapping("/SDSController")
public class SDSController {

    private static final long PHONE_CODE_TIME = 300;
    private static final String PHONE_KEY_PREFIX = "phone-";

    private final DeviceWebSocketHandler deviceWebSocketHandler;
    private final RedisUtil redisUtil;
    private final LoginServiceImpl loginService;

    @Autowired
    public SDSController(DeviceWebSocketHandler deviceWebSocketHandler, RedisUtil redisUtil, LoginServiceImpl loginService) {
        this.deviceWebSocketHandler = deviceWebSocketHandler;
        this.redisUtil = redisUtil;
        this.loginService = loginService;
    }

    /**
     * 登陆接口
     * @param phone
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResult login(@RequestParam(value = "phone") String phone,
                            @RequestParam(value = "code") String code) throws Exception {
        // 验证验证码
        String phoneName = PHONE_KEY_PREFIX + phone;
        if (!redisUtil.hasKey(phoneName)) {
            return JsonResult.getCustomResult(ResultCode.CODE_INVALID);
        }
        String systemCode = String.valueOf(redisUtil.getKey(phoneName));
        if (!systemCode.equals(code)) {
            return JsonResult.getCustomResult(ResultCode.CODE_ERROR);
        }
        // 验证新老用户
        Map<String, Object> detail = new HashMap<>();
        User user = loginService.verifyOldOrNew(phone);
        if (user == null) {
            // 说明这个是一个新用户
            user = new User();
            user.setPhone(phone);
            user.setUsername("小和尚"+codeGenerate());
            loginService.addNewUser(user);
            detail.put("type", "new");
        } else {
            detail.put("type", "old");
        }
        log.info("当前用户id:{}", user.getId());
        // 给用户生成jwt
        Map<String, Object> sessionInfo = new HashMap<>();
        sessionInfo.put("userId", user.getId());
        String sessionJson = JSON.toJSONString(sessionInfo);
        String jwt = JWTUtil.buildJWT(sessionJson);
        // 返回结果
        detail.put("jwt", jwt);
        return JsonResult.getSuccessResult(detail);
    }

    /**
     * 登出接口，暂不使用
     * @param request
     * @return
     * @throws Exception
     */
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

    /**
     * 验证码发送接口
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendSMS", method = RequestMethod.GET)
    public JsonResult sendSMS(@RequestParam(value = "phone") String phone) throws Exception {
        String code = codeGenerate();
        if (SMSUtil.sendSMS(phone, code)) {
            // 存到redis中
            redisUtil.set(PHONE_KEY_PREFIX + phone, code, PHONE_CODE_TIME);
            return JsonResult.getSuccessResult();
        } else {
            // 发送失败
            return JsonResult.getErrorResult();
        }
    }

    /**
     * app启动时以jwt旧换新
     * @param jwt
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public JsonResult refreshJWT(@RequestParam(value = "jwt") String jwt) throws Exception {
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        String newJWT = JWTUtil.buildJWT(jwtJson);
        Map<String, Object> detail = new HashMap<>();
        detail.put("jwt", newJWT);
        return JsonResult.getSuccessResult(detail);
    }

    /**
     * 生成6位验证码
     * @return
     */
    private String codeGenerate() {
        Random random = new Random();
        String result="";
        for (int i=0;i<6;i++)
        {
            result+=random.nextInt(10);
        }
        return result;
    }

}
