package com.netease.focusmonk.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.model.Summary;
import com.netease.focusmonk.model.TaskDetail;
import com.netease.focusmonk.service.SummaryServiceImpl;
import com.netease.focusmonk.service.TaskDetailServiceImpl;
import com.netease.focusmonk.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhenghang
 * @Date 2019-04-29
 */
@Slf4j
@Controller
@RequestMapping("/personCenter")
public class PersonCenterController {

    @Autowired
    private TaskDetailServiceImpl taskDetailService;

    @Autowired
    private SummaryServiceImpl summaryService;

    /**
     * 每日详情查询接口
     * @param request
     * @param summaryId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/showTasksDetails",method = RequestMethod.GET)
    public JsonResult showTasks(HttpServletRequest request,@RequestParam("summaryId") int summaryId,@RequestParam(value = "jwt") String jwt) throws Exception{
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }
        HashMap<String,Integer> hashMap = new HashMap<>();
        hashMap.put("summaryId",summaryId);
        hashMap.put("userId",Integer.valueOf(userId));
        List<TaskDetail> taskDetailList=taskDetailService.getTaskDetail(hashMap);
        if(taskDetailList.size() == 0){
            log.warn("未查询到当日学习记录");
            return new JsonResult(ResultCode.PERSON_CENTER_ERROR, "未查询当日学习记录");
        }
        return JsonResult.getSuccessResult(taskDetailList);
    }

    /**
     * 时间历程查询
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/showDayTasks" ,method = RequestMethod.GET)
    public JsonResult showTasksByDay(HttpServletRequest request,@RequestParam(value = "jwt") String jwt,@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) throws Exception{
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }
        PageInfo<Summary> pageInfo = summaryService.getDayTaskByUserId(Integer.valueOf(userId),pageNum,pageSize);
        if(pageInfo == null){
            log.warn("用户尚未有学习记录");
            return new JsonResult(ResultCode.PERSON_CENTER_ERROR, "用户尚未有学习记录");
        }
        return JsonResult.getSuccessResult(pageInfo);
    }
}
