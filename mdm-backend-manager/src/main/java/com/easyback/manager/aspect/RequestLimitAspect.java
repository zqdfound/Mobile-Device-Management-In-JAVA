package com.easyback.manager.aspect;

import com.easyback.manager.annotation.RequestLimit;
import com.easyback.framework.exceptions.BizException;
import com.easyback.framework.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 单机限流
 * @author zhuangqingdian
 * @date 2019/12/24 10:10 上午
 */
@Component
@Aspect
@Slf4j
public class RequestLimitAspect {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Before("execution(public * com.easyback.manager.web.*.*(..)) && @annotation(limit)")
    public void requestLimit(JoinPoint joinpoint, RequestLimit limit) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String ip = CommonUtil.getIpAddress(request);
        String url = request.getRequestURL().toString();
        String key = "manager_supply_chain_req_limit:".concat(url).concat(ip);
        log.info("key="+key);
        //加1
        long count = redisTemplate.opsForValue().increment(key,1);
        if (count == 1) {
            //设置1分钟过期
            redisTemplate.expire(key, limit.time(), TimeUnit.SECONDS);
        }
        if (count > limit.count()) {
            log.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
            throw new BizException("由于您当前操作频繁，请在稍候再试");
        }
    }
}
