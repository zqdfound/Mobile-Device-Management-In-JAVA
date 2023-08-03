package com.easyback.model.dto.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel("app信息")
public class AppIdsDTO {

    @ApiModelProperty(value = "列表")
    @NotEmpty
    private List<DeviceAppDTO> appList;

}
