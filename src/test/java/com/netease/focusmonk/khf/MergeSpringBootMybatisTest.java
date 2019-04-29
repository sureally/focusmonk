package com.netease.focusmonk.khf;

import com.netease.focusmonk.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MergeSpringBootMybatisTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testMerge() {

    }
}
