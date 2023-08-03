package com.easyback.model.dto;

import com.easyback.model.base.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("设备查询实体")
public class DeviceQryDTO extends PageReq {

    @ApiModelProperty(value = "序列号")
    private String serialNo;

    @ApiModelProperty(value = "添加人ID")
    private Integer adminId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    @ApiModelProperty(value = "上次通信时间-开始")
    private LocalDateTime lastConnectTimeBegin;

    @ApiModelProperty(value = "上次通信时间-结束")
    private LocalDateTime lastConnectTimeEnd;

    @ApiModelProperty(value = "设备状态:0正常")
    private Integer deviceStatus;

    @ApiModelProperty(value = "ABM状态:0不在库，1在库")
    private Integer abmStatus;

}
