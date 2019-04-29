package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.Summary;

import java.util.List;

public interface SummaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Summary record);

    int insertSelective(Summary record);

    Summary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Summary record);

    int updateByPrimaryKey(Summary record);

    List<Summary> selectByUserId(Integer userId);
}