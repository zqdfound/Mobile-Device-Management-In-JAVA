package com.easyback.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("新增充值")
public class RechargeAddDTO{

    @ApiModelProperty(value = "用户Id")
    @NotNull
    private Integer adminId;

    @ApiModelProperty(value = "充值台数量")
    private Integer rechargeNum;

    @ApiModelProperty(value = "充值金额（分）")
    @Min(0)
    private Integer rechargeMoney;

    @ApiModelProperty(value = "支付方式：0现金")
    @Min(0)
    private Integer payType;

    @ApiModelProperty(value = "创建时间-开始")
    private String remark;

}