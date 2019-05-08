package com.netease.focusmonk.swj.service;

import com.netease.focusmonk.service.StudyTogetherServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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
    @Resource
    private StudyTogetherServiceImpl studyTogetherService;

    @Test
    public void otherTest() {
        String jwt = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJ1c2VySWRcIjo1fSIsImp0aSI6Ijc2ZDZlNTY5LWVhNDctNGFkOC04MDdjLWJhZTgzOGQyYTRjNiIsImlhdCI6MTU1NzExMDk1OCwiZXhwIjoxNTU3MzcwMTU4fQ.IKdTtn5ssVnbHXKrZ6JhnCny-J5pFhRuVdv2PvMyRv8";

    }
}
