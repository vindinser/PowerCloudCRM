package com.zs.crmserver.aspect;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.commons.EnableDictConversion;
import com.zs.crmserver.util.DictConversionUtil;
import com.zs.crmserver.util.UserConversionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 字典与用户信息转换切面
 *
 * 该切面负责拦截标记了@EnableDictConversion的方法，
 * 并对返回结果进行自动转换处理
 */
@Aspect
@Component
public class DictConvertAspect {
    private static final Logger log = LoggerFactory.getLogger(DictConvertAspect.class);

    /**
     * 拦截标记了@EnableDictConversion的方法
     *
     * @param joinPoint 连接点
     * @param enableAnnotation EnableDictConversion注解
     * @return 处理后的结果
     */
    @Around("@annotation(enableAnnotation)")
    public Object doDictConversion(ProceedingJoinPoint joinPoint,
                                   EnableDictConversion enableAnnotation) throws Throwable {

        Object result = joinPoint.proceed();
        if (result == null) return result;

        // 获取转换配置
        boolean enableUser = enableAnnotation.enableUser();
        boolean enableDict = enableAnnotation.enableDict();

        // 根据注解配置确定处理策略
        EnableDictConversion.ReturnType returnType = enableAnnotation.value();

        try {
            if (returnType == EnableDictConversion.ReturnType.AUTO) {
                result = processAutoType(result, enableUser, enableDict);
            } else {
                switch (returnType) {
                    case PAGE:
                        if (result instanceof PageInfo) {
                            processPageInfo((PageInfo<?>) result, enableUser, enableDict);
                        }
                        break;
                    case SINGLE:
                        processEntity(result, enableUser, enableDict);
                        break;
                    case COLLECTION:
                        if (result instanceof Collection) {
                            processList((Collection<?>) result, enableUser, enableDict);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            log.warn("字典转换处理失败", e);
        }

        return result;
    }

    /**
     * 自动检测并处理返回类型
     *
     * @param result 方法返回结果
     * @return 处理后的结果
     */
    private Object processAutoType(Object result, boolean enableUser, boolean enableDict) {
        if (result instanceof PageInfo) {
            processPageInfo((PageInfo<?>) result, enableUser, enableDict);
            return result;
        }

        if (result instanceof Collection) {
            processList((Collection<?>) result, enableUser, enableDict);
            return result;
        }

        if (isResultWrapper(result)) {
            Object data = extractDataFromWrapper(result);
            if (data != null) {
                data = processAutoType(data, enableUser, enableDict); // 递归处理
                setDataToWrapper(result, data);
            }
            return result;
        }

        // 单个对象
        processEntity(result, enableUser, enableDict);
        return result;
    }

    /**
     * 处理分页数据
     *
     * @param pageInfo PageInfo对象
     */
    private void processPageInfo(PageInfo<?> pageInfo, boolean enableUser, boolean enableDict) {
        if (pageInfo != null && pageInfo.getList() != null) {
            processList(pageInfo.getList(), enableUser, enableDict);
        }
    }

    /**
     * 处理集合数据
     *
     * @param collection 对象集合
     */
    private void processList(Collection<?> collection, boolean enableUser, boolean enableDict) {
        if (collection == null || collection.isEmpty()) return;
        collection.forEach(entity -> processEntity(entity, enableUser, enableDict));
    }

    /**
     * 处理单个实体
     *
     * @param entity 实体对象
     */
    private void processEntity(Object entity, boolean enableUser, boolean enableDict) {
        if (entity == null) return;

        // 1. 处理用户信息转换
        if (enableUser) {
            UserConversionUtil.convertEntity(entity);
        }

        // 2. 处理字典转换
        if(enableDict) {
            DictConversionUtil.convertEntity(entity);
        }

        // 3. 处理嵌套对象
        // processNestedObjects(entity);
    }

    /**
     * 处理嵌套对象（对象内部的嵌套对象和集合）
     *
     * @param entity 实体对象
     */
    private void processNestedObjects(Object entity, boolean enableUser, boolean enableDict) {
        Class<?> clazz = entity.getClass();

        // 遍历所有字段，递归处理嵌套对象
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(entity);

                // 处理嵌套对象
                if (value != null && !isPrimitiveOrWrapper(value.getClass())) {
                    // 递归处理单个对象
                    if (!(value instanceof Collection)) {
                        processEntity(value, enableUser, enableDict);
                    }
                    // 处理集合中的对象
                    else {
                        for (Object item : (Collection<?>) value) {
                            if (!isPrimitiveOrWrapper(item.getClass())) {
                                processEntity(item, enableUser, enableDict);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("嵌套对象处理失败|field={}", field.getName(), e);
            }
        }
    }

    /**
     * 判断是否为包装类结果（如ResultDTO）
     *
     * @param result 方法返回结果
     * @return 是否为包装类
     */
    private boolean isResultWrapper(Object result) {
        try {
            // 尝试获取常见包装类字段
            DictConversionUtil.getField(result.getClass(), "data");
            DictConversionUtil.getField(result.getClass(), "result");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从包装类中提取数据
     *
     * @param wrapper 包装类对象
     * @return 数据对象
     */
    private Object extractDataFromWrapper(Object wrapper) {
        try {
            Field dataField = DictConversionUtil.getField(wrapper.getClass(), "data");
            dataField.setAccessible(true);
            return dataField.get(wrapper);
        } catch (Exception e) {
            try {
                Field resultField = DictConversionUtil.getField(wrapper.getClass(), "result");
                resultField.setAccessible(true);
                return resultField.get(wrapper);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * 将数据设置回包装类
     *
     * @param wrapper 包装类对象
     * @param data 数据对象
     */
    private void setDataToWrapper(Object wrapper, Object data) {
        try {
            Field dataField = DictConversionUtil.getField(wrapper.getClass(), "data");
            dataField.setAccessible(true);
            dataField.set(wrapper, data);
        } catch (Exception e) {
            try {
                Field resultField = DictConversionUtil.getField(wrapper.getClass(), "result");
                resultField.setAccessible(true);
                resultField.set(wrapper, data);
            } catch (Exception ex) {
                // 忽略
            }
        }
    }

    /**
     * 判断是否为基本类型或包装类型
     *
     * @param type 类对象
     * @return 是否为基本类型或包装类型
     */
    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                   type.equals(String.class) ||
                   type.equals(Integer.class) ||
                   type.equals(Long.class) ||
                   type.equals(Double.class) ||
                   type.equals(Float.class) ||
                   type.equals(Boolean.class) ||
                   type.equals(Character.class) ||
                   type.equals(Byte.class) ||
                   type.equals(Short.class);
    }
}
