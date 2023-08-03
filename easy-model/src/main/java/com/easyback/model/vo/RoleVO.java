package com.easyback.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("角色")
public class RoleVO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否为系统内置角色 0:否,1:是")
    private Integer isSystem;

    @ApiModelProperty(value = " 是否删除  0-否 1-是")
    private Integer deleted;
}
