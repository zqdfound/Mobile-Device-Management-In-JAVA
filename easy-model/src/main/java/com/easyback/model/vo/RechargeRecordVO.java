package com.easyback.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("")
public class RechargeRecordVO {

    private Integer id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "充值账号ID")
    private Integer adminId;

    @ApiModelProperty(value = "公司名称")
    private String name;

    @ApiModelProperty(value = "充值台数")
    private Integer rechargeNum;

    @ApiModelProperty(value = "支付金额(分)")
    private Integer rechargeMoney;

    @ApiModelProperty(value = "支付方式 0现金")
    private Integer payType;

    @ApiModelProperty(value = "备注")
    private String remark;

}
