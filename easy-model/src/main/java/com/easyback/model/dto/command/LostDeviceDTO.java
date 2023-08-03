package com.easyback.model.dto.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("设备丢失")
public class LostDeviceDTO {

    @ApiModelProperty(value = "设备ID")
    @NotNull
    private Integer deviceId;

    @ApiModelProperty(value = "显示的文本")
    @NotBlank
    private String message;

}
