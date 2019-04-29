package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.WhiteNoiseElement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WhiteNoiseElementMapper {
    String TABLE_NAME = " white_noise_element ";
    String SELECT_FIELDS = " id, name, url, image, icon ";

    int deleteByPrimaryKey(Integer id);

    int insert(WhiteNoiseElement record);

    int insertSelective(WhiteNoiseElement record);

    WhiteNoiseElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WhiteNoiseElement record);

    int updateByPrimaryKey(WhiteNoiseElement record);

    @Select({"Select ", SELECT_FIELDS, " from ", TABLE_NAME})
    List<WhiteNoiseElement> listAll();
}