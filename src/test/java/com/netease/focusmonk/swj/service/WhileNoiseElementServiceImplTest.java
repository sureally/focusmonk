package com.netease.focusmonk.swj.service;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.model.WhiteNoiseElement;
import com.netease.focusmonk.service.WhiteNoiseElementServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName WhileNoiseElementServiceImplTest
 * @Desciption
 * @Author Shu WJ
 * @DateTime 2019-04-29 15:41
 * @Version 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class WhileNoiseElementServiceImplTest {

    @Resource
    private WhiteNoiseElementServiceImpl whiteNoiseElementService;

    @Test
    public void testListAll() {
        List<WhiteNoiseElement> wnes = whiteNoiseElementService.listAll();
        for (WhiteNoiseElement wne : wnes) {
            System.out.println(wne.toString());
        }

        JsonResult jr = new JsonResult(ResultCode.SUCCESS, wnes);
    }
}
