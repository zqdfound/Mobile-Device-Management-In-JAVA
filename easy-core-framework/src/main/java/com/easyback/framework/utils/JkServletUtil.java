package com.easyback.framework.utils;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @Author: zhuangqingdian
 * @Date:2023/3/25
 */
public class JkServletUtil {

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    public static String getHeader(String headerName) {
        return ServletUtil.getHeader(getRequest(),headerName, StandardCharsets.UTF_8);
    }

}
