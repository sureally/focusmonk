package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByPhone(String phone);

    int addUser(User user);

    int updateBookNum(@Param("userId") Integer userId, @Param("bookNum") Integer bookNum);

    int updateDefaultTaskAndPlanTime(@Param("userId") Integer userId, @Param("task") String task, @Param("planTime") Integer planTime);

    int updateStudyTime(@Param("userId") Integer userId, @Param("durationTime") Integer durationTime);
}