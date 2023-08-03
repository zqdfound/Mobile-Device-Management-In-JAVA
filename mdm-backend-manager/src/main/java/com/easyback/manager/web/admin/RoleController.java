package com.easyback.manager.web.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.easyback.framework.response.Result;
import com.easyback.framework.response.ResultGenerator;
import com.easyback.infra.base.PageVO;
import com.easyback.infra.service.RoleService;
import com.easyback.model.dto.RoleAddDTO;
import com.easyback.model.dto.RoleQryDTO;
import com.easyback.model.dto.RoleUpdateDTO;
import com.easyback.model.vo.RolePermVO;
import com.easyback.model.vo.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zhuangqingdian
 * @Date:2023/7/4
 */
@RestController
@Api(tags = "角色管理")
@RequestMapping("/admin/role")
@SaCheckLogin
@SaCheckRole("超级管理员")
public class RoleController {
    @Resource
    private RoleService roleService;

    @ApiOperation(value = "分页列表")
    @GetMapping("/page")
    public Result<PageVO<RoleVO>> page(RoleQryDTO dto) {
        return ResultGenerator.genSuccessResult(roleService.getRolePage(dto));
    }

    @ApiOperation(value = "列表")
    @GetMapping("/list")
    public Result<List<RoleVO>> listAll(){
        return ResultGenerator.genSuccessResult(roleService.getAllList());
    }

    @ApiOperation(value = "我的角色列表")
    @GetMapping("/list/user")
    public Result<List<String>> listUser(){
        return ResultGenerator.genSuccessResult(StpUtil.getRoleList());
    }

    @ApiOperation(value = "新增角色")
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody @Valid RoleAddDTO dto){
        return ResultGenerator.genSuccessResult(roleService.addRole(dto));
    }

    @ApiOperation(value = "修改角色")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody @Valid RoleUpdateDTO dto){
        return ResultGenerator.genSuccessResult(roleService.updateRole(dto));
    }

    @ApiOperation(value = "详情")
    @PostMapping("/detail/{id}")
    public Result<RolePermVO> detail(@PathVariable Integer id){
        return ResultGenerator.genSuccessResult(roleService.getRolePermissionDetail(id));
    }
}
