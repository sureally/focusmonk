package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.TaskDetailMapper;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.model.Summary;
import com.netease.focusmonk.model.TaskDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaskDetailServiceImpl {

    @Resource
    private TaskDetailMapper taskDetailMapper;

    @Resource
    private SummaryServiceImpl summaryService;

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
    // End Write By KHF.
}
