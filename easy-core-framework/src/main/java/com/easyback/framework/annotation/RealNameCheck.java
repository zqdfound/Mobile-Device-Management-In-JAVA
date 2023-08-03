package com.easyback.framework.annotation;

import com.easyback.framework.valid.RealNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 姓名校验
 * @Author: zhuangqingdian
 * @Date:2023/4/18
 */
@Constraint(validatedBy = RealNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RealNameCheck {

    String message() default "姓名格式不正确";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
