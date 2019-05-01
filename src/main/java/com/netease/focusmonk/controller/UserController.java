package com.netease.focusmonk.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.model.User;
import com.netease.focusmonk.service.UserServiceImpl;
import com.netease.focusmonk.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hejiecheng
 * @Date 2019-05-01
 */
@Slf4j
@RestController
@RequestMapping("/User")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * 更新用户头像
     * @param image
     * @param jwt
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public JsonResult changeImage(@RequestParam(value = "image") String image,
                                  @RequestParam(value = "jwt") String jwt) throws Exception {
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }
        log.info("userId:{} ,image:{}", userId, image);
        boolean result = userService.updateUserImage(image, userId);
        if (result) {
            return JsonResult.getSuccessResult();
        } else {
            return JsonResult.getErrorResult();
        }
    }

    /**
     * 更新用户名
     * @param username
     * @param jwt
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/username", method = RequestMethod.POST)
    public JsonResult changeUsername(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "jwt") String jwt) throws Exception {
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }
        boolean result = userService.updateUserUsername(username, userId);
        if (result) {
            return JsonResult.getSuccessResult();
        } else {
            return JsonResult.getErrorResult();
        }
    }

    /**
     * 首页获取用户信息
     * @param jwt
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/indexInfo", method = RequestMethod.GET)
    public JsonResult getIndexUserInfo(@RequestParam(value = "jwt") String jwt) throws Exception {
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }
        User user = userService.getUserInfo(userId);
        if (user == null) {
            return JsonResult.getErrorResult();
        } else {
            user.setId(null);
            return JsonResult.getSuccessResult(user);
        }
    }

}
