package com.easyback.infra.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyback.infra.entity.Admin;
import com.easyback.infra.entity.Permission;
import com.easyback.infra.entity.PermissionRole;
import com.easyback.infra.mapper.AdminMapper;
import com.easyback.infra.mapper.PermissionMapper;
import com.easyback.infra.mapper.PermissionRoleMapper;
import com.easyback.infra.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyback.model.vo.PermissionVO;
import com.easyback.redis.RedisKey;
import com.easyback.redis.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-04
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private PermissionRoleMapper permissionRoleMapper;

    @Override
    public List<String> getListByUserId(Integer userId) {
        List<String> result = new ArrayList<>();
        //1 根据用户查询角色
        Admin Admin = adminMapper.selectById(userId);
        Integer roleId = Admin.getRoleId();
        //2 根据角色查询权限
        LambdaQueryWrapper<PermissionRole> fxrQw = new LambdaQueryWrapper<>();
        fxrQw.eq(PermissionRole::getRoleId, roleId);
        List<PermissionRole> PermissionRoleList = permissionRoleMapper.selectList(fxrQw);
        Set<Integer> permissionIdSet = PermissionRoleList.stream().map(PermissionRole::getPermissionId).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(permissionIdSet)) {
            //3 查找权限
            LambdaQueryWrapper<Permission> fpQw = new LambdaQueryWrapper<>();
            fpQw.in(Permission::getId, permissionIdSet);
            List<Permission> permissionList = list(fpQw);
            result = permissionList.stream().map(Permission::getName).collect(Collectors.toList());
        }
        return result;
    }
    @Override
    public List<String> listAllName() {
        return getAllPermissionList().stream().map(Permission::getName).collect(Collectors.toList());
    }

    private List<Permission> getAllPermissionList() {
        List<Permission> permissionList;
        Object o = redisUtil.get(RedisKey.ALL_PERMISSIONS);
        if (o == null) {
            permissionList = list();
            redisUtil.set(RedisKey.ALL_PERMISSIONS, JSONObject.toJSONString(permissionList), 3600);
        } else {
            permissionList = JSONObject.parseArray(o.toString(), Permission.class);
        }
        return permissionList;
    }
    
    @Override
    public List<PermissionVO> getAllList() {
        List<Permission> totalList = getAllPermissionList();

        //查找一级
        List<PermissionVO> permissionsList1 = BeanUtil.copyToList(
                totalList.stream().filter(l->l.getPid().equals(0)).collect(Collectors.toList()), PermissionVO.class);
        for (PermissionVO permission1 : permissionsList1) {
            //查找二级
            List<PermissionVO> permissionsList2 = BeanUtil.copyToList(
                    totalList.stream().filter(l->l.getPid() .equals( permission1.getId())).collect(Collectors.toList()), PermissionVO.class);
            for (PermissionVO permission2 : permissionsList2) {
                //查找三级
                List<PermissionVO> permissionsList3 = BeanUtil.copyToList(
                        getAllPermissionList().stream().filter(l->l.getPid() .equals( permission2.getId())).collect(Collectors.toList()), PermissionVO.class);
                for (PermissionVO permission3 : permissionsList3) {
                    //查找四级
                    List<PermissionVO> permissionsList4 = BeanUtil.copyToList(
                            totalList.stream().filter(l->l.getPid() .equals( permission3.getId())).collect(Collectors.toList()), PermissionVO.class);
                    permission3.setSubList(permissionsList4);
                }
                permission2.setSubList(permissionsList3);
            }
            permission1.setSubList(permissionsList2);
        }
        return permissionsList1;
    }
}
