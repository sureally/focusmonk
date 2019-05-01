package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByPhone(String phone);

    int addUser(User user);

    int updateBookNum(Integer userId, Integer bookNum);

    int updateDefaultTaskAndPlanTime(Integer userId, String task, Integer planTime);
}