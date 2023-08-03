package com.easyback.infra.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyback.infra.entity.DeviceApp;

/**
 * <p>
 * 设备信息 服务类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-05
 */
public interface DeviceAppService extends IService<DeviceApp> {


    DeviceApp getByDeviceId(Integer deviceId);
}
