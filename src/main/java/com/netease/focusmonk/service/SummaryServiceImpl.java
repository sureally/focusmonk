package com.netease.focusmonk.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netease.focusmonk.dao.SummaryMapper;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.model.Summary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SummaryServiceImpl {

    @Resource
    private SummaryMapper summaryMapper;

    /**
     * edit by zhenghang
     *分页查询时间学习历程
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Summary> getDayTaskByUserId(int userId, int pageNum, int pageSize) {
        List<Summary> listDayTask = summaryMapper.selectDayTaskByUserId(userId);
        PageHelper.startPage(pageNum, pageSize);
        List<Summary> dayTasks = summaryMapper.selectByUserId(userId);
        for (int i = 0; i < dayTasks.size(); i++) {
            dayTasks.get(i).setTaskDetailsList(listDayTask.get(i + (pageNum - 1) * pageSize).getTaskDetailsList());
        }
        PageInfo<Summary> pageInfo = new PageInfo<Summary>(dayTasks);
        return pageInfo;
    }

    // KHF Start

    //添加一条汇总记录
    @Transactional
    public int addSummary(Summary summary) {

        summaryMapper.insert(summary);

        return summary.getId();
    }

    //查询用户当天的汇总（Summary）记录
    public Summary getTodaySummaryByUserId(int userId) {
        return summaryMapper.selectTodaySummaryByUserId(userId);
    }

    //累计经书卷数和学习时长
    public void accumulateBookAndTime(int summaryId, Integer bookNum, Integer durationTime) throws GeneralException {

        Summary summary = summaryMapper.selectByPrimaryKey(summaryId);

        if (summary == null) {
            throw new GeneralException("个人任务汇总信息表数据异常");
        }

        summary.setSumTime(summary.getSumTime() + durationTime);
        summary.setSumBook(summary.getSumBook() + bookNum);

        summaryMapper.updateByPrimaryKeySelective(summary);
    }

    //根据前端请求数据中的学习起始时间、用户ID，获取summary对象
    public Summary getSummaryByUserIdAndDay(Date startTime, Integer userId) throws GeneralException {
        return summaryMapper.selectSummaryByUserIdAndDay(startTime, userId);
    }
    // KFH End
}
