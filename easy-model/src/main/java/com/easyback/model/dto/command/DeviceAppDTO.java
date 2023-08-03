package com.easyback.model.dto.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zhuangqingdian
 * @Date:2023/8/3
 */

@Data
@ApiModel("设备app信息")
public class DeviceAppDTO {

    @ApiModelProperty(value = "标志符")
    private String appBundleId;

    @ApiModelProperty(value = "名称")
    private String appName;

}
