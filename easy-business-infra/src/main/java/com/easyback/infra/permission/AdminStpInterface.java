package com.easyback.infra.permission;

import cn.dev33.satoken.stp.StpInterface;
import com.easyback.infra.service.PermissionService;
import com.easyback.infra.service.RoleService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class AdminStpInterface implements StpInterface {

    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;

    /**
     * 查询用户权限
     * @param o
     * @param s
     * @return
     */
    @Override
    public List<String> getPermissionList(Object o, String s) {
        Integer userId = Integer.valueOf((String)o);
        return permissionService.getListByUserId(userId);
    }

    /**
     * 设置角色
     * @param o
     * @param s
     * @return
     */
    @Override
    public List<String> getRoleList(Object o, String s) {
        Integer userId = Integer.valueOf((String)o);
        return roleService.getListByUserId(userId);
    }
}
