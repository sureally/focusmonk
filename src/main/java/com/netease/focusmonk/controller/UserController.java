package com.netease.focusmonk.controller;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.model.User;
import com.netease.focusmonk.service.UserServiceImpl;
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
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public JsonResult changeImage(@RequestParam(value = "image") String image,
                                  @RequestParam(value = "userId") String userId) throws Exception {
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
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/username", method = RequestMethod.POST)
    public JsonResult changeUsername(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "userId") String userId) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.USERNAME_ERROR);
        }
        username = username.trim();
        boolean result = userService.updateUserUsername(username, userId);
        if (result) {
            return JsonResult.getSuccessResult();
        } else {
            log.info("更新用户：{}的用户名：{}发生错误！", userId, username);
            return JsonResult.getErrorResult();
        }
    }

    /**
     * 首页获取用户信息
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/indexInfo", method = RequestMethod.GET)
    public JsonResult getIndexUserInfo(@RequestParam(value = "userId") String userId) throws Exception {
        User user = userService.getUserInfo(userId);
        if (user == null) {
            return JsonResult.getErrorResult();
        } else {
            return JsonResult.getSuccessResult(user);
        }
    }

}
