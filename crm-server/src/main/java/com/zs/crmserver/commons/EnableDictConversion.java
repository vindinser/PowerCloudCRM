package com.zs.crmserver.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法级注解：标记需要进行转换的方法
 *
 * 在服务层方法上添加此注解，切面将自动处理返回数据的转换工作
 *
 * @param value 返回数据类型，用于指导切面如何处理返回值
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableDictConversion {
    /**
     * 支持的返回类型枚举
     */
    enum ReturnType {
        /** 自动检测返回值类型 */
        AUTO,
        /** PageInfo分页类型 */
        PAGE,
        /** 单个实体对象 */
        SINGLE,
        /** 对象集合 */
        COLLECTION
    }

    /**
     * 返回数据类型，默认为自动检测
     */
    ReturnType value() default ReturnType.AUTO;

    /**
     * 是否启用字典转换，默认启用
     */
    boolean enableDict() default true;

    /**
     * 是否启用用户转换，默认启用
     */
    boolean enableUser() default true;
}
