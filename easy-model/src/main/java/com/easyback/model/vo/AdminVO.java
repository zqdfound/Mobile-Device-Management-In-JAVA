package com.easyback.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("管理员实体信息")
public class AdminVO {

    @ApiModelProperty(value = "token名称")
    private String tokenName;

    @ApiModelProperty(value = "token值")
    private String tokenValue;

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

    @ApiModelProperty(value = "激活锁二级密码")
    private String activePassword;
}
