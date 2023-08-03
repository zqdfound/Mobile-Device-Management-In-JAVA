package com.easyback.model.dto;

import com.easyback.model.base.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("管理员查询实体")
public class AdminQryDTO extends PageReq {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "公司全称")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;
}