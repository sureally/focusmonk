package com.netease.focusmonk.swj.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @ClassName com.netease.focusmonk.swj.service.StudyTogetherTest
 * @Desciption
 * @Author Shu WJ
 * @DateTime 2019-05-08 16:03
 * @Version 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudyTogetherTest {
    @Test
    public void otherTest() {
        Date now = new Date();
        System.out.println(now.getTime());
    }
}
