package com.easyback.framework.valid;

import cn.hutool.core.lang.Validator;
import com.easyback.framework.annotation.PhoneCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: zhuangqingdian
 * @Date:2023/4/18
 */
public class PhoneValidator implements ConstraintValidator<PhoneCheck, String> {
    @Override
    public boolean isValid(String phoneNum, ConstraintValidatorContext context) {
        return Validator.isMobile(phoneNum);
    }
}
