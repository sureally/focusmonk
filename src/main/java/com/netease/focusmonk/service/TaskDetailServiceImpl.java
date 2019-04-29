package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.TaskDetailMapper;
import com.netease.focusmonk.model.TaskDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskDetailServiceImpl {

    @Resource
    private TaskDetailMapper taskDetailMapper;

    public List<TaskDetail> getTaskDetail(Map<String,Integer> map) {
        List<TaskDetail> taskDetails = taskDetailMapper.selectBySummaryIdAndUserId(map);
        return taskDetails;
    }
}
