package com.easyback.model.dto;

import com.easyback.model.base.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@ApiModel("充值记录查询实体")
public class RechargeQryDTO extends PageReq {

    @ApiModelProperty(value = "公司全称")
    private String name;

    @ApiModelProperty(value = "充值数量-最小")
    private Integer rechargeNumMin;

    @ApiModelProperty(value = "充值数量-最大")
    private Integer rechargeNumMax;

    @ApiModelProperty(value = "支付方式：0现金")
    private Integer payType;

    @ApiModelProperty(value = "创建时间-开始")
    private LocalDateTime createTimeBegin;

    @ApiModelProperty(value = "创建时间-结束")
    private LocalDateTime createTimeEnd;
}