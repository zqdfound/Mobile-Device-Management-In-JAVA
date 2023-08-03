package com.easyback.manager.web.webhook;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyback.framework.exceptions.BizException;
import com.easyback.framework.response.Result;
import com.easyback.framework.response.ResultGenerator;
import com.easyback.infra.entity.Command;
import com.easyback.infra.entity.Device;
import com.easyback.infra.entity.DeviceApp;
import com.easyback.infra.service.CommandService;
import com.easyback.infra.service.DeviceAppService;
import com.easyback.infra.service.DeviceService;
import com.easyback.manager.constant.MdmCommandEnum;
import com.easyback.manager.constant.MdmCommandStatusEnum;
import com.easyback.manager.constant.MdmConstants;
import com.easyback.manager.utils.IphoneNameUtil;
import com.easyback.manager.utils.RespParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 消息响应接口
 *
 * @Author: zhuangqingdian
 * @Date:2023/6/30
 */
@Slf4j
@ApiIgnore
@RestController
@RequestMapping("")
public class MdmWebhookController {

    @Resource
    private CommandService commandService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private IphoneNameUtil iphoneNameUtil;
    @Resource
    private DeviceAppService deviceAppService;



    @PostMapping("/webhook")
    public Result webhook(@RequestBody JSONObject jsonObject) {
        log.info("===========[MDM]Webhook接收到消息===========================================");
        log.info(jsonObject.toString());
        log.info("==========================================================================");
        String topic = jsonObject.getStr("topic");
        switch (topic) {
            case MdmConstants.RESP_AUTH:
                //注册设备
                registerDevice(jsonObject);
                break;
            case MdmConstants.RESP_TOKEN_UPDATE:
                //更新设备Token
                updateDeviceToken(jsonObject);
                break;
            case MdmConstants.RESP_CONNECT:
                connectEvent(jsonObject);
                break;
            default:
                log.error("无法识别的topic:{}", topic);
        }
        return ResultGenerator.genSuccessResult();
    }

//==================================================================================================================================================================================================================================================

    /**
     * 注册设备
     * <?xml version="1.0" encoding="UTF-8"?>
     * <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
     * <plist version="1.0">
     * <dict>
     * 	<key>BuildVersion</key>
     * 	<string>19F77</string>
     * 	<key>MessageType</key>
     * 	<string>Authenticate</string>
     * 	<key>OSVersion</key>
     * 	<string>15.5</string>
     * 	<key>ProductName</key>
     * 	<string>iPhone11,8</string>
     * 	<key>SerialNumber</key>
     * 	<string>G0NY50N9KXM2</string>
     * 	<key>Topic</key>
     * 	<string>com.apple.mgmt.External.d8ce74d8-bb7d-439f-acc1-fc21ea49f753</string>
     * 	<key>UDID</key>
     * 	<string>00008020-0005444A0E51002E</string>
     * </dict>
     * </plist>
     * @param jsonObject
     */
    private void registerDevice(JSONObject jsonObject) {
        String udid = jsonObject.getJSONObject("checkin_event").getStr("udid");
        String payloadEncode = jsonObject.getJSONObject("checkin_event").getStr("raw_payload");
        Map<String, String> respMap = RespParser.getRespMapByPayload(payloadEncode);
        String ProductName = respMap.get("ProductName");
        String OSVersion = respMap.get("OSVersion");
        String IphoneName = iphoneNameUtil.getOutNameByInName(ProductName);
        String SerialNumber = respMap.get("SerialNumber");
        //更新数据库
        Device device = getDeviceBySerialNo(SerialNumber);
        if(StringUtils.isNotBlank(IphoneName)){
            device.setDeviceName(IphoneName);//覆盖型号信息
        }
        device.setUdid(udid);
        device.setOsVersion(OSVersion);
        device.setLastConnectTime(LocalDateTime.now());
        deviceService.updateById(device);
    }



    /**
     * 更新设备token
     * <?xml version="1.0" encoding="UTF-8"?>
     * <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
     * <plist version="1.0">
     * <dict>
     * 	<key>AwaitingConfiguration</key>
     * 	<false/>
     * 	<key>MessageType</key>
     * 	<string>TokenUpdate</string>
     * 	<key>PushMagic</key>
     * 	<string>5CF5A04E-F2DB-435F-9787-7CE4A8BFBE68</string>
     * 	<key>Token</key>
     * 	<data>
     * 	f8QyXzvXsE8V8Umu7OmRi+3KDodPUxjsoSXPHz70K5M=
     * 	</data>
     * 	<key>Topic</key>
     * 	<string>com.apple.mgmt.External.d8ce74d8-bb7d-439f-acc1-fc21ea49f753</string>
     * 	<key>UDID</key>
     * 	<string>00008020-0005444A0E51002E</string>
     * 	<key>UnlockToken</key>
     * 	<data>
     * 	省略...
     * 	</data>
     * </dict>
     * </plist>
     * @param jsonObject
     */
    private void updateDeviceToken(JSONObject jsonObject) {
        String udid = jsonObject.getJSONObject("checkin_event").getStr("udid");
        String payloadEncode = jsonObject.getJSONObject("checkin_event").getStr("raw_payload");
        Map<String, String> respMap = RespParser.getRespMapByPayload(payloadEncode);
        String UnlockToken = respMap.get("UnlockToken");
        //更新到数据库
        Device device = getDeviceByUdid(udid);
        device.setMdmStatus(1);//已注册
        device.setLastConnectTime(LocalDateTime.now());
        deviceService.updateById(device);
    }



    //处理连接事件
    private void connectEvent(JSONObject jsonObject) {
        String status = jsonObject.getJSONObject("acknowledge_event").getStr("status");
        log.info("状态:{}",status);
        if (MdmConstants.Idle.equals(status)) {
            //Idle直接忽略
            return;
        }
        if (!MdmConstants.Acknowledged.equals(status)) {
            //保存异常信息
            String errCommandId = jsonObject.getJSONObject("acknowledge_event").getStr("command_uuid");
            Command command = commandService.getOne(new LambdaQueryWrapper<Command>().eq(Command::getUuid,errCommandId).last("limit 1"));
            command.setStatus(MdmCommandStatusEnum.FAIL.getKey());
            command.setRemark(jsonObject.getJSONObject("acknowledge_event").getStr("raw_payload"));
            commandService.updateById(command);
            return;
        }

        //获取返回值Map
        String payloadEncode = jsonObject.getJSONObject("acknowledge_event").getStr("raw_payload");
        String commandUUID = jsonObject.getJSONObject("acknowledge_event").getStr("command_uuid");
//        Map<String, String> respMap = MdmUtil.getRespMapByPayload(payloadEncode);
        //更新命令为执行成功
        Command command = commandService.getOne(new LambdaQueryWrapper<Command>().eq(Command::getUuid,commandUUID).last("limit 1"));
        command.setStatus(MdmCommandStatusEnum.FINISHED.getKey());
        commandService.updateById(command);
        //命令执行成功后续处理
        commandSuccess(command,payloadEncode);
    }

    private void commandSuccess(Command command,String payloadEncode) {
        if(command.getCommand().equals(MdmCommandEnum.EnableLostMode.getCommand())){//启用丢失
            Device device = new Device();
            device.setId(command.getDeviceId());
            device.setLockStatus(1);
            device.setLatitude("");
            device.setLongitude("");
            device.setLastConnectTime(LocalDateTime.now());
            deviceService.updateById(device);
            return;
        }
        if(command.getCommand().equals(MdmCommandEnum.DisableLostMode.getCommand())){//关闭丢失
            Device device = new Device();
            device.setId(command.getDeviceId());
            device.setLockStatus(0);
            device.setLastConnectTime(LocalDateTime.now());
            deviceService.updateById(device);
            return;
        }
        if(command.getCommand().equals(MdmCommandEnum.DeviceInformation.getCommand())){//设备信息
            Map<String, String> respMap = RespParser.getRespMapByPayload(payloadEncode);
            String IMEI = respMap.get("IMEI");
            String MEID = respMap.get("MEID");
            String OSVersion = respMap.get("OSVersion");
            Device device = new Device();
            device.setId(command.getDeviceId());
            device.setImei(IMEI);
            device.setMeid(MEID);
            device.setOsVersion(OSVersion);
            device.setLastConnectTime(LocalDateTime.now());
            deviceService.updateById(device);//更新设备信息
            return;
        }
        if(command.getCommand().equals(MdmCommandEnum.DeviceLocation.getCommand())){//设备定位
            Map<String, String> respMap = RespParser.getRespMapByPayload(payloadEncode);
            String Latitude = respMap.get("Latitude");
            String Longitude = respMap.get("Longitude");
            Device device = new Device();
            device.setId(command.getDeviceId());
            device.setLatitude(Latitude);
            device.setLongitude(Longitude);
            device.setLastConnectTime(LocalDateTime.now());
            deviceService.updateById(device);//更新定位信息
            return;
        }
        if(command.getCommand().equals(MdmCommandEnum.INSTALLED_APP_LIST.getCommand())) {//获取安装APP列表
            List<Map<String, String>> appList = RespParser.getInstallAppMap(payloadEncode);
            DeviceApp deviceApp = deviceAppService.getByDeviceId(command.getDeviceId());
            if(deviceApp == null){
                deviceApp = new DeviceApp();
                deviceApp.setDeviceId(command.getDeviceId());
                deviceApp.setInstalledApps(JSONUtil.toJsonStr(appList));
                deviceAppService.save(deviceApp);
            }else{
                deviceApp.setInstalledApps(JSONUtil.toJsonStr(appList));
                deviceAppService.updateById(deviceApp);
            }
            return;
        }
    }


//===================================================================================================================================================================================

    private Device getDeviceBySerialNo(String serialNumber) {
        Device device = deviceService.getOne(new LambdaQueryWrapper<Device>().eq(Device::getSerialNo,serialNumber).last("limit 1"));
        if(device == null){
            throw new BizException("[Webhook]获取序列号: "+serialNumber+" 设备失败");
        }else{
            return device;
        }
    }
    private Device getDeviceByUdid(String udid) {
        Device device = deviceService.getOne(new LambdaQueryWrapper<Device>().eq(Device::getUdid,udid).last("limit 1"));
        if(device == null){
            throw new BizException("[Webhook]获取UDID: "+udid+" 设备失败");
        }else{
            return device;
        }
    }


}
