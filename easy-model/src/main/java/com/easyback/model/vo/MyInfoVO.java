package com.easyback.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel("我的信息")
@Data
public class MyInfoVO{


    @ApiModelProperty(value = "管理员ID")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    @ApiModelProperty(value = "角色名")
    private String roleName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后登录日期")
    private LocalDateTime loginDate;

    @ApiModelProperty(value = "最后登录IP")
    private String loginIp;

    @ApiModelProperty(value = "权限列表")
    private List<String> permissionList;

    @ApiModelProperty(value = "可用数量")
    private Integer usableNum;

    @ApiModelProperty(value = "总充值数量")
    private Integer totalNum;

    @ApiModelProperty(value = "1:启动;0禁用")
    private Integer enabled;

    @ApiModelProperty(value = "权限名称列表")
    private List<String> permissionItemList;
}
