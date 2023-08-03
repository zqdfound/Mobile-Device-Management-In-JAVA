package com.easyback.framework.annotation;

import com.easyback.framework.valid.IdCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author: zhuangqingdian
 * @Date:2023/4/18
 */
@Constraint(validatedBy = IdCardValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdCardCheck {

    String message() default "身份证格式不正确";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
