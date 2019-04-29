package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.TaskDetail;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskDetail record);

    int insertSelective(TaskDetail record);

    TaskDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskDetail record);

    int updateByPrimaryKey(TaskDetail record);

    List<TaskDetail> selectBySummaryIdAndUserId(Map<String ,Integer> map);

}