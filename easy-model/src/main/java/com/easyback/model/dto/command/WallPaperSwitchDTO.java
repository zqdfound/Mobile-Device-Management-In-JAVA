package com.easyback.model.dto.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("壁纸切换命令")
public class WallPaperSwitchDTO {

    @ApiModelProperty(value = "设备ID")
    @NotNull
    private Integer deviceId;

    @ApiModelProperty(value = "壁纸ID")
    @NotNull
    private Integer wallpaperId;

    @ApiModelProperty(value = "壁纸类型：1锁屏，2主页，3锁屏加主页")
    @NotNull
    private Integer type;
}
