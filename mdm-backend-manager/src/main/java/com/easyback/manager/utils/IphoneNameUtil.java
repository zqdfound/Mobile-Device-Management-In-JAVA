package com.easyback.manager.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyback.infra.entity.IphoneName;
import com.easyback.infra.service.IphoneNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * iPhone设备名称工具
 * @Author: zhuangqingdian
 * @Date:2023/7/6
 */
@Slf4j
@Component
//@Order(1)
//public class IphoneNameUtil implements CommandLineRunner {
public class IphoneNameUtil {
    @Resource
    IphoneNameService iphoneNameService;

    private Map<String,String> iphoneMap = new HashMap<>();

//    @Override
//    public void run(String... args) {
//        log.info("加载Iphone设备名称工具");
//        List<IphoneName> nameList = iphoneNameService.list();
//        nameList.forEach(l->iphoneMap.put(StringUtils.trim(l.getInName()),StringUtils.trim(l.getOutName())));
//    }

    public String getOutNameByInName(String inName){
        return iphoneNameService.getOne(new LambdaQueryWrapper<IphoneName>().eq(IphoneName::getInName,inName).last("limit 1")).getOutName();
    }
}
