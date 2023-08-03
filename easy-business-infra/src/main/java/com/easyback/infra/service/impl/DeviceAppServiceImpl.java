package com.easyback.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyback.infra.entity.DeviceApp;
import com.easyback.infra.mapper.DeviceAppMapper;
import com.easyback.infra.service.DeviceAppService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 设备信息 服务实现类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-05
 */
@Service
public class DeviceAppServiceImpl extends ServiceImpl<DeviceAppMapper, DeviceApp> implements DeviceAppService {

    @Resource
    DeviceAppMapper deviceAppMapper;

    @Override
    public DeviceApp getByDeviceId(Integer deviceId) {
        DeviceApp deviceApp = deviceAppMapper.selectOne(new LambdaQueryWrapper<DeviceApp>().eq(DeviceApp::getDeviceId, deviceId).last("limit 1"));
        return deviceApp;
    }
}
