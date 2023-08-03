package com.easyback.manager.configurer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhuangqingdian
 * @Date:2023/7/3
 */
@Configuration
@ConfigurationProperties(prefix = "mdm.config")
@Data
public class MdmConfig {
    private String authorization;
    private String enroll;
    private String host;
}
