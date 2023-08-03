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
 * 命令执行信息
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("command")
@ApiModel(value="Command对象", description="命令执行信息")
public class Command implements Serializable {

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

    @ApiModelProperty(value = "命令UUID")
    private String uuid;

    @ApiModelProperty(value = "所属管理员")
    private Integer adminId;

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

    @ApiModelProperty(value = "执行结果备注")
    private String remark;


}
