package com.easyback.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("APP信息")
public class AppInfoVO {

    private Integer id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "应用名城")
    private String appName;

    @ApiModelProperty(value = "应用bundleId")
    private String appBundleId;

    @ApiModelProperty(value = "应用图标")
    private String appIcon;
}
