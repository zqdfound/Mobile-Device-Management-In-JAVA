package com.easyback.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("新增设备")
public class DeviceAddDTO {

    @ApiModelProperty(value = "序列号")
    @NotBlank
    private String serialNo;

    @ApiModelProperty(value = "设备型号")
    @NotBlank
    private String deviceName;

    @ApiModelProperty(value = "用户姓名")
    @NotBlank
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    @NotBlank
    private String userPhone;


}
