package com.zs.crmserver.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段级注解：标记需要字典转换的字段
 *
 * 在实体类字段上添加此注解，指定字典类型和目标字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictConvert {
    /**
     * 字典类型（缓存中的key）
     */
    String dicType();

    /**
     * 目标字段名（转换后的名称字段）
     */
    String targetField();

    /**
     * 字典项的ID字段名（默认为"id"）
     */
    String idField() default "id";

    /**
     * 字典项的名称字段名（默认为"typeValue"）
     */
    String nameField() default "typeValue";

    /**
     * 数据源类型（默认本地缓存）
     */
    SourceType source() default SourceType.LOCAL_CACHE;

    /**
     * 数据源类型枚举
     */
    enum SourceType {
        /** 从本地缓存（cacheMap）获取 */
        LOCAL_CACHE,
        /** 从Redis用户服务获取 */
        REDIS_USER
    }
}
