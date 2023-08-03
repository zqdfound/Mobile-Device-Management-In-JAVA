package com.easyback.manager.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.easyback.framework.exceptions.BizException;
import com.easyback.framework.response.Result;
import com.easyback.framework.response.ResultGenerator;
import com.easyback.framework.utils.HttpUtil;
import com.easyback.infra.context.AdminContext;
import com.easyback.infra.entity.Command;
import com.easyback.infra.entity.Device;
import com.easyback.infra.entity.DeviceApp;
import com.easyback.infra.mapper.AppInfoMapper;
import com.easyback.infra.service.CommandService;
import com.easyback.infra.service.DeviceAppService;
import com.easyback.infra.service.DeviceService;
import com.easyback.infra.service.WallpaperService;
import com.easyback.manager.configurer.MdmConfig;
import com.easyback.manager.constant.MdmCommandEnum;
import com.easyback.manager.constant.MicroMdmApi;
import com.easyback.manager.utils.ReqPlistBuilder;
import com.easyback.model.dto.command.AppIdsDTO;
import com.easyback.model.dto.command.DeviceAppDTO;
import com.easyback.model.dto.command.LostDeviceDTO;
import com.easyback.model.dto.command.WallPaperSwitchDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: zhuangqingdian
 * @Date:2023/7/4
 */
@RestController
@Api(tags = "远程命令")
@Slf4j
@RequestMapping("/command")
@SaCheckLogin
public class CommandsController {

    @Resource
    private MdmConfig mdmConfig;
    @Resource
    private DeviceAppService deviceAppService;
    @Resource
    private CommandService commandService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private WallpaperService wallpaperService;
    @Resource
    private AppInfoMapper appInfoMapper;

    @ApiOperation("丢失设备")
    @PostMapping("/lostDevice")
    public Result lostDevice(@RequestBody LostDeviceDTO dto) {
        Device device = getDeviceById(dto.getDeviceId());
        String commandUUID = UUID.randomUUID().toString();
        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("Footnote", "dddddd");
        paramMap.put("Message", dto.getMessage());
//        paramMap.put("PhoneNumber", "15950515069");

        //命令保存到数据库
        saveCommand(device,commandUUID,MdmCommandEnum.EnableLostMode);

        try{
            String xml = ReqPlistBuilder.getCommandPList(MdmCommandEnum.EnableLostMode.getCommand(), commandUUID, paramMap);
            sendCommand(device.getUdid(), xml);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultGenerator.genFailResult("发送命令失败");
        }
    }

    @ApiOperation("解除丢失")
    @PostMapping("/unLostDevice/{deviceId}")
    public Result unLostDevice(@PathVariable @ApiParam("设备Id") Integer deviceId) {
        Device device = getDeviceById(deviceId);
        String commandUUID = UUID.randomUUID().toString();
        //命令保存到数据库
        saveCommand(device,commandUUID,MdmCommandEnum.DisableLostMode);
        try{
            String xml = ReqPlistBuilder.getCommandPList(MdmCommandEnum.DisableLostMode.getCommand(), commandUUID,null);
            sendCommand(device.getUdid(), xml);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultGenerator.genFailResult("发送命令失败");
        }
    }

    @ApiOperation("获取丢失设备定位")
    @PostMapping("/device/location/{deviceId}")
    public Result deviceLocation(@PathVariable @ApiParam("设备Id") Integer deviceId) {
        Device device = getDeviceById(deviceId);
        if(device.getLockStatus() != 1){
            return ResultGenerator.genFailResult("设备不是已丢失状态");
        }
        String commandUUID = UUID.randomUUID().toString();

        //命令保存到数据库
        saveCommand(device,commandUUID,MdmCommandEnum.DeviceLocation);
        try{
            String xml = ReqPlistBuilder.getCommandPList(MdmCommandEnum.DeviceLocation.getCommand(), commandUUID, null);
            sendCommand(device.getUdid(), xml);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultGenerator.genFailResult("发送命令失败");
        }
    }

    @ApiOperation("获取设备信息")
    @PostMapping("/deviceInfo/{deviceId}")
    public Result deviceInfo(@PathVariable @ApiParam("设备Id") Integer deviceId) {
        Device device = getDeviceById(deviceId);
        String commandUUID = UUID.randomUUID().toString();

        //命令保存到数据库
        saveCommand(device,commandUUID,MdmCommandEnum.DeviceInformation);
        try{
            String xml = ReqPlistBuilder.getDeviceInfoPList(commandUUID);
            sendCommand(device.getUdid(), xml);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultGenerator.genFailResult("发送命令失败");
        }
    }


    @ApiOperation("切换壁纸")
    @PostMapping("/wallpaper/switch")
    public Result wallpaperSwitch(@RequestBody WallPaperSwitchDTO dto) {
        Device device = getDeviceById(dto.getDeviceId());
        String commandUUID = UUID.randomUUID().toString();
        String base64 = wallpaperService.getById(dto.getWallpaperId()).getBase64();
        StringBuffer paramSB = new StringBuffer();
        paramSB.append("<key>Image</key>");
        paramSB.append("<data>");
        paramSB.append(base64);
        paramSB.append("</data>");
        paramSB.append("<key>Item</key>");
        paramSB.append("<string>Wallpaper</string>");
        paramSB.append("<key>Where</key>");
        paramSB.append("<integer>"+dto.getType()+"</integer>");

        //命令保存到数据库
        saveCommand(device,commandUUID,MdmCommandEnum.SWITCH_WALLPAPER);
        try{
            String xml = ReqPlistBuilder.getSettingPList(commandUUID,paramSB);
            sendCommand(device.getUdid(), xml);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultGenerator.genFailResult("发送命令失败");
        }
    }


    @ApiOperation("获取当前设备安装的APP")
    @PostMapping("/app/Installed/{deviceId}")
    public Result appInstalled(@PathVariable @ApiParam("设备Id") Integer deviceId) {
        String commandUUID = UUID.randomUUID().toString();
        Device device = deviceService.getById(deviceId);

        saveCommand(device,commandUUID,MdmCommandEnum.INSTALLED_APP_LIST);
        String xml = ReqPlistBuilder.getInstalledApp(commandUUID);
        try {
            sendCommand(device.getUdid(),xml);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResultGenerator.genFailResult("发送命令失败");
        }
        return ResultGenerator.genSuccessResult();

    }

    @ApiOperation("限制仅允许的APP")
    @PostMapping("/app/allowed/{deviceId}")
    public Result appAllowed(@PathVariable @ApiParam("设备Id") Integer deviceId,
                             @RequestBody AppIdsDTO dto) {
        String commandUUID = UUID.randomUUID().toString();
        Device device = deviceService.getById(deviceId);

        saveCommand(device,commandUUID,MdmCommandEnum.RESTRICT_APP);
        //更新设备APP信息
        DeviceApp deviceApp = deviceAppService.getByDeviceId(deviceId);
        if(deviceApp == null){
            deviceApp = new DeviceApp();
            deviceApp.setDeviceId(deviceId);
            deviceApp.setAllowedApps(JSONUtil.toJsonStr(dto.getAppList()));
            deviceAppService.save(deviceApp);
        }else{
            deviceApp.setAllowedApps(JSONUtil.toJsonStr(dto.getAppList()));
            deviceAppService.updateById(deviceApp);
        }
        try {
            String xml = getDistrictAppXml(deviceApp,commandUUID);
            sendCommand(device.getUdid(),xml);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResultGenerator.genFailResult("发送命令失败");
        }

        return ResultGenerator.genSuccessResult();
    }


    @ApiOperation("限制不允许的APP")
    @PostMapping("/app/disAllowed/{deviceId}")
    public Result appDisAllowed(@PathVariable @ApiParam("设备Id") Integer deviceId,
                                @RequestBody AppIdsDTO dto) {
        String commandUUID = UUID.randomUUID().toString();
        Device device = deviceService.getById(deviceId);

        //命令保存到数据库
        saveCommand(device,commandUUID,MdmCommandEnum.RESTRICT_APP);
        //更新设备APP信息
        DeviceApp deviceApp = deviceAppService.getByDeviceId(deviceId);
        if(deviceApp == null){
            deviceApp = new DeviceApp();
            deviceApp.setDeviceId(deviceId);
            deviceApp.setDisawllowedApps(JSONUtil.toJsonStr(dto.getAppList()));
            deviceAppService.save(deviceApp);
        }else{
            deviceApp.setDisawllowedApps(JSONUtil.toJsonStr(dto.getAppList()));
            deviceAppService.updateById(deviceApp);
        }
        try {
            String xml = getDistrictAppXml(deviceApp,commandUUID);
            sendCommand(device.getUdid(),xml);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResultGenerator.genFailResult("发送命令失败");
        }
        return ResultGenerator.genSuccessResult();

    }
//=================================================================================================================================================================================================================================

    //保存命令
    private void saveCommand(Device device,String commandUUID,MdmCommandEnum mdmCommandEnum){
        Integer adminId = AdminContext.getUserId();
        Command command = new Command();
        command.setAdminId(adminId)
                .setDeviceId(device.getId())
                .setSerialNo(device.getSerialNo())
                .setCommand(mdmCommandEnum.getCommand())
                .setCommandName(mdmCommandEnum.getRemark())
                .setUuid(commandUUID);
        commandService.save(command);
    }


    //获取设备信息udid
    private Device getDeviceById(Integer deviceId){
        Device device = deviceService.getById(deviceId);
        if(device == null){
            throw new BizException("获取设备信息失败");
        }
        if(device.getMdmStatus() == 0){
            throw new BizException("设备"+device.getSerialNo()+"未注册");
        }
        return device;
    }

    //发送命令
    public void sendCommand(String udid, String xml) throws IOException {
        String url = mdmConfig.getHost() + MicroMdmApi.COMMANDS + "/" + udid;
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", mdmConfig.getAuthorization());
        HttpUtil.doPost(url, map, xml,HttpUtil.XML);
    }

    //限制app策略命令xml
    private String getDistrictAppXml(DeviceApp deviceApp,String commandUUID){
        List<String> allowedIds = new ArrayList<>();
        List<String> disAllowedIds = new ArrayList<>();
        if(StringUtils.isNotBlank(deviceApp.getAllowedApps())){
            allowedIds = JSONUtil.toList(deviceApp.getAllowedApps(), DeviceAppDTO.class).stream().map(DeviceAppDTO::getAppBundleId).collect(Collectors.toList());
        }
        if(StringUtils.isNotBlank(deviceApp.getDisawllowedApps())){
            disAllowedIds = JSONUtil.toList(deviceApp.getDisawllowedApps(), DeviceAppDTO.class).stream().map(DeviceAppDTO::getAppBundleId).collect(Collectors.toList());
        }
        String appXml = ReqPlistBuilder.getRestrictApp(allowedIds,disAllowedIds);
        String xml = ReqPlistBuilder.getInstallProfile(commandUUID,appXml);
        return xml;
    }
}
