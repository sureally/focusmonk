package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.Summary;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface SummaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Summary record);

    int insertSelective(Summary record);

    Summary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Summary record);

    int updateByPrimaryKey(Summary record);

    //edit by zhenghang
    List<Summary> selectByUserId(Integer userId);

    List<Summary> selectDayTaskByUserId(Integer userId);

    // Start Write By KHF.
    Summary selectTodaySummaryByUserId(int userId);

    Summary selectSummaryByUserIdAndDay(@Param("startTime") Date startTime, Integer userId);
    // End
}