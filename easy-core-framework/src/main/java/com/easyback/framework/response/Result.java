package com.easyback.framework.response;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一API响应结果封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("响应结果")
@Accessors(chain = true)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -8054007511410819665L;

    @ApiModelProperty("状态码")
    private int code;
    @ApiModelProperty("提示消息")
    private String message;
    @ApiModelProperty("数据")
    private T data;

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.code;
        return this;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
