package com.easyback.thirdpart.dingtalk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by huangqilong on 2019/10/28
 */
@Component
@Configuration
@PropertySource(value= {"classpath:/application-${spring.profiles.active}.yml"},
        encoding="UTF-8")
@ConfigurationProperties(prefix = "dingtalk")
@Data
public class DingTalkApiConfig {

    private String appkey;

    private String secretKey;
}
