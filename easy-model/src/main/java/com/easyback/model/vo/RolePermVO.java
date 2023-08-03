package com.easyback.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel("角色权限详情")
public class RolePermVO extends RoleVO{

    @ApiModelProperty(value = "权限名列表")
    private Set<String> permissionItemList;
}
