package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.WhiteNoiseSchemeDetail;

public interface WhiteNoiseSchemeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WhiteNoiseSchemeDetail record);

    int insertSelective(WhiteNoiseSchemeDetail record);

    WhiteNoiseSchemeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WhiteNoiseSchemeDetail record);

    int updateByPrimaryKey(WhiteNoiseSchemeDetail record);
}