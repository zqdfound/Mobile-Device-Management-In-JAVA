package com.easyback.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("管理员新增")
public class AdminAddDTO {

    @ApiModelProperty(value = "用户名")
    @NotBlank
    private String username;

    @ApiModelProperty(value = "密码")
    //@NotBlank
    private String password;

    @ApiModelProperty(value = "公司名")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "手机号")
    @NotBlank
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "角色ID")
    @NotNull
    private Integer roleId;

    @ApiModelProperty(value = "激活锁二级密码")
    private String activePassword;
}
