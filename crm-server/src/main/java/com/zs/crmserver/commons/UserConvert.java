package com.zs.crmserver.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段级注解：标记需要用户信息转换的字段
 *
 * 在实体类字段上添加此注解，指定用户ID字段和对应的用户名字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserConvert {
    /**
     * 用户名字段名
     */
    String userNameField();
}
