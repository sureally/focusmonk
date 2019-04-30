package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.WhiteNoiseScheme;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WhiteNoiseSchemeMapper {
    String TABLE_NAME = " white_noise_scheme ";
    String SELECT_FIELDS = " id, user_id, name, speed ";

    int deleteByPrimaryKey(Integer id);

    int insert(WhiteNoiseScheme record);

    int insertSelective(WhiteNoiseScheme record);

    WhiteNoiseScheme selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WhiteNoiseScheme record);

    int updateByPrimaryKey(WhiteNoiseScheme record);

    @Select({" select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id = #{userId} "})
    List<WhiteNoiseScheme> selectByUserId(Integer userId);
}