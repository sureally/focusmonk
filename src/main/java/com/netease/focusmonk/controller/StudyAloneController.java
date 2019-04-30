package com.netease.focusmonk.controller;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.model.TaskDetail;
import com.netease.focusmonk.service.TaskDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@Controller
public class StudyAloneController {

    @Resource
    private TaskDetailServiceImpl taskDetailService;
    
    @RequestMapping("/addTaskDetail")
    @ResponseBody
    public JsonResult addTaskDetail(@Valid TaskDetail taskDetail, @RequestParam("token") String token) {

        if (token == null) {
            //TODO
        } else {
            //TODO
            //taskDetail.setUserId(redisService.get(token));
        }

        try {
            taskDetailService.generateTaskDetail(taskDetail);
        } catch (GeneralException e) {
            log.error("error info : {}","独处任务生产异常");
            return JsonResult.getErrorResult();
        }

        return JsonResult.getSuccessResult();
    }
}
