package com.easyback.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RechargeRecordExportVO {

    @ApiModelProperty(value = "公司名称")
    private String name;

    @ApiModelProperty(value = "充值台数")
    private Integer rechargeNum;

    @ApiModelProperty(value = "支付金额(元)")
    private String rechargeMoney;

    @ApiModelProperty(value = "支付方式")
    private String payType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
