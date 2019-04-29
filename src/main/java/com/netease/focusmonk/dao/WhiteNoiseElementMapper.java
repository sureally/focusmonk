package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.WhiteNoiseElement;

public interface WhiteNoiseElementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WhiteNoiseElement record);

    int insertSelective(WhiteNoiseElement record);

    WhiteNoiseElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WhiteNoiseElement record);

    int updateByPrimaryKey(WhiteNoiseElement record);
}