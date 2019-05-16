package com.netease.focusmonk.controller;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.service.FeedBackServiceImpl;
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

    /**
     * 反馈意见接口
     * @param info
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public JsonResult feedBackInfo(@RequestParam(value = "info") String info,
                                   @RequestParam(value = "userId") String userId) throws Exception {
        if (info == null || info.trim().isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.PARAM_ERROR);
        }
        /**
         * 反馈意见长度限制
         */
        info = info.trim();
        info = info.length() > 200 ? info.substring(0,200) : info;
        if (feedBackService.addFeedBackInfo(userId, info)) {
            return JsonResult.getSuccessResult();
        } else {
            return JsonResult.getErrorResult();
        }
    }

}
