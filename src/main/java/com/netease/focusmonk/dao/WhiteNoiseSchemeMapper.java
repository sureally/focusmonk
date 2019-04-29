package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.WhiteNoiseScheme;

public interface WhiteNoiseSchemeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WhiteNoiseScheme record);

    int insertSelective(WhiteNoiseScheme record);

    WhiteNoiseScheme selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WhiteNoiseScheme record);

    int updateByPrimaryKey(WhiteNoiseScheme record);
}