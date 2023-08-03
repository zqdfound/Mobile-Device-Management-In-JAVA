package com.easyback.infra.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easyback.framework.exceptions.BizException;
import com.easyback.infra.base.PageVO;
import com.easyback.infra.entity.Admin;
import com.easyback.infra.entity.Permission;
import com.easyback.infra.entity.PermissionRole;
import com.easyback.infra.entity.Role;
import com.easyback.infra.mapper.AdminMapper;
import com.easyback.infra.mapper.PermissionMapper;
import com.easyback.infra.mapper.PermissionRoleMapper;
import com.easyback.infra.mapper.RoleMapper;
import com.easyback.infra.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyback.model.dto.RoleAddDTO;
import com.easyback.model.dto.RoleQryDTO;
import com.easyback.model.dto.RoleUpdateDTO;
import com.easyback.model.vo.RolePermVO;
import com.easyback.model.vo.RoleVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-04
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    PermissionRoleMapper permissionRoleMapper;
    @Resource
    PermissionMapper permissionMapper;
    @Resource
    AdminMapper adminMapper;

    @Override
    public List<String> getListByUserId(Integer userId) {
        //1 查询角色ID
        Admin fxAdmin = adminMapper.selectById(userId);
        Integer roleId = fxAdmin.getRoleId();
        //2 查询角色
        Role role = getById(roleId);
        return Arrays.asList(role.getName());
    }
    @Override
    public PageVO<RoleVO> getRolePage(RoleQryDTO dto) {
        LambdaQueryWrapper<Role> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotBlank(dto.getRoleName()),Role::getName,dto.getRoleName());
        qw.orderByDesc(Role::getCreateTime);
        Page<Role> pageDo = page(new Page<>(dto.getPageNum(),dto.getPageSize()),qw);
        PageVO<RoleVO> pageVO = PageVO.getPageData(pageDo,l-> BeanUtil.copyProperties(l,RoleVO.class));
        return pageVO;
    }
    @Override
    public List<RoleVO> getAllList() {
        LambdaQueryWrapper<Role> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(Role::getCreateTime);
        List<Role> list = list(qw);
        return BeanUtil.copyToList(list,RoleVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addRole(RoleAddDTO dto) {
        LambdaQueryWrapper<Role> qw = new LambdaQueryWrapper<>();
        qw.eq(Role::getName,dto.getName());
        if(list(qw).size() > 0){
            throw new BizException("角色名已存在!");
        }
        Role teRole = BeanUtil.copyProperties(dto,Role.class);
        //1 保存角色
        save(teRole);
        //2 保存角色权限关联信息
        if(CollectionUtil.isNotEmpty(dto.getPermissionIdList())){
            Set<Integer> permissionIds = dto.getPermissionIdList().stream().collect(Collectors.toSet());
            for (Integer pid: permissionIds) {
                PermissionRole pr = new PermissionRole();
                pr.setRoleId(teRole.getId());
                pr.setPermissionId(pid);
                permissionRoleMapper.insert(pr);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRole(RoleUpdateDTO dto) {
        LambdaQueryWrapper<Role> qw = new LambdaQueryWrapper<>();
        qw.eq(Role::getName,dto.getName());
        qw.ne(Role::getId,dto.getId());
        if(list(qw).size() > 0){
            throw new BizException("角色名已存在!");
        }
        Role fxRole = BeanUtil.copyProperties(dto,Role.class);
        //1 更新角色信息
        updateById(fxRole);
        //2 删除角色权限关联信息
        permissionRoleMapper.delete(new LambdaQueryWrapper<PermissionRole>().eq(PermissionRole::getRoleId,fxRole.getId()));
        //3 新增角色权限关联信息
        if(CollectionUtil.isNotEmpty(dto.getPermissionIdList())){
            Set<Integer> permissionIds = dto.getPermissionIdList().stream().collect(Collectors.toSet());
            for (Integer pid: permissionIds) {
                PermissionRole pr = new PermissionRole();
                pr.setRoleId(fxRole.getId());
                pr.setPermissionId(pid);
               permissionRoleMapper.insert(pr);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public RolePermVO getRolePermissionDetail(Integer id) {
        Role role = getById(id);
        if(role == null){
            throw new BizException("数据不存在");
        }
        RolePermVO vo = BeanUtil.copyProperties(role,RolePermVO.class);
        //获取权限列表
        List<PermissionRole> prList = permissionRoleMapper.selectList(new LambdaQueryWrapper<PermissionRole>().eq(PermissionRole::getRoleId,role.getId()));
        if(CollectionUtil.isNotEmpty(prList)){
            Set<Integer> permissionIds = prList.stream().map(PermissionRole::getPermissionId).collect(Collectors.toSet());
            List<Permission> permissionList = permissionMapper.selectList(new LambdaQueryWrapper<Permission>().in(Permission::getId,permissionIds));
            Set<String> permissionItemList = permissionList.stream().map(Permission::getName).collect(Collectors.toSet());
            vo.setPermissionItemList(permissionItemList);
        }
        return vo;
    }
}
