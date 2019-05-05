package com.netease.focusmonk.controller;

import com.alibaba.fastjson.JSONObject;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.model.TaskDetail;
import com.netease.focusmonk.service.TaskDetailServiceImpl;
import com.netease.focusmonk.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@Controller
public class StudyAloneController {

    @Resource
    private TaskDetailServiceImpl taskDetailService;
    
    @PostMapping("/addTaskDetail")
    @ResponseBody
    public JsonResult addTaskDetail(@Valid TaskDetail taskDetail, @RequestParam(value = "jwt") @NotBlank(message = "jwt 不能为空") String jwt) {

        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");

        if (!StringUtils.isNotBlank(userId)) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }

        try {
            taskDetail.setUserId(Integer.valueOf(userId));
            taskDetailService.generateTaskDetailWithDay(taskDetail);
        } catch (GeneralException e) {
            log.error("error info : {}","独处任务生产异常");
            return JsonResult.getErrorResult();
        } catch (NumberFormatException nfe) {
            log.error("error info : {}","用户ID存在异常");
            return JsonResult.getErrorResult();
        }

        return JsonResult.getSuccessResult();
    }
}