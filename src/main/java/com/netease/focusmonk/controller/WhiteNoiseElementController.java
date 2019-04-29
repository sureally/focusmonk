package com.netease.focusmonk.controller;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.model.WhiteNoiseElement;
import com.netease.focusmonk.service.WhiteNoiseElementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName com.netease.focusmonk.controller.WhiteNoiseElementController
 * @Desciption 白噪声
 * @Author Shu WJ
 * @DateTime 2019-04-29 16:37
 * @Version 1.0
 **/
@RestController
@RequestMapping("/WhiteNoiseElementController")
public class WhiteNoiseElementController {
    @Autowired
    private WhiteNoiseElementServiceImpl whiteNoiseElementServiceImpl;

    /**
     * 返回所有白噪声元素
     * @return
     */
    @RequestMapping(value = "/all", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonResult allWhiteNoiseElement() {
        List<WhiteNoiseElement> wnes = whiteNoiseElementServiceImpl.listAll();
        return new JsonResult(ResultCode.SUCCESS, wnes);
    }
}
