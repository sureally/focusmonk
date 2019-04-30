package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.WhiteNoiseSchemeDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WhiteNoiseSchemeDetailMapper {
    String TABLE_NAME = " white_noise_scheme_detail ";
    String SELECT_FIELDS = " id, scheme_id, element_id, volume ";

    int deleteByPrimaryKey(Integer id);

    int insert(WhiteNoiseSchemeDetail record);

    int insertSelective(WhiteNoiseSchemeDetail record);

    WhiteNoiseSchemeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WhiteNoiseSchemeDetail record);

    int updateByPrimaryKey(WhiteNoiseSchemeDetail record);

    @Select({" select ", SELECT_FIELDS, " from ", TABLE_NAME, " where scheme_id = #{schemeId} "})
    List<WhiteNoiseSchemeDetail> selectBySchemeId(Integer schemeId);

    @Delete({" delete from ", TABLE_NAME, " where scheme_id = #{schemeId}"})
    int deleteBySchemeId(Integer schemeId);
}