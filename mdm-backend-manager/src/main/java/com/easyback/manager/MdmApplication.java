package com.easyback.manager;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

/**
 * web启动类
 *
 * @author zhuangqingdian
 */
@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"com.easyback.**"}, exclude = {FreeMarkerAutoConfiguration.class})
@EnableAsync
@EnableCaching
@EnableScheduling
@Slf4j
@MapperScan("com.easyback.**.mapper")
public class MdmApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(MdmApplication.class, args);
        log.info("【MDM后台】启动成功");
    }

}
