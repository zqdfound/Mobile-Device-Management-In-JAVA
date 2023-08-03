package com.easyback.framework.valid;

import com.easyback.framework.annotation.RealNameCheck;
import com.easyback.framework.utils.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: zhuangqingdian
 * @Date:2023/4/18
 */
public class RealNameValidator implements ConstraintValidator<RealNameCheck, String> {
    @Override
    public boolean isValid(String realName, ConstraintValidatorContext context) {
        return RegexUtils.isRealName(realName);
    }
}
