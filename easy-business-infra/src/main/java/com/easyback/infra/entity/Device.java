package com.easyback.infra.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备信息
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device")
@ApiModel(value="Device对象", description="设备信息")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = " 是否删除  0-否 1-是")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "版本号")
    @Version
    private Integer version;

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

    @ApiModelProperty(value = "设备udid")
    private String udid;

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

    private String imei;

    private String meid;

    @ApiModelProperty(value = "操作系统版本")
    private String osVersion;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "激活码")
    private String activeBypass;

}
