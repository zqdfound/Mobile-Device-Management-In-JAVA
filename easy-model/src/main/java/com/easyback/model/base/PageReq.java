package com.easyback.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分页请求
 * @Author: zhuangqingdian
 * @Date:2023/3/31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分页请求")
public class PageReq implements Serializable {

    @ApiModelProperty(value = "当前页码，默认1")
    @Builder.Default
    protected long pageNum = 1;

    @ApiModelProperty(value = "每页条数，默认20")
    @Builder.Default
    protected long pageSize = 20;


}
