package com.zs.crmserver.util;

import org.springframework.util.ObjectUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CacheUtils {

    /**
     * 带有缓存的查询工具方法
     * @param cacheSelector 生产者
     * @param dataSelector 生产者
     * @param cacheSetter 消费者
     * @return
     * @param <T>
     */
    public static <T> T getCacheData(Supplier<T> cacheSelector, Supplier<T> dataSelector, Consumer<T> cacheSetter) {
        // 从Redis查询数据
        T data = cacheSelector.get();
        if(ObjectUtils.isEmpty(data)) {
            // 数据库查询数据
            data = dataSelector.get();
            if(!ObjectUtils.isEmpty(data)) {
                // 数据苦查询到数据，将数据放入Redis
                cacheSetter.accept(data);
            }
        }
        return data;
    }
}
