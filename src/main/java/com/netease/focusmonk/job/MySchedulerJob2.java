package com.netease.focusmonk.job;

import com.netease.focusmonk.service.RedisServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author hejiecheng
 * @Date 2019-05-09
 */
public class MySchedulerJob2 implements Job {

    @Autowired
    private RedisServiceImpl redisService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int l = 123;
        System.out.println("SchedulerJob2 event number: "+l);
    }
}
