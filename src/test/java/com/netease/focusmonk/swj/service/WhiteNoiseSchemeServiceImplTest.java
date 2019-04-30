package com.netease.focusmonk.swj.service;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.service.WhiteNoiseSchemeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName com.netease.focusmonk.swj.service.WhiteNoiseSchemeServiceImplTest
 * @Desciption
 * @Author Shu WJ
 * @DateTime 2019-04-29 17:47
 * @Version 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class WhiteNoiseSchemeServiceImplTest {

    @Resource
    private WhiteNoiseSchemeServiceImpl whiteNoiseSchemeService;

    @Test
    public void otherTest() {
        List<String> list = new ArrayList<>();
        list.add("abs");
        list.add(null);
        for (String str : list) {
            System.out.println(str.length());
        }
        System.out.println(JsonResult.getErrorResult(list));
    }

}
