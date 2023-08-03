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
 * app信息
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("app_info")
@ApiModel(value="AppInfo对象", description="app信息")
public class AppInfo implements Serializable {

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

    @ApiModelProperty(value = "应用名城")
    @TableField("app_name")
    private String appName;

    @ApiModelProperty(value = "应用bundleId")
    @TableField("app_bundle_id")
    private String appBundleId;

    @ApiModelProperty(value = "应用图标")
    @TableField("app_icon")
    private String appIcon;


}
