package com.easyback.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("角色新增")
public class WallpaperDTO {

    @ApiModelProperty(value = "图片名称")
    private String name;

    @ApiModelProperty(value = "图片Base64编码")
    private String base64;
}
