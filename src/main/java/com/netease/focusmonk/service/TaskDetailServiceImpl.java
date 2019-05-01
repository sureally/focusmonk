package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.TaskDetailMapper;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.model.Summary;
import com.netease.focusmonk.model.TaskDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaskDetailServiceImpl {

    @Resource
    private TaskDetailMapper taskDetailMapper;

    @Resource
    private SummaryServiceImpl summaryService;

    @Resource
    private UserServiceImpl userService;

    public List<TaskDetail> getTaskDetail(Map<String,Integer> map) {
        List<TaskDetail> taskDetails = taskDetailMapper.selectBySummaryIdAndUserId(map);
        return taskDetails;
    }

    // Start Write By KHF.

    // 为用户生成一条任务记录-
    @Transactional
    public void generateTaskDetail(TaskDetail taskDetail) throws GeneralException {

        int summaryId;

        Summary todaySummary = summaryService.getTodaySummaryByUserId(taskDetail.getUserId());
        if (todaySummary == null) {
            todaySummary = new Summary();
            todaySummary.setUserId(taskDetail.getUserId());
            todaySummary.setSumBook(0);
            todaySummary.setSumTime(0);
            todaySummary.setSummaryDay(new Date());
            summaryId = summaryService.addSummary(todaySummary);
        } else {
            summaryId = todaySummary.getId();
        }

        summaryService.accumulateBookAndTime(summaryId, taskDetail.getBookNum(), taskDetail.getDurationTime());

        taskDetail.setSummaryId(summaryId);

        taskDetailMapper.insertSelective(taskDetail);
    }

    @Transactional
    public void generateTaskDetailWithDay(TaskDetail taskDetail) throws GeneralException {

        Date startTime = taskDetail.getStartTime();

        //找出用户指定日期的汇总记录
        Summary specifiedDateSummary = summaryService.getSummaryByUserIdAndDay(startTime, taskDetail.getUserId());

        int summaryId;
        if (specifiedDateSummary == null) {
            specifiedDateSummary = new Summary();
            specifiedDateSummary.setUserId(taskDetail.getUserId());
            specifiedDateSummary.setSumBook(0);
            specifiedDateSummary.setSumTime(0);
            specifiedDateSummary.setSummaryDay(startTime);
            summaryId = summaryService.addSummary(specifiedDateSummary);
        } else {
            summaryId = specifiedDateSummary.getId();
        }

        //保存单条学习任务记录
        taskDetail.setSummaryId(summaryId);
        taskDetailMapper.insertSelective(taskDetail);

        //累加到当天汇总表中
        summaryService.accumulateBookAndTime(summaryId, taskDetail.getBookNum(), taskDetail.getDurationTime());

        //累加到用户总经书卷数中
        int bookNumRows = userService.accumulateBookNum(taskDetail.getUserId(), taskDetail.getBookNum());

        //设置用户默认任务名和学习时长
        int taskAndPlanTimeRows = userService.setDefaultTaskAndPlanTime(taskDetail.getUserId(), taskDetail.getTask(), taskDetail.getPlanTime());
        if (bookNumRows == 0 || taskAndPlanTimeRows == 0) {
            throw new GeneralException("任务记录无对应用户异常");
        }
    }

    // End Write By KHF.
}
