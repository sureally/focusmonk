package com.netease.focusmonk.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netease.focusmonk.dao.SummaryMapper;
import com.netease.focusmonk.model.Summary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SummaryServiceImpl {

    @Resource
    private SummaryMapper summaryMapper;

    public PageInfo<Summary> getDayTaskByUserId(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Summary> DayTasks = summaryMapper.selectByUserId(userId);
        PageInfo<Summary> pageInfo = new PageInfo<Summary>(DayTasks);
        return pageInfo;
    }
}
