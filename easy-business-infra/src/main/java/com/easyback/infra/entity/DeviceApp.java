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
 * 设备APP信息
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_app")
@ApiModel(value="DeviceApp对象", description="设备APP信息")
public class DeviceApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = " 是否删除  0-否 1-是")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "设备ID")
    @TableField("device_id")
    private Integer deviceId;

    @ApiModelProperty(value = "已安装的app列表，数组")
    @TableField("installed_apps")
    private String installedApps;

    @ApiModelProperty(value = "允许使用的app列表，数组")
    @TableField("allowed_apps")
    private String allowedApps;

    @ApiModelProperty(value = "不允许使用的app列表，数组")
    @TableField("disawllowed_apps")
    private String disawllowedApps;


}
