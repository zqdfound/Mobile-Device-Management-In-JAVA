package com.easyback.manager.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easyback.framework.exceptions.BizException;
import com.easyback.framework.response.Result;
import com.easyback.framework.response.ResultGenerator;
import com.easyback.infra.base.PageVO;
import com.easyback.infra.context.AdminContext;
import com.easyback.infra.entity.*;
import com.easyback.infra.mapper.AppInfoMapper;
import com.easyback.infra.service.AdminService;
import com.easyback.infra.service.CommandService;
import com.easyback.infra.service.DeviceAppService;
import com.easyback.infra.service.DeviceService;
import com.easyback.manager.configurer.MdmConfig;
import com.easyback.manager.constant.MdmCommandEnum;
import com.easyback.manager.utils.BypassGenerator;
import com.easyback.manager.utils.DEPUtil;
import com.easyback.model.base.PageReq;
import com.easyback.model.dto.DeviceAddDTO;
import com.easyback.model.dto.DeviceQryDTO;
import com.easyback.model.dto.DeviceUserDTO;
import com.easyback.model.dto.command.DeviceAppDTO;
import com.easyback.model.dto.dep.BypassCodeDTO;
import com.easyback.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhuangqingdian
 * @Date:2023/7/3
 */
@RestController
@Api(tags = "设备管理")
@Slf4j
@RequestMapping("/device")
@SaCheckLogin
public class DeviceController {

    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceAppService deviceAppService;
    @Resource
    private CommandService commandService;
    @Resource
    private AdminService adminService;
    @Resource
    private MdmConfig mdmConfig;
    @Resource
    private AppInfoMapper appInfoMapper;


    @ApiOperation(value = "设备列表")
    @PostMapping("/page")
    public Result<PageVO<DeviceVO>> page(@RequestBody DeviceQryDTO dto) {
        Integer adminId = AdminContext.getUserId();
        dto.setAdminId(adminId);
        return ResultGenerator.genSuccessResult(deviceService.getDevicePage(dto));
    }

    @ApiOperation(value = "新增设备")
    @PostMapping("/add")
    public Result add(@RequestBody @Valid DeviceAddDTO dto) {
        if (deviceService.count(new LambdaQueryWrapper<Device>()
                .eq(Device::getSerialNo, dto.getSerialNo())) > 0) {
            return ResultGenerator.genFailResult("序列号" + dto.getSerialNo() + "设备已存在");
        }
        deviceService.addDevice(dto);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "更改设备用户")
    @PostMapping("/update/user")
    public Result updateUser(@RequestBody DeviceUserDTO dto) {
        Device device = new Device();
        device.setId(dto.getId());
        device.setUserName(dto.getUserName());
        device.setUserPhone(dto.getUserPhone());
        deviceService.updateById(device);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "设备详情")
    @GetMapping("/detail")
    public Result<DeviceDetailVO> add(@RequestParam @ApiParam("设备ID") Integer deviceId) {
        Device device = deviceService.getById(deviceId);
        if (device == null) {
            throw new BizException("获取设备失败");
        }
        DeviceDetailVO vo = BeanUtil.copyProperties(device, DeviceDetailVO.class);
        return ResultGenerator.genSuccessResult(vo);
    }

    @ApiOperation(value = "默认APP列表")
    @GetMapping("/app/list")
    public Result<List<AppInfoVO>> appList() {
        List<AppInfo> list = appInfoMapper.selectList(null);
        List<AppInfoVO> voList = BeanUtil.copyToList(list, AppInfoVO.class);
        return ResultGenerator.genSuccessResult(voList);
    }

    @ApiOperation(value = "设备已安装APP列表")
    @GetMapping("/app/list/{deviceId}")
    public Result<List<AppInfoVO>> appList(@PathVariable @ApiParam("设备ID") Integer deviceId) {
        List<AppInfoVO> list = new ArrayList<>();
        DeviceApp deviceApp = deviceAppService.getByDeviceId(deviceId);
        if (deviceApp != null && StringUtils.isNotBlank(deviceApp.getInstalledApps())) {
            List<DeviceAppDTO> appList = JSONUtil.toList(deviceApp.getInstalledApps(), DeviceAppDTO.class);
            list = BeanUtil.copyToList(appList,AppInfoVO.class);
            return ResultGenerator.genSuccessResult(list);
        } else {
            return ResultGenerator.genFailResult("获取中...");
        }
    }

    @ApiOperation(value = "设备仅允许使用的APP列表")
    @GetMapping("/app/allowedList/{deviceId}")
    public Result<List<AppInfoVO>> appAllowedList(@PathVariable @ApiParam("设备ID") Integer deviceId) {
        List<AppInfoVO> list = new ArrayList<>();
        DeviceApp deviceApp = deviceAppService.getByDeviceId(deviceId);
        if (deviceApp != null && StringUtils.isNotBlank(deviceApp.getAllowedApps())) {
            List<DeviceAppDTO> appList = JSONUtil.toList(deviceApp.getAllowedApps(), DeviceAppDTO.class);
            list = BeanUtil.copyToList(appList,AppInfoVO.class);
        }
        return ResultGenerator.genSuccessResult(list);
    }


    @ApiOperation(value = "设备不允许使用的APP列表")
    @GetMapping("/app/disAllowedList/{deviceId}")
    public Result<List<AppInfoVO>> appDisAllowedList(@PathVariable @ApiParam("设备ID") Integer deviceId) {
        List<AppInfoVO> list = new ArrayList<>();
        DeviceApp deviceApp = deviceAppService.getByDeviceId(deviceId);
        if (deviceApp != null && StringUtils.isNotBlank(deviceApp.getDisawllowedApps())) {
            List<DeviceAppDTO> appList = JSONUtil.toList(deviceApp.getDisawllowedApps(), DeviceAppDTO.class);
            list = BeanUtil.copyToList(appList,AppInfoVO.class);
        }
        return ResultGenerator.genSuccessResult(list);
    }


    @ApiOperation(value = "查看激活锁解锁密码")
    @GetMapping("/getActivationBypass")
    public Result<String> getActivationBypass(@RequestParam @ApiParam("设备ID") Integer deviceId,
                                              @RequestParam @ApiParam("二级密码") String secondPassword) {
        Integer adminId = AdminContext.getUserId();
        Device device = deviceService.getById(deviceId);
        if (device == null) {
            return ResultGenerator.genFailResult("获取设备失败");
        }
        if (device.getActiveStatus() == 0) {
            return ResultGenerator.genFailResult("激活锁未上锁");
        }
        Admin admin = adminService.getById(adminId);
        if (checkSecondPwd(secondPassword, admin.getActivePassword())) {
            return ResultGenerator.genSuccessResult(device.getActiveBypass());
        } else {
            return ResultGenerator.genFailResult("二级密码错误");
        }
    }


    @ApiOperation(value = "操作记录")
    @GetMapping("/command/records")
    public Result<PageVO<CommandVO>> add(@RequestParam @ApiParam("设备ID") Integer deviceId, PageReq req) {
        Page<Command> commandPage = commandService.page(Page.of(req.getPageNum(), req.getPageSize()),
                new LambdaQueryWrapper<Command>().eq(Command::getDeviceId, deviceId));
        PageVO<CommandVO> voPage = PageVO.getPageData(commandPage, l -> BeanUtil.copyProperties(l, CommandVO.class));
        return ResultGenerator.genSuccessResult(voPage);
    }

    @ApiOperation(value = "定位信息")
    @GetMapping("/location")
    public Result<LocationVO> location(@RequestParam @ApiParam("设备ID") Integer deviceId) {
        Device device = deviceService.getById(deviceId);
        if (device == null) {
            throw new BizException("获取设备失败");
        }
        if (device.getLockStatus() == 0) {
            return ResultGenerator.genFailResult("设备不在丢失状态");
        }
        String latitude = device.getLatitude();
        String longitude = device.getLongitude();
        if (StringUtils.isBlank(latitude) || StringUtils.isBlank(longitude)) {
            throw new BizException("位置信息获取中...请稍后");
        }
        return ResultGenerator.genSuccessResult(LocationVO.builder()
                .latitude(latitude)
                .longitude(longitude).build());
    }


    @ApiOperation(value = "[设备操作]-下发激活锁")
    @GetMapping("/activeCode")
    public Result<String> activeCode(@RequestParam @ApiParam("设备ID") Integer deviceId) throws IOException {
        Device device = deviceService.getById(deviceId);
        if (0 == device.getMdmStatus()) {
            throw new BizException("设备" + device.getSerialNo() + "未注册");
        }
        if (1 == device.getActiveStatus()) {
            return ResultGenerator.genFailResult("该设备已下放激活锁，请勿重复操作");
        }
        //下放激活锁
        if (activationLockDevice(device)) {
            Integer adminId = AdminContext.getUserId();
            Command command = new Command();
            command.setAdminId(adminId)
                    .setDeviceId(deviceId)
                    .setSerialNo(device.getSerialNo())
                    .setStatus(1)
                    .setCommand(MdmCommandEnum.ACTIVATION_LOCK_DEVICE.getCommand())
                    .setCommandName(MdmCommandEnum.ACTIVATION_LOCK_DEVICE.getRemark())
                    .setUuid(UUID.randomUUID().toString());
            commandService.save(command);
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("下放激活锁失败");
        }
    }

    @ApiOperation(value = "[设备操作]-删除监管")
    @GetMapping("/disownDevice")
    public Result<String> disownDevice(@RequestParam @ApiParam("设备ID") Integer deviceId,
                                       @RequestParam @ApiParam("二级密码") String secondPassword) throws IOException {
        Integer adminId = AdminContext.getUserId();
        Admin admin = adminService.getById(adminId);
        if (checkSecondPwd(secondPassword, admin.getActivePassword())) {
            return ResultGenerator.genFailResult("密码错误");
        }
        Device device = deviceService.getById(deviceId);
        if (0 == device.getMdmStatus()) {
            throw new BizException("设备" + device.getSerialNo() + "未注册");
        }
        if (0 == device.getAbmStatus()) {
            throw new BizException("设备" + device.getSerialNo() + "未加入ABM");
        }
        if (disownDevice(device)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("删除监管失败");
    }


    //下放激活锁
    private boolean activationLockDevice(Device device) throws IOException {
        DEPUtil depUtil = new DEPUtil(mdmConfig.getHost(), mdmConfig.getAuthorization());
        String deviceSerial = device.getSerialNo();
        BypassCodeDTO bypassMap = BypassGenerator.getByRandom();
        String bypass = bypassMap.getByPass();
        String escrow = bypassMap.getHash();
        log.info("设备{}下发激活锁：{}", deviceSerial, bypass);
        if (depUtil.activationLockDevice(deviceSerial, escrow)) {
            device.setActiveStatus(1);//修改激活锁状态
            device.setAbmStatus(1);//修改ABM在库状态
            device.setActiveBypass(bypass);
            deviceService.updateById(device);
            return true;
        }
        return false;
    }

    //移除监管
    private boolean disownDevice(Device device) throws IOException {
        DEPUtil depUtil = new DEPUtil(mdmConfig.getHost(), mdmConfig.getAuthorization());
        String deviceSerial = device.getSerialNo();
        log.info("设备{}移除ABM", deviceSerial);
        if (depUtil.disownDevice(deviceSerial)) {
            deviceService.removeById(device.getId());
            return true;
        }
        return false;
    }

    //校验二级密码
    private static boolean checkSecondPwd(String secondPassword, String activationPwd) {
        if (StringUtils.isBlank(secondPassword)) {
            secondPassword = null;
        }
        if (StringUtils.isBlank(activationPwd)) {
            activationPwd = null;
        }
        return ObjectUtil.equals(secondPassword, activationPwd);
    }


}
