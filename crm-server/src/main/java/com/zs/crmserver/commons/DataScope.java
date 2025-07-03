package com.zs.crmserver.commons;

import java.lang.annotation.*;

/**
 * 数据范围注解
 * sql语句末尾添加一个过滤条件
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    // 表的别名
    public String tableAlias() default "";

    // 表的字段名
    public String tableField() default "";

}
