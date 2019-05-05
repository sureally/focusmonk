package com.netease.focusmonk.controller;

import com.alibaba.fastjson.JSONObject;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.exception.ParamException;
import com.netease.focusmonk.model.WhiteNoiseScheme;
import com.netease.focusmonk.service.WhiteNoiseSchemeDetailServiceImpl;
import com.netease.focusmonk.service.WhiteNoiseSchemeServiceImpl;
import com.netease.focusmonk.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName com.netease.focusmonk.controller.WhiteNoiseSchemeController
 * @Desciption 白噪声方案
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
     * @param elementIds
     * @return schemeId
     */
    @RequestMapping(value = "/addOneScheme", method = RequestMethod.POST)
    public JsonResult addOneScheme(@Valid WhiteNoiseScheme wns,
                                   @RequestParam("volume") int[] volumes,
                                   @RequestParam("elementId") int[] elementIds) throws Exception {
        int newSchemeId = 0;
        try {
            newSchemeId = whiteNoiseSchemeService.addOneScheme(wns, volumes, elementIds);
        } catch (ParamException pe) {
            log.error("白噪声方案参数异常: {}", pe);
            return new JsonResult(ResultCode.WHITE_NOISE_ERROR, pe.getMessage());
        }
        return JsonResult.getSuccessResult(newSchemeId);
    }

    /**
     * 删除一个方案
     * @param schemeId
     * @return
     */
    @RequestMapping(value = "/deleteOneScheme", method = RequestMethod.DELETE)
    public JsonResult deleteOneScheme(@RequestParam("schemeId") int schemeId) throws Exception {
        whiteNoiseSchemeService.deleteOneScheme(schemeId);
        return JsonResult.getSuccessResult();
    }

    /**
     * 更新一个方案，逻辑是删除该方案的所有声音元素详情，然后新建所有元素详情
     * @param volumes
     * @param elementIds
     * @return
     */
    @RequestMapping(value = "/updateOneScheme", method = RequestMethod.POST)
    public JsonResult updateOneScheme(@Valid WhiteNoiseScheme wns,
            @RequestParam("volume") int[] volumes,
            @RequestParam("elementId") int[] elementIds) throws Exception {
        if (wns.getId() == null || wns.getId() <= 0) {
            log.error("error info: WhiteNoiseScheme.id 为空");
            return new JsonResult(ResultCode.WHITE_NOISE_ERROR, "WhiteNoiseScheme.id 不能为空");
        }
        try {
            whiteNoiseSchemeService.updateOneScheme(wns, volumes, elementIds);
        } catch (GeneralException ge) {
            log.error("白噪声方案通用异常: {}", ge);
            return new JsonResult(ResultCode.WHITE_NOISE_ERROR, ge.getMessage());
        } catch (ParamException pe) {
            log.error("白噪声方案参数异常: {}", pe);
            return new JsonResult(ResultCode.WHITE_NOISE_ERROR, pe.getMessage());
        }
        return JsonResult.getSuccessResult();
    }

    /**
     * 通过白噪声方案Id获取该方案的所有信息。
     * @param schemeId
     * @return
     */
    @RequestMapping(value = "/selectOneScheme", method = RequestMethod.GET)
    public JsonResult selectOneScheme(@RequestParam("schemeId") int schemeId) throws Exception {
        List<Object> schemeDetail;
        try {
            schemeDetail = whiteNoiseSchemeService.getOneScheme(schemeId);
        } catch (GeneralException ge) {
            log.error("白噪声方案通用异常: {}", ge);
            return new JsonResult(ResultCode.WHITE_NOISE_ERROR, ge.getMessage());
        }
        return JsonResult.getSuccessResult(schemeDetail);
    }

    /**
     * 根据用户Id获取该用户所有白噪声方案及其信息详情。
     * @param jwt
     * @return
     */
    @RequestMapping(value = "/selectAllScheme", method = RequestMethod.GET)
    public JsonResult selectAllScheme(@RequestParam("jwt") String jwt) throws Exception {
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }

        List<List<Object>> schemeDetails;
        try {
            schemeDetails = whiteNoiseSchemeService.selectAllSchemeByUserId(Integer.valueOf(userId));
        } catch (GeneralException ge) {
            log.error("白噪声方案通用异常: {}", ge);
            return new JsonResult(ResultCode.WHITE_NOISE_ERROR, ge.getMessage());
        }

        return JsonResult.getSuccessResult(schemeDetails);
    }

}

