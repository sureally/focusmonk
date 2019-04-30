package com.netease.focusmonk.controller;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.model.WhiteNoiseScheme;
import com.netease.focusmonk.service.WhiteNoiseSchemeDetailServiceImpl;
import com.netease.focusmonk.service.WhiteNoiseSchemeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName com.netease.focusmonk.controller.WhiteNoiseSchemeControlle
 * @Desciption
 * @Author Shu WJ
 * @DateTime 2019-04-29 21:25
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/WhiteNoiseSchemeController")
public class WhiteNoiseSchemeController {
    @Autowired
    private WhiteNoiseSchemeDetailServiceImpl whiteNoiseSchemeDetailService;

    @Autowired
    private WhiteNoiseSchemeServiceImpl whiteNoiseSchemeService;

    /**
     * 新增一个方案，并返回新增方案的id
     * @param volumes
     * @param elements
     * @return schemeId
     */
    @RequestMapping(value = "/addOneScheme", method = RequestMethod.POST)
    public JsonResult addOneScheme(@Valid WhiteNoiseScheme wns,
                                   @RequestParam("volumes") int[] volumes,
                                   @RequestParam("elements") int[] elements) {

        int newSchemeId = 0;
        try {
            newSchemeId = whiteNoiseSchemeService.addOneScheme(wns, volumes, elements);
        } catch (InterruptedException e) {
            return JsonResult.getErrorResult(e.getMessage());
        }
        return JsonResult.getSuccessResult(newSchemeId);
    }

    /**
     * 删除一个方案
     * @param schemeId
     * @return
     */
    @RequestMapping(value = "/deleteOneScheme", method = RequestMethod.DELETE)
    public JsonResult deleteOneScheme(@RequestParam("schemeId") int schemeId) {
        whiteNoiseSchemeService.deleteOneScheme(schemeId);
        return JsonResult.getSuccessResult();
    }

    /**
     * 更新一个方案，逻辑是删除该方案的所有声音元素详情，然后新建所有元素详情
     * @param volumes
     * @param elements
     * @return
     */
    @RequestMapping(value = "/updateOneScheme", method = RequestMethod.POST)
    public JsonResult updateOneScheme(WhiteNoiseScheme wns,
            @RequestParam("volumes") int[] volumes,
            @RequestParam("elements") int[] elements) {
        try {
            whiteNoiseSchemeService.updateOneScheme(wns, volumes, elements);
        } catch (IllegalAccessException e) {
            JsonResult.getErrorResult(e.getMessage());
        }
        return JsonResult.getSuccessResult();
    }

    /**
     * 通过白噪声方案Id获取该方案的所有信息。
     * @param schemeId
     * @return
     */
    @RequestMapping(value = "/selectOneScheme", method = RequestMethod.GET)
    public JsonResult selectOneScheme(@RequestParam("schemeId") int schemeId) {
        List<Object> schemeDetail = whiteNoiseSchemeService.getOneScheme(schemeId);
        return JsonResult.getSuccessResult(schemeDetail);
    }

    /**
     * 根据用户Id获取该用户所有白噪声方案及其信息详情。
     * @param userId
     * @return
     */
    @RequestMapping(value = "/selectAllScheme", method = RequestMethod.GET)
    public JsonResult selectAllScheme(@RequestParam("userId") int userId) {
        List<List<Object>> schemeDetails = whiteNoiseSchemeService.selectAllSchemeByUserId(userId);
        return JsonResult.getSuccessResult(schemeDetails);
    }

}

