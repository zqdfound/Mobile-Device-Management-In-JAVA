package com.easyback.model.dto;

import com.easyback.model.base.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("角色查询实体")
public class RoleQryDTO extends PageReq {

    @ApiModelProperty(value = "角色名称")
    private String roleName;
}
