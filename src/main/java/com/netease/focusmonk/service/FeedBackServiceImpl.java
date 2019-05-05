package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.FeedBackMapper;
import com.netease.focusmonk.model.FeedBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hejiecheng
 * @Date 2019-05-05
 */

@Service
public class FeedBackServiceImpl {

    private final FeedBackMapper feedBackMapper;

    @Autowired
    public FeedBackServiceImpl(FeedBackMapper feedBackMapper) {
        this.feedBackMapper = feedBackMapper;
    }

    public boolean addFeedBackInfo(String userId, String info) {
        FeedBack feedBack = new FeedBack();
        feedBack.setUserId(Integer.valueOf(userId));
        feedBack.setInfo(info);
        if (feedBackMapper.insert(feedBack) == 1) {
            return true;
        } else {
            return false;
        }
    }

}
