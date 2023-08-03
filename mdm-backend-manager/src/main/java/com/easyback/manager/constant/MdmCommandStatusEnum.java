package com.easyback.manager.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 执行状态：-1执行失败,0待执行,1执行中，2执行成功'
 * @Author: zhuangqingdian
 * @Date:2023/7/5
 */
@Getter
@AllArgsConstructor
public enum MdmCommandStatusEnum {
    FAIL(-1, "失败"),
    WAIT(0, "待执行"),
    FINISHED(1, "执行成功"),
    ;
    private Integer key;
    private String value;


    public static String getValueByKey(Integer key){
        if(null != key){
            for (MdmCommandStatusEnum a : MdmCommandStatusEnum.values()) {
                if(a.getKey().equals(key)){
                    return a.value;
                }
            }
        }
        return null;
    }
}
