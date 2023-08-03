package com.easyback.framework.valid;

import cn.hutool.core.lang.Validator;
import com.easyback.framework.annotation.EmailCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: zhuangqingdian
 * @Date:2023/4/18
 */
public class EmailValidator implements ConstraintValidator<EmailCheck, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return Validator.isEmail(email);
    }
}
