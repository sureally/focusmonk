package com.netease.focusmonk.validator;

import com.netease.focusmonk.annotation.CheckDurationTime;
import com.netease.focusmonk.model.TaskDetail;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

/**
 * @ClassName CheckDurationTimeValidator
 * @Author konghaifeng
 * @Date 2019/5/13 15:16
 **/
@Slf4j
public class CheckDurationTimeValidator implements ConstraintValidator<CheckDurationTime, Object> {

    @Override
    public void initialize(CheckDurationTime constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        TaskDetail taskDetail = (TaskDetail) o;

        Date startTime = taskDetail.getStartTime();
        Date endTime = taskDetail.getEndTime();

        if (endTime.before(startTime)) {
            log.error("=======StartTime:{}========", startTime.toString());
            log.error("=======EndTime:{}========", endTime.toString());
            return false;
        }

        int planTime = taskDetail.getPlanTime();
        int durationTime = taskDetail.getDurationTime();
        int taskState = taskDetail.getTaskState();

        if ((planTime > durationTime && taskState == 1) || (planTime <= durationTime && taskState == 0)) {
            log.error("=======planTime:{}========", planTime);
            log.error("=======durationTime:{}========", durationTime);
            log.error("=======taskState:{}========", taskState);
            return false;
        }

        long difference = (endTime.getTime() - startTime.getTime()) / 1000 - durationTime;

        log.info("=======StartTime:{}========", startTime.toString());
        log.info("=======EndTime:{}========", endTime.toString());
        log.info("=======durationTime:{}========", durationTime);
        log.info("=======difference:{}========", difference);
        return Math.abs(difference) <= 60;
    }
}
