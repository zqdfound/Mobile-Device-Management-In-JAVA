package com.easyback.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("设备用户")
public class DeviceUserDTO {

    @ApiModelProperty(value = "id")
    @NotNull
    private Integer id;

    @ApiModelProperty(value = "用户姓名")
    @NotBlank
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    @NotBlank
    private String userPhone;


}
