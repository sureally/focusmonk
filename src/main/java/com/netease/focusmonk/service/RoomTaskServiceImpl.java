package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.RoomTaskMapper;
import com.netease.focusmonk.model.RoomTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName com.netease.focusmonk.service.RoomTaseServiceImpl
 * @Desciption
 * @Author Shu WJ
 * @DateTime 2019-05-09 11:54
 * @Version 1.0
 **/
@Service
public class RoomTaskServiceImpl {

    @Autowired
    private RoomTaskMapper roomTaskMapper;

    /**
     * 插入一条roomTask数据。
     * @param roomTask
     */
    public void addOneRoomTask(RoomTask roomTask) {
        roomTaskMapper.insert(roomTask);
    }
}
