package com.easyback.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel("商户管理员权限")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVO {

    @ApiModelProperty(value = "创建时间")
    private Integer id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "上级权限ID，无父级默认为0")
    private Integer pid;

    @ApiModelProperty(value = "下属权限列表")
    private List<PermissionVO> subList;

}
