package com.netease.focusmonk.controller;

import com.alibaba.fastjson.JSONObject;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.RedisConstant;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.service.RedisServiceImpl;
import com.netease.focusmonk.service.RoomServiceImpl;
import com.netease.focusmonk.utils.JWTUtil;
import com.netease.focusmonk.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * @author hejiecheng
 * @Date 2019-05-08
 */
@Slf4j
@RestController
@RequestMapping("/RoomController")
public class RoomController {

    private final static int ROOM_NAME_LENGTH = 15;
    private final static int ROOM_INTRODUCE_LENGTH = 40;

    private final RoomServiceImpl roomService;

    @Resource
    private RedisServiceImpl redisService;

    @Autowired
    public RoomController(RoomServiceImpl roomService) {
        this.roomService = roomService;
    }

    /**
     * 创建一个房间
     * @param jwt
     * @param roomName
     * @param roomIntroduce
     * @return
     */
    @RequestMapping(value = "/addRoom", method = RequestMethod.POST)
    public JsonResult addRoom(@RequestParam(value = "jwt") String jwt,
                              @RequestParam(value = "roomName") String roomName,
                              @RequestParam(value = "roomIntroduce") String roomIntroduce) {
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }

        // 参数校验
        if (roomName == null || roomIntroduce == null || roomName.isEmpty() || roomIntroduce.isEmpty()) {
            log.info("创建房间参数错误");
            return JsonResult.getCustomResult(ResultCode.PARAM_ERROR);
        }
        if (roomName.length() > ROOM_NAME_LENGTH || roomIntroduce.length() > ROOM_INTRODUCE_LENGTH) {
            log.info("创建房间参数长度超过阈值");
            return JsonResult.getCustomResult(ResultCode.PARAM_ERROR);
        }

        // 创建并初始化房间
        int roomId = roomService.initRoom(roomName, roomIntroduce, userId);

        // 判断结果
        if (roomId == -1) {
            log.info("房间初始化失败!");
            return JsonResult.getErrorResult();
        }

        return JsonResult.getSuccessResult();
    }


    @RequestMapping(value = "/untiedRoom", method = RequestMethod.POST)
    public JsonResult untiedRoom(@RequestParam(value = "jwt") String jwt,
                                 @RequestParam(value = "roomId") String roomId) {
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }

        if (roomService.untiedRoom(roomId, userId)) {
            return JsonResult.getSuccessResult();
        } else {
            log.info("解绑房间失败：userId-{}, roomId-{}", userId, roomId);
            return JsonResult.getErrorResult();
        }
    }

    @GetMapping("/enter")
    public JsonResult enter(@RequestParam(value = "jwt") String jwt,
                                @RequestParam(value = "roomId") String roomId) {

        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }

        if (roomId == null || StringUtils.isBlank(roomId)) {
            return JsonResult.getCustomResult(ResultCode.REQUEST_PARAMETER_EXCEPTION);
        }

        try {
            return roomService.enterRoom(userId, roomId);
        } catch (IllegalAccessException e) {
            log.error("用户信息存入Redis hash失败", e);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return JsonResult.getCustomResult(ResultCode.USER_INFO_PUT_REDIS_HASH_ERROR);
    }

    @GetMapping("/exit")
    public JsonResult exitRoom() {
        //TODO
        return null;
    }
}
