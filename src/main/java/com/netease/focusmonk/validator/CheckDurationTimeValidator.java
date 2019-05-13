package com.netease.focusmonk.validator;

import com.netease.focusmonk.annotation.CheckDurationTime;
import com.netease.focusmonk.model.TaskDetail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

/**
 * @ClassName CheckDurationTimeValidator
 * @Author konghaifeng
 * @Date 2019/5/13 15:16
 **/
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
            return false;
        }

        int durationTime = taskDetail.getDurationTime();

        long difference = (endTime.getTime() - startTime.getTime()) / 1000 - durationTime;

        return Math.abs(difference) <= 60;
    }
}
