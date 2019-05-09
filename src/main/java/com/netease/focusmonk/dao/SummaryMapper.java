package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.Summary;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    Integer getBookNumsBySummaryDay(@Param("userId") int userId,@Param("date") Date date);

    // Start Write By KHF.
    Summary selectTodaySummaryByUserId(@Param("userId") int userId);

    Summary selectSummaryByUserIdAndDay(@Param("startTime") Date startTime, @Param("userId") Integer userId);
    // End

}