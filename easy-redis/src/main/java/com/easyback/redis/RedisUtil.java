package com.easyback.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @Author: zhuangqingdian
 * @Date:2023/3/28
 */
@Component
public class RedisUtil {
    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public RedisUtil() {
    }

    public boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }

            return true;
        } catch (Exception var5) {
            log.error("失效缓存Key[" + key + "," + time + "] 出错了>>", var5);
            return false;
        }
    }

    public boolean expireAt(String key, long unixTime) {
        return this.redisTemplate.expireAt(key, new Date(unixTime));
    }

    public long getExpire(String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public boolean hasKey(String key) {
        try {
            return this.redisTemplate.hasKey(key);
        } catch (Exception var3) {
            log.error("判断key是否存在 Key[" + key + "] 出错了>>", var3);
            return false;
        }
    }

    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                this.redisTemplate.delete(key[0]);
            } else {
                this.redisTemplate.delete(Arrays.asList(key));
            }
        }

    }

    public void delAll(String pattern) {
        this.redisTemplate.delete(this.redisTemplate.keys(pattern));
    }

    public String type(String key) {
        return this.redisTemplate.type(key).getClass().getName();
    }

    public Object get(String key) {
        return key == null ? null : this.redisTemplate.opsForValue().get(key);
    }

    public String getStr(String key) {
        if (null == key || "".equals(key)) {
            return null;
        }
        Object o = this.redisTemplate.opsForValue().get(key);
        if (null != o) {
            return o.toString();
        } else {
            return null;
        }
    }

    public Object getAndSet(String key, Serializable obj) {
        return key == null ? null : this.redisTemplate.opsForValue().getAndSet(key, obj);
    }

    public Set<Object> getAll(String pattern) {
        Set<Object> values = new HashSet();
        Set<String> keys = this.redisTemplate.keys(pattern);
        Iterator var4 = keys.iterator();

        while (var4.hasNext()) {
            String key = (String) var4.next();
            values.add(this.redisTemplate.opsForValue().get(key));
        }

        return values;
    }

    public Set<Object> getAll(String pattern, long time) {
        Set<Object> values = new HashSet();
        Set<String> keys = this.redisTemplate.keys(pattern);
        Iterator var6 = keys.iterator();

        while (var6.hasNext()) {
            String key = (String) var6.next();
            this.expire(key, time);
            values.add(this.redisTemplate.opsForValue().get(key));
        }

        return values;
    }

    public boolean set(String key, Serializable value) {
        try {
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception var4) {
            log.error("存储缓存Key[" + key + "," + value + "] 出错了>>", var4);
            return false;
        }
    }

    public boolean set(String key, Serializable value, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                this.set(key, value);
            }

            return true;
        } catch (Exception var6) {
            log.error("存储缓存Key[" + key + "," + value + "," + time + "] 出错了>>", var6);
            return false;
        }
    }

    public boolean set(String key, Serializable value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0L) {
                this.redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                this.set(key, value);
            }

            return true;
        } catch (Exception var6) {
            log.error("存储缓存Key[" + key + "," + value + "," + time + "] 出错了>>", var6);
            return false;
        }
    }

    public boolean setnx(String key, Serializable value) {
        return this.redisTemplate.boundValueOps(key).setIfAbsent(value);
    }

    public long incr(String key, long step) {
        if (step < 0L) {
            log.error("递增Key[" + key + "],步长参数:" + step + "无效<<<");
            throw new RuntimeException("递增因子必须大于0");
        } else {
            return this.redisTemplate.opsForValue().increment(key, step);
        }
    }

    public long decr(String key, long step) {
        if (step < 0L) {
            log.error("递减Key[" + key + "],步长参数:" + step + "无效<<<");
            throw new RuntimeException("递减因子必须大于0");
        } else {
            return this.redisTemplate.opsForValue().increment(key, -step);
        }
    }

    public Object hGet(String key, String item) {
        return this.redisTemplate.opsForHash().get(key, item);
    }

    public Map<Object, Object> hmGet(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    public boolean hmSet(String key, Map<String, Object> map) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception var4) {
            log.error("存储hashKey[" + key + ",键值:" + map + "] 出错了<<<", var4);
            return false;
        }
    }

    public boolean hmSet(String key, Map<String, Object> map, long time) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            log.error("存储hashKey[" + key + ",键值:" + map + ",time:" + time + "] 出错了<<<", var6);
            return false;
        }
    }

    public boolean hSet(String key, String item, Object value) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception var5) {
            log.error("存储hashKey[" + key + ",项:" + item + ",值:" + value + "] 出错了<<<", var5);
            return false;
        }
    }

    public boolean hSet(String key, String item, Object value, long time) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var7) {
            log.error("存储hashKey[" + key + ",项:" + item + ",值:" + value + ",time:" + time + "] 出错了<<<", var7);
            return false;
        }
    }

    public void hDel(String key, Object... item) {
        this.redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hHasKey(String key, String item) {
        return this.redisTemplate.opsForHash().hasKey(key, item);
    }

    public double hIncr(String key, String item, double step) {
        return this.redisTemplate.opsForHash().increment(key, item, step);
    }

    public double hDecr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, -by);
    }

    public Set<Object> sGet(String key) {
        try {
            return this.redisTemplate.opsForSet().members(key);
        } catch (Exception var3) {
            log.error("获取Set [" + key + "] 出错了<<<", var3);
            return null;
        }
    }

    public Object sPop(String key) {
        try {
            return this.redisTemplate.opsForSet().pop(key);
        } catch (Exception var3) {
            log.error("获取Set [" + key + "] 出错了<<<", var3);
            return null;
        }
    }

    public List<Object> sPop(String key, long count) {
        try {
            return this.redisTemplate.opsForSet().pop(key, count);
        } catch (Exception var5) {
            log.error("获取Set [" + key + "] 出错了<<<", var5);
            return null;
        }
    }

    public boolean sHasKey(String key, Object value) {
        try {
            return this.redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception var4) {
            log.error("查询Set [" + key + ",value:" + value + "] 出错了<<<", var4);
            return false;
        }
    }

    public long sSet(String key, Object... values) {
        try {
            return this.redisTemplate.opsForSet().add(key, values);
        } catch (Exception var4) {
            log.error("存储Set数据 [" + key + ",values:" + values + "] 出错了<<<", var4);
            return 0L;
        }
    }

    public long sSet(String key, long time, Object... values) {
        try {
            Long count = this.redisTemplate.opsForSet().add(key, values);
            if (time > 0L) {
                this.expire(key, time);
            }

            return count;
        } catch (Exception var6) {
            log.error("存储Set数据 [" + key + ",values:" + values + "] 出错了<<<", var6);
            return 0L;
        }
    }

    public long sGetSize(String key) {
        try {
            return this.redisTemplate.opsForSet().size(key);
        } catch (Exception var3) {
            log.error("获取set缓存的长度 [" + key + "] 出错了<<<", var3);
            return 0L;
        }
    }

    public long sRemove(String key, Object... values) {
        try {
            Long count = this.redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception var4) {
            log.error("移除值为value的Set [" + key + ",values:" + values + "] 出错了<<<", var4);
            return 0L;
        }
    }

    public List<Object> lGet(String key, long start, long end) {
        try {
            return this.redisTemplate.opsForList().range(key, start, end);
        } catch (Exception var7) {
            log.error("获取list缓存 [" + key + ",start:" + start + ",end:" + end + "] 出错了<<<", var7);
            return null;
        }
    }

    public long lGetSize(String key) {
        try {
            return this.redisTemplate.opsForList().size(key);
        } catch (Exception var3) {
            log.error("获取list缓存的长度 [" + key + "] 出错了<<<", var3);
            return 0L;
        }
    }

    public Object lGetIndex(String key, long index) {
        try {
            return this.redisTemplate.opsForList().index(key, index);
        } catch (Exception var5) {
            log.error("获取list缓存 [" + key + ",index:" + index + "] 出错了<<<", var5);
            return null;
        }
    }

    public boolean lSet(String key, Serializable value) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception var4) {
            log.error("向list添加缓存对象 [" + key + ",value:" + value + "] 出错了<<<", var4);
            return false;
        }
    }

    public boolean lSet(String key, Serializable value, long time) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            log.error("向list添加缓存对象 [" + key + ",value:" + value + ",time:" + time + "] 出错了<<<", var6);
            return false;
        }
    }

    public boolean lSet(String key, List<Serializable> value) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, new Object[]{value});
            return true;
        } catch (Exception var4) {
            log.error("添加list缓存对象 [" + key + ",value:" + value + "] 出错了<<<", var4);
            return false;
        }
    }

    public boolean lSet(String key, List<Serializable> value, long time) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, new Object[]{value});
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            log.error("添加list缓存对象 [" + key + ",value:" + value + ",time:" + time + "] 出错了<<<", var6);
            return false;
        }
    }

    public boolean lUpdateIndex(String key, long index, Serializable value) {
        try {
            this.redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception var6) {
            log.error("修改list缓存对象 [" + key + ",index:" + index + ",value:" + value + "] 出错了<<<", var6);
            return false;
        }
    }

    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = this.redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception var6) {
            log.error("移除list缓存对象 [" + key + ",count:" + count + ",value:" + value + "] 出错了<<<", var6);
            return 0L;
        }
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


}
