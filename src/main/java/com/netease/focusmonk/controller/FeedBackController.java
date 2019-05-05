package com.netease.focusmonk.controller;

import com.alibaba.fastjson.JSONObject;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.service.FeedBackServiceImpl;
import com.netease.focusmonk.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hejiecheng
 * @Date 2019-05-05
 */
@RestController
@RequestMapping("/FeedBack")
public class FeedBackController {

    private final FeedBackServiceImpl feedBackService;

    @Autowired
    public FeedBackController(FeedBackServiceImpl feedBackService) {
        this.feedBackService = feedBackService;
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public JsonResult feedBackInfo(@RequestParam(value = "info") String info,
                                   @RequestParam(value = "jwt") String jwt) throws Exception {
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }
        if (info == null || info.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.PARAM_ERROR);
        }
        if (feedBackService.addFeedBackInfo(userId, info)) {
            return JsonResult.getSuccessResult();
        } else {
            return JsonResult.getErrorResult();
        }
    }

}
