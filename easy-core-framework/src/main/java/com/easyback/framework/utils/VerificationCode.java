package com.easyback.framework.utils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuangqingdian
 */
public class VerificationCode implements Serializable {

    private static final long serialVersionUID = -6193630436318894856L;

    private String code;
    private String phone;
    private Date expireTime;
    private Date createTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
