package com.easyback.framework.valid;

import cn.hutool.core.lang.Validator;
import com.easyback.framework.annotation.IdCardCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: zhuangqingdian
 * @Date:2023/4/18
 */
public class IdCardValidator implements ConstraintValidator<IdCardCheck, String> {
    @Override
    public boolean isValid(String idCard, ConstraintValidatorContext context) {
        return Validator.isCitizenId(idCard);
    }
}
