package com.zs.crmserver.manager;

import com.zs.crmserver.model.TUser;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class RedisManager {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 从Redis中获取数据
     * @param key
     * @return
     */
    public Object getValue(String key) {

        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * Redis中存数据
     * @param key 存入的key
     * @param value 存入的值
     * @return result
     */
    public <T> Object setValue(String key, Collection<T> value) {
        Object[] t = new Object[value.size()];
        value.toArray(t);
        // 存储数据
        Long result = redisTemplate.opsForList().leftPushAll(key, t);
        // 设置 过期时间
        redisTemplate.expire(key, 24, TimeUnit.HOURS);

        return result;
    }

    /**
     * Redis中存数据
     * @param key 存入的key
     * @param value 存入的值
     * @param timeout 过期时间
     * @return result
     */
    public <T> Object setValue(String key, Collection<T> value, long timeout) {
        Object[] t = new Object[value.size()];
        value.toArray(t);
        // 存储数据
        Long result = redisTemplate.opsForList().leftPushAll(key, t);
        // 设置 过期时间
        redisTemplate.expire(key, timeout, TimeUnit.HOURS);

        return result;
    }
}
