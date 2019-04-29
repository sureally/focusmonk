package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.Summary;

public interface SummaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Summary record);

    int insertSelective(Summary record);

    Summary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Summary record);

    int updateByPrimaryKey(Summary record);
}