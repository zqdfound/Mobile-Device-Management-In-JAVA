package com.easyback.manager.web.admin;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.easyback.framework.response.Result;
import com.easyback.framework.response.ResultGenerator;
import com.easyback.infra.context.AdminContext;
import com.easyback.infra.service.AdminService;
import com.easyback.infra.service.PermissionService;
import com.easyback.model.dto.AdminLoginDTO;
import com.easyback.model.vo.AdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zhuangqingdian
 * @Date:2023/7/4
 */
@RestController
@Api(tags = "用户登录")
@Slf4j
@RequestMapping("/admin")
public class LoginController {

    @Resource
    private AdminService adminService;


    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result<AdminVO> login(@Valid @RequestBody AdminLoginDTO dto, HttpServletRequest request) {
        SaTokenInfo saTokenInfo = adminService.adminLogin(dto, request);
        //权限信息
        List<String> permissionList = StpUtil.getPermissionList();
        //用户信息
        AdminContext context = AdminContext.getUser();
        return ResultGenerator.genSuccessResult(AdminVO.builder()
                .tokenName(saTokenInfo.getTokenName())
                .tokenValue(saTokenInfo.getTokenValue())
                .id(context.getId())
                .username(context.getUsername())
                .name(context.getName())
                .phone(context.getPhone())
                .roleId(context.getRoleId())
                .roleName(context.getRoleName())
                .loginDate(context.getLoginDate())
                .loginIp(context.getLoginIp())
                .permissionList(permissionList)
                .build());
    }

    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public Result<?> logout(){
        StpUtil.logout();
        return ResultGenerator.genSuccessResult();
    }
}
