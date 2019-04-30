package com.netease.focusmonk.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netease.focusmonk.dao.SummaryMapper;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.model.Summary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void accumulateBookAndTime(int summaryId, Integer bookNum, Integer durationTime) throws GeneralException {
        Summary summary = summaryMapper.selectByPrimaryKey(summaryId);

        if (summary == null) {
            throw new GeneralException("个人任务汇总信息表数据异常");
        }

        summary.setSumTime(summary.getSumTime() + durationTime);
        summary.setSumBook(summary.getSumBook() + bookNum);

        summaryMapper.updateByPrimaryKeySelective(summary);
    }
    // KFH End
}
