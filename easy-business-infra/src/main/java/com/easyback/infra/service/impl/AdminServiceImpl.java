package com.easyback.infra.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easyback.framework.exceptions.BizException;
import com.easyback.framework.utils.IPUtil;
import com.easyback.framework.utils.PwdUtil;
import com.easyback.infra.base.PageVO;
import com.easyback.infra.context.AdminContext;
import com.easyback.infra.entity.Admin;
import com.easyback.infra.entity.Role;
import com.easyback.infra.mapper.AdminMapper;
import com.easyback.infra.mapper.RoleMapper;
import com.easyback.infra.permission.AdminStpInterface;
import com.easyback.infra.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyback.model.dto.*;
import com.easyback.model.enums.EnabledEnum;
import com.easyback.model.vo.AdminVO;
import com.easyback.model.vo.MyInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-04
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private AdminStpInterface adminStpInterface;

    @Override
    public SaTokenInfo adminLogin(AdminLoginDTO dto, HttpServletRequest request) {
        Admin admin = getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, dto.getUsername()));
        if (null == admin) {
            throw new BizException("用户不存在");
        }
        if (EnabledEnum.OFF.getKey() == admin.getEnabled()) {
            throw new BizException("账号被禁用");
        }
        Boolean failPwd = !PwdUtil.verify(admin.getPassword(), dto.getPassword(), admin.getRandom());
        if (failPwd) {
            throw new BizException("用户名或密码错误");
        }
        String ipAddr = IPUtil.getIpAddress(request);
        admin.setLoginIp(ipAddr);
        admin.setLoginDate(LocalDateTime.now());
        //更新登录ip，登录时间
        updateById(admin);
        StpUtil.login(admin.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        //设置权限角色
        adminStpInterface.getPermissionList(tokenInfo.getLoginId(), tokenInfo.getLoginType());
        adminStpInterface.getRoleList(tokenInfo.getLoginId(), tokenInfo.getLoginType());
        //设置用户上下文
        AdminContext context = AdminContext.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .phone(admin.getPhone())
                .loginIp(ipAddr)
                .loginDate(LocalDateTime.now())
                .roleId(admin.getRoleId())
                .roleName(roleMapper.selectById(admin.getRoleId()).getName())
                .build();
        StpUtil.getSession().set(AdminContext.KEY, context);

        return tokenInfo;
    }



    @Override
    public PageVO<AdminVO> getAdminPage(AdminQryDTO dto) {
        Map<Integer,String> roleMap = roleMapper.selectList(null).stream().collect(
                Collectors.toMap(
                        Role::getId,
                        r -> r.getName() == null ? "" : r.getName(),
                        (oldValue, newValue) -> newValue)
        );
        LambdaQueryWrapper<Admin> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNoneBlank(dto.getUsername()), Admin::getUsername, dto.getUsername());
        qw.like(StringUtils.isNoneBlank(dto.getName()), Admin::getName, dto.getName());
        qw.like(StringUtils.isNoneBlank(dto.getPhone()), Admin::getPhone, dto.getPhone());
        qw.eq(dto.getRoleId() != null, Admin::getRoleId, dto.getRoleId());
        qw.orderByDesc(Admin::getCreateTime);
        Page<Admin> doPage = page(new Page<>(dto.getPageNum(), dto.getPageSize()), qw);
        return PageVO.getPageData(doPage, l -> {
            AdminVO vo = BeanUtil.copyProperties(l, AdminVO.class);
            vo.setRoleName(roleMap.get(l.getRoleId()));
            return vo;
        });
    }

    @Override
    public PageVO<AdminVO> getAdminPageWithoutSuperAdmin(AdminQryDTO dto) {
        Map<Integer,String> roleMap = roleMapper.selectList(null).stream().collect(
                Collectors.toMap(
                        Role::getId,
                        r -> r.getName() == null ? "" : r.getName(),
                        (oldValue, newValue) -> newValue)
        );
        LambdaQueryWrapper<Admin> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNoneBlank(dto.getUsername()), Admin::getUsername, dto.getUsername());
        qw.like(StringUtils.isNoneBlank(dto.getName()), Admin::getName, dto.getName());
        qw.like(StringUtils.isNoneBlank(dto.getPhone()), Admin::getPhone, dto.getPhone());
        qw.eq( Admin::getRoleId, 2);//普通用户ID写死为2
        qw.orderByDesc(Admin::getCreateTime);
        Page<Admin> doPage = page(new Page<>(dto.getPageNum(), dto.getPageSize()), qw);
        return PageVO.getPageData(doPage, l -> {
            AdminVO vo = BeanUtil.copyProperties(l, AdminVO.class);
            vo.setRoleName(roleMap.get(l.getRoleId()));
            return vo;
        });
    }

    @Override
    public List<AdminVO> getAdminListWithoutSuperAdmin(AdminQryDTO dto) {
        Map<Integer,String> roleMap = roleMapper.selectList(null).stream().collect(
                Collectors.toMap(
                        Role::getId,
                        r -> r.getName() == null ? "" : r.getName(),
                        (oldValue, newValue) -> newValue)
        );
        LambdaQueryWrapper<Admin> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNoneBlank(dto.getUsername()), Admin::getUsername, dto.getUsername());
        qw.like(StringUtils.isNoneBlank(dto.getName()), Admin::getName, dto.getName());
        qw.like(StringUtils.isNoneBlank(dto.getPhone()), Admin::getPhone, dto.getPhone());
        qw.eq(dto.getRoleId() != null, Admin::getRoleId, dto.getRoleId());
        qw.ne( Admin::getRoleId, 1);
        qw.orderByDesc(Admin::getCreateTime);
        List<Admin> adminList = list(qw);
        List<AdminVO> voList = BeanUtil.copyToList(adminList,AdminVO.class);
        voList.forEach(l->l.setRoleName(roleMap.get(l.getRoleId())));
        return voList;
    }

    @Override
    public AdminVO getAdminDetail(Integer id) {
        Admin Admin = getById(id);
        if (Admin == null) {
            throw new BizException("数据不存在");
        }
        String roleName = roleMapper.selectById(Admin.getRoleId()).getName();
        AdminVO vo = BeanUtil.copyProperties(Admin, AdminVO.class);
        vo.setRoleName(roleName);
        return vo;
    }

    @Override
    public Boolean enabled(Integer id) {
        Admin Admin = getById(id);
        if (Admin == null) {
            throw new BizException("数据不存在");
        }
        //禁用
        if (Admin.getEnabled() == EnabledEnum.ON.getKey()) {
            //强制下线
            StpUtil.logout(id);
            Admin.setEnabled(EnabledEnum.OFF.getKey());
        }
        //启用
        else if (Admin.getEnabled() == EnabledEnum.OFF.getKey()) {
            Admin.setEnabled(EnabledEnum.ON.getKey());
        }
        return updateById(Admin);
    }

    @Override
    public Boolean addUser(AdminAddDTO dto) {
        //1 重名判断
        LambdaQueryWrapper<Admin> qw = new LambdaQueryWrapper<>();
        qw.eq(Admin::getUsername, dto.getUsername());
        if (list(qw).size() > 0) {
            throw new BizException("该用户名已被占用");
        }
        String random = PwdUtil.getRandom();
        String password = PwdUtil.encryption(StringUtils.isBlank(dto.getPassword())? "123456":dto.getPassword(), random);
        //2 插入新用户
        Admin t = new Admin();
        t.setUsername(dto.getUsername());
        t.setPassword(password);
        t.setRandom(random);
        t.setName(dto.getName());
        t.setEmail(dto.getEmail());
        t.setPhone(dto.getPhone());
        t.setRoleId(dto.getRoleId());
        t.setActivePassword(dto.getActivePassword());
        return save(t);
    }

    @Override
    public Boolean updateUser(AdminUpdateDTO dto) {
        Admin admin = getById(dto.getId());
        if (admin == null) {
            throw new BizException("数据不存在");
        }
        admin.setName(dto.getName());
        admin.setPhone(dto.getPhone());
        admin.setEmail(dto.getEmail());
        admin.setRoleId(dto.getRoleId());
        admin.setActivePassword(dto.getActivePassword());
        //修改密码
        if(StringUtils.isNoneBlank(dto.getPassword())){
            String random = admin.getRandom();
            String newPwdEnc = PwdUtil.encryption(dto.getPassword(), random);
            admin.setPassword(newPwdEnc);
        }
        return updateById(admin);
    }

    @Override
    public MyInfoVO myInfo() {
        Integer userId = AdminContext.getUserId();
        AdminVO AdminVO = getAdminDetail(userId);
        List<String> permissionList = StpUtil.getPermissionList();
        MyInfoVO vo = BeanUtil.copyProperties(AdminVO,MyInfoVO.class);
        vo.setPermissionItemList(permissionList);
        return vo;
    }

    @Override
    public Boolean updateMyInfo(MyInfoUpdateDTO dto) {
        Admin Admin = getById(dto.getId());
        if (Admin == null) {
            throw new BizException("数据不存在");
        }
        Admin.setName(dto.getName());
        Admin.setPhone(dto.getPhone());
        Admin.setEmail(dto.getEmail());
        String oldPwd = dto.getOldPassword();
        String newPwd = dto.getNewPassword();
        //修改密码
        if(StringUtils.isNoneBlank(oldPwd) && StringUtils.isNoneBlank(newPwd)){
            String random = Admin.getRandom();
            String oldPwdEnc = PwdUtil.encryption(oldPwd, random);
            if(ObjectUtil.notEqual(oldPwdEnc,Admin.getPassword())){
                throw new BizException("旧密码有误");
            }
            String newPwdEnc = PwdUtil.encryption(newPwd, random);
            Admin.setPassword(newPwdEnc);
        }
        return updateById(Admin);
    }
}
