package com.netease.focusmonk.controller;

import com.netease.focusmonk.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(path = "/startStudy", method = RequestMethod.GET)
    public JsonResult startStudy(@RequestParam("jwt") String jwt,
                                 int roomId){
        return JsonResult.getSuccessResult();
    }

    @RequestMapping(path = "/pauseStudy", method = RequestMethod.GET)
    public JsonResult pauseStudy(@RequestParam("jwt") String jwt,
                                 int roomId){
        return JsonResult.getSuccessResult();
    }

    @RequestMapping(path = "/restartStudy", method = RequestMethod.GET)
    public JsonResult restartStudy(@RequestParam("jwt") String jwt,
                                 int roomId){
        return JsonResult.getSuccessResult();
    }

    @RequestMapping(path = "/finishStudy", method = RequestMethod.GET)
    public JsonResult finishStudy(@RequestParam("jwt") String jwt,
                                 int roomId){
        return JsonResult.getSuccessResult();
    }
}
