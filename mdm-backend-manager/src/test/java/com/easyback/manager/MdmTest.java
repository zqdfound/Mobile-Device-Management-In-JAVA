package com.easyback.manager;

import com.easyback.infra.entity.Device;
import com.easyback.infra.mapper.AppInfoMapper;
import com.easyback.infra.mapper.DeviceMapper;
import com.easyback.infra.service.DeviceService;
import com.easyback.manager.utils.DEPUtil;
import com.easyback.manager.utils.IphoneNameUtil;
import com.easyback.manager.web.CommandsController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author: zhuangqingdian
 * @Date:2023/7/6
 */
@Slf4j
@SpringBootTest(classes = MdmApplication.class)
public class MdmTest {
    @Resource
    IphoneNameUtil iphoneNameUtil;
    @Resource
    DeviceMapper deviceMapper;
    @Resource
    DeviceService deviceService;
    @Resource
    CommandsController commandsController;
    @Resource
    AppInfoMapper appInfoMapper;

    @Test
    public void getIphoneName(){
        System.out.println(iphoneNameUtil.getOutNameByInName("iPhone11,8"));
    }

    @Test
    public void updateLatlong(){
        Device device = new Device();
        device.setId(1);
        device.setLatitude("");
        device.setLongitude("");
        deviceMapper.updateById(device);
    }

    @Test
    public void abmDeviceDetail() throws IOException {
        DEPUtil depUtil = new DEPUtil("https://mdm.zejihui.com.cn", "Basic bWljcm9tZG06cGh0T2NlVFpFaQ==");
        depUtil.deviceDetail(Arrays.asList("G0NY50N9KXM2"));
    }

    @Test
    public void restrictApp() throws IOException {

    }


    public static void main(String[] args) {
        System.out.println( System.currentTimeMillis()/1000);
    }
}
