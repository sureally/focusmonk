package com.netease.focusmonk.scheduler;

import com.netease.focusmonk.job.MySchedulerJob2;
import com.netease.focusmonk.utils.SpringUtil;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author hejiecheng
 * @Date 2019-05-09
 * Scheduler
 */
@Component
public class MyScheduler {


    public void schedulerJob() throws SchedulerException {
        ApplicationContext annotationContext = SpringUtil.getApplicationContext();
        StdScheduler stdScheduler = (StdScheduler) annotationContext.getBean("mySchedulerFactoryBean");//获得上面创建的bean
        Scheduler myScheduler =stdScheduler;
        startScheduler2(myScheduler);
        myScheduler.start();
    }

    /**
     * 定时任务样例
     * @param scheduler
     * @throws SchedulerException
     */
    public void startScheduler2(Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(MySchedulerJob2.class).withIdentity("job2", "jobGroup2").build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0 1 * ?");
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger2", "triggerGroup2")
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

}
