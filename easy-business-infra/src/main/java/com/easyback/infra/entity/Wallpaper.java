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
 * 壁纸
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wallpaper")
@ApiModel(value="Wallpaper对象", description="壁纸")
public class Wallpaper implements Serializable {

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

    @ApiModelProperty(value = "版本号")
    @TableField("version")
    @Version
    private Integer version;

    @ApiModelProperty(value = "所属管理员")
    @TableField("admin_id")
    private Integer adminId;

    @ApiModelProperty(value = "图片名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "图片Base64编码")
    @TableField("base64")
    private String base64;


}
