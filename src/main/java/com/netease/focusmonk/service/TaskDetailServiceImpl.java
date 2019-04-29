package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.TaskDetailMapper;
import com.netease.focusmonk.model.TaskDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaskDetailServiceImpl {

    @Resource
    private TaskDetailMapper taskDetailMapper;

    public List<TaskDetail> getTaskDetail(int summaryId) {
        List<TaskDetail> taskDetails = taskDetailMapper.selectBySummaryId(summaryId);
        return taskDetails;
    }
}
