package com.netease.focusmonk.controller;

import com.alibaba.fastjson.JSON;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.common.SocketMsgCode;
import com.netease.focusmonk.handler.DeviceWebSocketHandler;
import com.netease.focusmonk.model.User;
import com.netease.focusmonk.service.LoginServiceImpl;
import com.netease.focusmonk.service.SMSServiceImpl;
import com.netease.focusmonk.utils.JWTUtil;
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

/**
 * @author hejiecheng
 * @Date 2019-04-29
 */

@Slf4j
@RestController
@RequestMapping("/SDSController")
public class SDSController {

    private final LoginServiceImpl loginService;
    private final SMSServiceImpl smsService;

    private final static String[] nickName = {"一禅","一休","贤二"};

    @Autowired
    public SDSController(LoginServiceImpl loginService, SMSServiceImpl smsService) {
        this.loginService = loginService;
        this.smsService = smsService;
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
        if (code.length() != 6) {
            return JsonResult.getCustomResult(ResultCode.CODE_ERROR);
        }
        if (!smsService.verifyCode(phone, code)) {
            log.info("手机号：{}，验证码：{}验证失败!", phone, code);
            return JsonResult.getCustomResult(ResultCode.CODE_INVALID);
        }
        // 验证新老用户
        Map<String, Object> detail = new HashMap<>();
        User user = loginService.verifyOldOrNew(phone);
        if (user == null) {
            // 说明这个是一个新用户
            user = new User();
            user.setPhone(phone);
            user.setUsername(phone + codeGenerate());
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
     * 验证码发送接口
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendSMS", method = RequestMethod.GET)
    public JsonResult sendSMS(@RequestParam(value = "phone") String phone) throws Exception {
        log.info("验证码请求手机号：{}", phone);
        if (smsService.sendCode(phone)) {
            // 发送成功
            return JsonResult.getSuccessResult();
        } else {
            // 发送失败
            log.info("手机号：{}验证码发送失败!", phone);
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
     * 生成6位随机用户编号
     * @return
     */
    private String codeGenerate() {
        Random random = new Random();
        int code = random.nextInt(10) % 3;
        return nickName[code];
    }

}
