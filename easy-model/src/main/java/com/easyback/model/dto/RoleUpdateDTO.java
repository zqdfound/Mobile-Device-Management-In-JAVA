package com.easyback.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("角色修改")
public class RoleUpdateDTO {

    @ApiModelProperty(value = "ID")
    @NotNull
    private Integer id;

    @ApiModelProperty(value = "角色名称")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "权限ID列表,不修改不传")
    private List<Integer> permissionIdList;
}
