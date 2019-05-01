package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.UserMapper;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hejiecheng
 * @Date 2019-05-01
 */
@Service
public class UserServiceImpl {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 更新用户的头像
     * @param image
     * @param userId
     * @return
     */
    public boolean updateUserImage(String image, String userId) {
        User user = new User();
        user.setId(Integer.valueOf(userId));
        user.setImage(image);
        if (userMapper.updateByPrimaryKeySelective(user) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 更新用户昵称
     * @param username
     * @param userId
     * @return
     */
    public boolean updateUserUsername(String username, String userId) {
        User user = new User();
        user.setId(Integer.valueOf(userId));
        user.setUsername(username);
        if (userMapper.updateByPrimaryKeySelective(user) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    public User getUserInfo(String userId) {
        return userMapper.selectByPrimaryKey(Integer.valueOf(userId));
    }

    // Start Write By KHF.
    public int  accumulateBookNum(Integer userId, Integer bookNum) {
        return userMapper.updateBookNum(userId, bookNum);
    }

    public int setDefaultTaskAndPlanTime(Integer userId, String task, Integer planTime) {
        return userMapper.updateDefaultTaskAndPlanTime(userId, task, planTime);
    }
    //End Write By KHF.
}
