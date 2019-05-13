package com.netease.focusmonk.annotation;

import com.netease.focusmonk.validator.CheckDurationTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Constraint(validatedBy = CheckDurationTimeValidator.class)
public @interface CheckDurationTime {

    String message() default "学习时间区间与持续学习时间不匹配";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
