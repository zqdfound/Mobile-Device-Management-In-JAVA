package com.easyback.manager.web.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.easyback.framework.response.Result;
import com.easyback.framework.response.ResultGenerator;
import com.easyback.infra.base.PageVO;
import com.easyback.infra.service.AdminService;
import com.easyback.model.dto.AdminAddDTO;
import com.easyback.model.dto.AdminQryDTO;
import com.easyback.model.dto.AdminUpdateDTO;
import com.easyback.model.dto.MyInfoUpdateDTO;
import com.easyback.model.vo.AdminVO;
import com.easyback.model.vo.MyInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "账号管理")
@RequestMapping("/admin/manage")
@SaCheckLogin
public class AdminManageController {

    @Resource
    private AdminService adminService;

    @ApiOperation(value = "账号列表")
    @PostMapping("/page")
    @SaCheckRole("超级管理员")
    public Result<PageVO<AdminVO>> page(@RequestBody AdminQryDTO dto) {
        return ResultGenerator.genSuccessResult(adminService.getAdminPage(dto));
    }
    @ApiOperation(value = "用户列表")
    @PostMapping("/page/user")
    public Result<PageVO<AdminVO>> pageUser(@RequestBody AdminQryDTO dto) {
        return ResultGenerator.genSuccessResult(adminService.getAdminPageWithoutSuperAdmin(dto));
    }
    @ApiOperation(value = "用户列表-不分页")
    @PostMapping("/list/user")
    public Result<List<AdminVO>> listUser(@RequestBody AdminQryDTO dto) {
        return ResultGenerator.genSuccessResult(adminService.getAdminListWithoutSuperAdmin(dto));
    }


    @ApiOperation(value = "管理员详情")
    @PostMapping("/detail/{id}")
    @SaCheckRole("超级管理员")
    public Result<AdminVO> detail(@PathVariable Integer id) {
        return ResultGenerator.genSuccessResult(adminService.getAdminDetail(id));
    }

    @ApiOperation(value = "禁用/启用账号")
    @PostMapping("/enabled/{id}")
    @SaCheckRole("超级管理员")
    public Result<Boolean> enabled(@PathVariable("id") Integer id) {
        return ResultGenerator.genSuccessResult(adminService.enabled(id));
    }

    @ApiOperation(value = "新建账号")
    @PostMapping("/add")
    @SaCheckRole("超级管理员")
    public Result<Boolean> addAdmin(@RequestBody @Valid AdminAddDTO dto) {
        return ResultGenerator.genSuccessResult(adminService.addUser(dto));
    }

    @ApiOperation(value = "修改账号")
    @PostMapping("/update")
    @SaCheckRole("超级管理员")
    public Result<Boolean> updateAdmin(@RequestBody @Valid AdminUpdateDTO dto) {
        return ResultGenerator.genSuccessResult(adminService.updateUser(dto));
    }

    @ApiOperation(value = "个人信息")
    @GetMapping("/myInfo")
    public Result<MyInfoVO> myInfo() {
        return ResultGenerator.genSuccessResult(adminService.myInfo());
    }

    @ApiOperation(value = "个人信息修改")
    @PostMapping("/update/myInfo")
    public Result<Boolean> updateMyInfo(@RequestBody MyInfoUpdateDTO dto) {
        return ResultGenerator.genSuccessResult(adminService.updateMyInfo(dto));
    }
}
