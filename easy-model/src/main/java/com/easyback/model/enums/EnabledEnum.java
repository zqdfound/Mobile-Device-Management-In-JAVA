package com.easyback.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnabledEnum {
    OFF(0, "禁用"),
    ON(1, "启用")
    ;
    private Integer key;
    private String value;
}
