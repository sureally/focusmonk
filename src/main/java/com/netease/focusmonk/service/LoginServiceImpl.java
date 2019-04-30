package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.UserMapper;
import com.netease.focusmonk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author hejiecheng
 * @Date 2019-04-30
 */
@Slf4j
@Service
public class LoginServiceImpl {

    private final UserMapper userMapper;

    @Autowired
    public LoginServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 判断该手机号用户的新老
     * @param phone
     * @return
     */
    public User verifyOldOrNew(String phone) {
        User user = userMapper.selectByPhone(phone);
        if (user == null || user.getId() == null) {
            return null;
        }
        return user;
    }

    /**
     * 新增一个用户
     * @param phone
     * @return
     */
    public int addNewUser(String phone) {
        return userMapper.addUser(phone);
    }

}
