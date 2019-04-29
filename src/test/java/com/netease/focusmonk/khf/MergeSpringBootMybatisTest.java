package com.netease.focusmonk.khf;

import com.netease.focusmonk.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MergeSpringBootMybatisTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testMerge() {
        userMapper.selectByPrimaryKey(1);
    }
}
