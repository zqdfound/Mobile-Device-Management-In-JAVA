package com.easyback.infra.context;

import cn.dev33.satoken.stp.StpUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 分销管理员登录信息上下文
 * @Author: zhuangqingdian
 * @Date:2023/3/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminContext {

    /**
     * 用户信息 key
     */
    public static final String KEY = "admin-context";
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 最后登录日期
     */
    private LocalDateTime loginDate;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色
     */
    private String roleName;

    public static AdminContext getUser(){
        return (AdminContext) StpUtil.getSession().get(KEY);
    }

    public static Integer getUserId(){
        return getUser().getId();
    }
}
