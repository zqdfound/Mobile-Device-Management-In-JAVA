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
@ApiModel("设备信息")
public class DeviceVO {

    private Integer id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "序列号")
    private String serialNo;

    @ApiModelProperty(value = "添加人ID")
    private Integer adminId;

    @ApiModelProperty(value = "添加人名称")
    private String adminName;

    @ApiModelProperty(value = "设备型号")
    private String deviceName;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    @ApiModelProperty(value = "上次通信时间")
    private LocalDateTime lastConnectTime;

    @ApiModelProperty(value = "mdm服务器注册状态:0未注册，1已注册")
    private Integer mdmStatus;

    @ApiModelProperty(value = "设备状态:0正常")
    private Integer deviceStatus;

    @ApiModelProperty(value = "恢复出厂限制:0未限制，1已限制")
    private Integer factoryStatus;

    @ApiModelProperty(value = "USB限制:0未限制，1已限制")
    private Integer usbStatus;

    @ApiModelProperty(value = "激活锁状态:0未上锁，1已上锁")
    private Integer activeStatus;

    @ApiModelProperty(value = "锁定状态:0未丢失，1已丢失")
    private Integer lockStatus;

    @ApiModelProperty(value = "ABM状态:0不在库，1在库")
    private Integer abmStatus;

}
