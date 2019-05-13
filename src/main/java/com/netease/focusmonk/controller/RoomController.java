package com.netease.focusmonk.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.RedisConstant;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.model.Room;
import com.netease.focusmonk.service.RedisServiceImpl;
import com.netease.focusmonk.service.RoomServiceImpl;
import com.netease.focusmonk.utils.JWTUtil;
import com.netease.focusmonk.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final RedisServiceImpl redisService;

    @Autowired
    public RoomController(RoomServiceImpl roomService, RedisServiceImpl redisService) {
        this.roomService = roomService;
        this.redisService = redisService;
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
        log.info("创建房间-{}：{}", roomId, roomName);
        Map<String, String> detail = new HashMap<>();
        detail.put("roomId", String.valueOf(roomId));

        return JsonResult.getSuccessResult(detail);
    }

    /**
     * 获取房间列表
     * @param jwt
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getHomeList", method = RequestMethod.GET)
    public JsonResult getRoomList(@RequestParam(value = "jwt") String jwt,
                                  @RequestParam(value = "pageNum") String pageNum,
                                  @RequestParam(value = "pageSize") String pageSize) throws Exception {
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }

        Integer pageNumInt = null;
        Integer pageSizeInt = null;
        try {
            pageNumInt = Integer.valueOf(pageNum);
            pageSizeInt = Integer.valueOf(pageSize);
        } catch (Exception e) {
            log.info("获取房间列表参数错误：{}-{}", pageNum, pageSize);
            return JsonResult.getCustomResult(ResultCode.PARAM_ERROR);
        }

        PageInfo<Room> roomList = roomService.getRoomList(pageNumInt, pageSizeInt);

        return JsonResult.getSuccessResult(roomList);
    }

    /**
     * 获取用户自己的房间
     * @param jwt
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getUserHomeList", method = RequestMethod.GET)
    public JsonResult getUserRoomList(@RequestParam(value = "jwt") String jwt) throws Exception {
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }

        List<Room> roomList = roomService.getUserRoomList(userId);

        return JsonResult.getSuccessResult(roomList);
    }


    /**
     * 用户和房间进行解绑
     * @param jwt
     * @param roomId
     * @return
     */
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
    public JsonResult enter(HttpServletRequest request,
                            @RequestParam(value = "roomId") String roomId) {

        String userId = (String) request.getAttribute("userId");

        JsonResult validateParam = validate(userId, roomId);

        if (validateParam != null) {
            return validateParam;
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
    public JsonResult exitRoom(HttpServletRequest request,
                               @RequestParam(value = "roomId") String roomId) {

        String userId = (String) request.getAttribute("userId");

        JsonResult validateParam = validate(userId, roomId);

        if (validateParam != null) {
            return validateParam;
        }

        try {
            return roomService.exitRoom(userId, roomId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonResult.getCustomResult(ResultCode.USER_EXIT_ROOM_ERROR);
    }

    private JsonResult validate(String userId, String roomId) {
        if (userId == null || userId.isEmpty()) {
            return JsonResult.getCustomResult(ResultCode.JWT_ERROR);
        }

        if (roomId == null || StringUtils.isBlank(roomId)) {
            return JsonResult.getCustomResult(ResultCode.REQUEST_PARAMETER_EXCEPTION);
        }

        return null;
    }
}
