package com.easyback.infra.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 充值记录
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("recharge_record")
@ApiModel(value="RechargeRecord对象", description="充值记录")
public class RechargeRecord implements Serializable {

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

    @ApiModelProperty(value = "充值账号ID")
    private Integer adminId;

    @ApiModelProperty(value = "充值台数")
    private Integer rechargeNum;

    @ApiModelProperty(value = "支付金额(分)")
    private Integer rechargeMoney;

    @ApiModelProperty(value = "支付方式 0现金")
    private Integer payType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "操作人ID")
    private Integer operatorId;


}
