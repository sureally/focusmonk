package com.netease.focusmonk.controller;

import com.github.pagehelper.PageInfo;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.model.Summary;
import com.netease.focusmonk.model.TaskDetail;
import com.netease.focusmonk.service.SummaryServiceImpl;
import com.netease.focusmonk.service.TaskDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zhenghang
 * @Date 2019-04-29
 */
@Controller
@RequestMapping("/personCenter")
public class PersonCenterController {

    @Autowired
    private TaskDetailServiceImpl taskDetailService;

    @Autowired
    private SummaryServiceImpl summaryService;

    /**
     * 时间详情查询接口
     * @param request
     * @param summaryId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/showTasksDetails",method = RequestMethod.GET)
    public JsonResult showTasks(HttpServletRequest request,int summaryId) throws Exception{
        List<TaskDetail> taskDetailList=taskDetailService.getTaskDetail(1);
        JsonResult jsonResult = new JsonResult(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),taskDetailList);
        return jsonResult;
    }

    /**
     * 时间历程查询
     * @param request
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/showDayTasks/{pageNum}/{pageSize}" ,method = RequestMethod.GET)
    public JsonResult showTasksByDay(HttpServletRequest request,int userId,@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) throws Exception{
        PageInfo<Summary> pageInfo = summaryService.getDayTaskByUserId(userId,pageNum,pageSize);
        JsonResult jsonResult = new JsonResult(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),pageInfo);
        return jsonResult;
    }

}
