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
@ApiModel("命令")
public class CommandVO {

    private Integer id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "设备Id")
    private Integer deviceId;

    @ApiModelProperty(value = "命令")
    private String command;

    @ApiModelProperty(value = "命令名称")
    private String commandName;

    @ApiModelProperty(value = "序列号")
    private String serialNo;

    @ApiModelProperty(value = "执行状态：-1执行失败,0待执行,1执行成功")
    private Integer status;

}
