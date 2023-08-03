package com.easyback.infra.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easyback.framework.exceptions.BizException;
import com.easyback.infra.base.PageVO;
import com.easyback.infra.context.AdminContext;
import com.easyback.infra.entity.Admin;
import com.easyback.infra.entity.Device;
import com.easyback.infra.mapper.AdminMapper;
import com.easyback.infra.mapper.DeviceMapper;
import com.easyback.infra.service.DeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyback.model.dto.DeviceAddDTO;
import com.easyback.model.dto.DeviceQryDTO;
import com.easyback.model.vo.DeviceVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Resource
    AdminMapper adminMapper;


    @Override
    public PageVO<DeviceVO> getDevicePage(DeviceQryDTO dto) {
        LambdaQueryWrapper<Device> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotBlank(dto.getSerialNo()),Device::getSerialNo,dto.getSerialNo());
        qw.eq(null != dto.getAdminId(),Device::getAdminId,dto.getAdminId());
        qw.like(StringUtils.isNotBlank(dto.getUserName()),Device::getUserName,dto.getUserName());
        qw.like(StringUtils.isNotBlank(dto.getUserPhone()),Device::getUserPhone,dto.getUserPhone());
        qw.ge(null != dto.getLastConnectTimeBegin(),Device::getLastConnectTime,dto.getLastConnectTimeBegin());
        qw.le(null != dto.getLastConnectTimeEnd(),Device::getLastConnectTime,dto.getLastConnectTimeEnd());
        qw.eq(null != dto.getDeviceStatus(),Device::getDeviceStatus,dto.getDeviceStatus());
        qw.eq(null != dto.getAbmStatus(),Device::getAbmStatus,dto.getAbmStatus());
        Page<Device> devicePage = page(Page.of(dto.getPageNum(),dto.getPageSize()), qw);
        PageVO<DeviceVO> pageVO = PageVO.getPageData(devicePage,l-> BeanUtil.copyProperties(l,DeviceVO.class));
        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDevice(DeviceAddDTO dto) {
        Integer adminId = AdminContext.getUserId();
        Admin admin = adminMapper.selectById(adminId);
        if(admin.getUsableNum() <= 0){
            throw new BizException("当前用户可用余额不足，请充值");
        }
        Device device = new Device();
        device.setSerialNo(dto.getSerialNo())
                .setAdminId(adminId)
                .setAdminName(admin.getName())
                .setDeviceName(dto.getDeviceName())
                .setUserName(dto.getUserName())
                .setUserPhone(dto.getUserPhone());
        if(save(device)){
            admin.setUsableNum(admin.getUsableNum() - 1);
            if(adminMapper.updateById(admin) <= 0){
                throw new BizException("余额扣减失败");
            }
        }else{
            throw new BizException("新增设备失败");
        }
    }

}
