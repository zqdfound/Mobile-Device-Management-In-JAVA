package com.easyback.manager.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: zhuangqingdian
 * @Date:2023/7/5
 */
@Getter
@AllArgsConstructor
public enum MdmCommandEnum {
    DeviceLock("DeviceLock", "锁屏/发送消息"),
    EnableLostMode("EnableLostMode", "启用丢失"),
    DisableLostMode("DisableLostMode", "关闭丢失"),
    SWITCH_WALLPAPER("SWITCH_WALLPAPER", "更换壁纸"),
    DeviceInformation("DeviceInformation", "设备信息"),
    DeviceLocation("DeviceLocation", "设备定位"),
    ACTIVATION_LOCK_DEVICE("ActivationLockDevice", "下发激活锁"),
    RESTRICT_APP("RestrictApp", "限制APP策略"),
    INSTALLED_APP_LIST("InstalledApplicationList", "设备安装APP列表"),
    ;
    private String command;
    private String remark;


    public static String getRemarkByCommand(String command){
        if(StringUtils.isNotBlank(command)){
            for (MdmCommandEnum a : MdmCommandEnum.values()) {
                if(a.getCommand().equals(command)){
                    return a.remark;
                }
            }
        }
        return null;
    }
}
