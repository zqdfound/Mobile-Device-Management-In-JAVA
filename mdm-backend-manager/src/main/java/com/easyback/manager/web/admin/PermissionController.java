package com.easyback.manager.web.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.easyback.framework.response.Result;
import com.easyback.framework.response.ResultGenerator;
import com.easyback.infra.service.PermissionService;
import com.easyback.model.vo.PermissionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限
 * @Author: zhuangqingdian
 * @Date:2023/3/29
 */
@RestController
@Api(tags = "权限")
@RequestMapping("/admin/permission")
//@SaCheckLogin
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @ApiOperation(value = "所有权限列表")
    @PostMapping("/list")
    public Result<List<PermissionVO>> listAll(){
        return ResultGenerator.genSuccessResult(permissionService.getAllList());
    }




}
