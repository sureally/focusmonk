package com.netease.focusmonk.controller;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.exception.ParamException;
import com.netease.focusmonk.model.RedisTaskDetailAO;
import com.netease.focusmonk.model.TaskDetail;
import com.netease.focusmonk.service.StudyTogetherServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @ClassName com.netease.focusmonk.controller.StudyTogetherController
 * @Desciption 多人房间，学习功能
 * @Author Shu WJ
 * @DateTime 2019-05-08 14:18
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/studyTogetherControl")
public class StudyTogetherController {

    @Autowired
    private StudyTogetherServiceImpl studyTogetherService;

    @RequestMapping(path = "/startStudy", method = RequestMethod.GET)
    public JsonResult startStudy(@RequestParam("jwt") String jwt,
                                 int roomId) throws Exception{
        try {
            studyTogetherService.setValueForStart(jwt, roomId);
        } catch (ParamException pe) {
            log.error("多人学习接口请求参数异常：", pe);
            return new JsonResult(ResultCode.STUDY_TOGETHER_PARAM_ERROR, pe.getMessage());
        } catch (GeneralException ge) {
            log.error("多人学习接口通用异常：", ge);
            return new JsonResult(ResultCode.STUDY_TOGETHER_GENERAL_ERROR, ge.getMessage());
        }
        return JsonResult.getSuccessResult();
    }

    @RequestMapping(path = "/pauseStudy", method = RequestMethod.GET)
    public JsonResult pauseStudy(@RequestParam("jwt") String jwt,
                                 int roomId) throws Exception {
        try {
            studyTogetherService.setValueForPause(jwt, roomId);
        } catch (ParamException pe) {
            log.error("多人学习接口请求参数异常：", pe);
            return new JsonResult(ResultCode.STUDY_TOGETHER_PARAM_ERROR, pe.getMessage());
        } catch (GeneralException ge) {
            log.error("多人学习接口通用异常：", ge);
            return new JsonResult(ResultCode.STUDY_TOGETHER_GENERAL_ERROR, ge.getMessage());
        }
        return JsonResult.getSuccessResult();
    }

    @RequestMapping(path = "/restartStudy", method = RequestMethod.GET)
    public JsonResult restartStudy(@RequestParam("jwt") String jwt,
                                 int roomId) throws Exception {
        try {
            studyTogetherService.setValueForRestart(jwt, roomId);
        } catch (ParamException pe) {
            log.error("多人学习接口请求参数异常：", pe);
            return new JsonResult(ResultCode.STUDY_TOGETHER_PARAM_ERROR, pe.getMessage());
        } catch (GeneralException ge) {
            log.error("多人学习接口通用异常：", ge);
            return new JsonResult(ResultCode.STUDY_TOGETHER_GENERAL_ERROR, ge.getMessage());
        }
        return JsonResult.getSuccessResult();
    }

    @RequestMapping(path = "/finishStudy", method = RequestMethod.GET)
    public JsonResult finishStudy(@Valid RedisTaskDetailAO redisTaskDetailAO,
                                  @RequestParam("jwt") String jwt,
                                  int roomId) throws Exception {
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setPlanTime(redisTaskDetailAO.getPlanTime());
        taskDetail.setType(redisTaskDetailAO.getType());
        taskDetail.setTaskState(redisTaskDetailAO.getTaskState());
        if (redisTaskDetailAO.getTask() != null) {
            taskDetail.setTask(redisTaskDetailAO.getTask());
        } else {
            // 任务默认为空
            taskDetail.setTask("");
        }
        TaskDetail updateTaskDetail = null;
        try {
            updateTaskDetail = studyTogetherService.setValueForFinish(jwt, roomId, taskDetail);
            // 隐藏userId
            updateTaskDetail.setUserId(0);
        } catch (ParamException pe) {
            log.error("多人学习接口请求参数异常：", pe);
            return new JsonResult(ResultCode.STUDY_TOGETHER_PARAM_ERROR, pe.getMessage());
        } catch (GeneralException ge) {
            log.error("多人学习接口通用异常：", ge);
            return new JsonResult(ResultCode.STUDY_TOGETHER_GENERAL_ERROR, ge.getMessage());
        }
        return JsonResult.getSuccessResult(updateTaskDetail);
    }


}
