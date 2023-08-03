package com.easyback.infra.service;

import com.easyback.infra.base.PageVO;
import com.easyback.infra.entity.Device;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easyback.model.dto.DeviceAddDTO;
import com.easyback.model.dto.DeviceQryDTO;
import com.easyback.model.vo.DeviceVO;

/**
 * <p>
 * 设备信息 服务类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-05
 */
public interface DeviceService extends IService<Device> {

    PageVO<DeviceVO> getDevicePage(DeviceQryDTO dto);

    void addDevice(DeviceAddDTO dto);

}
