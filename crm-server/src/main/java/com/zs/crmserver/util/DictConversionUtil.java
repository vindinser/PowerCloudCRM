package com.zs.crmserver.util;

import com.zs.crmserver.CrmServerApplication;
import com.zs.crmserver.commons.DictConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字典转换工具类
 *
 * 负责处理实体类中标记了@DictConvert注解的字段转换
 */
public class DictConversionUtil {
    private static final Logger log = LoggerFactory.getLogger(DictConversionUtil.class);

    // 缓存字段反射信息，避免重复查找
    private static final Map<Class<?>, Map<String, DictReflectionInfo>> dictReflectionCache = new ConcurrentHashMap<>();

    /**
     * 处理单个实体的字典转换
     *
     * @param entity 需要转换的实体对象
     */
    public static void convertEntity(Object entity) {
        if (entity == null) return;

        Class<?> entityClass = entity.getClass();
        Map<String, DictReflectionInfo> dictInfos = getDictReflectionInfos(entityClass);

        dictInfos.forEach((fieldName, info) -> {
            try {
                // 获取字段值
                Object idValue = info.sourceField.get(entity);
                if (idValue == null) return;

                String nameValue;

                // 根据数据源类型获取名称
                switch (info.sourceType) {
                    case LOCAL_CACHE:
                        nameValue = getLocalCacheName(info.dicType, idValue, info.idFieldName, info.nameFieldName);
                        break;
                    case REDIS_USER:
                        nameValue = UserConversionUtil.getRedisUserName(info.dicType, idValue.toString());
                        break;
                    default:
                        nameValue = "未知";
                }

                // 设置目标字段值
                info.targetField.set(entity, nameValue);
            } catch (Exception e) {
                log.warn("字典转换失败|field={}|dicType={}",
                    fieldName, info.dicType, e);
            }
        });
    }

    /**
     * 获取或构建字典字段反射信息
     *
     * @param entityClass 实体类Class对象
     * @return 字典字段反射信息映射
     */
    private static Map<String, DictReflectionInfo> getDictReflectionInfos(Class<?> entityClass) {
        return dictReflectionCache.computeIfAbsent(entityClass, clazz -> {
            Map<String, DictReflectionInfo> infos = new HashMap<>();

            // 遍历所有字段查找@DictConvert注解
            for (Field field : clazz.getDeclaredFields()) {
                DictConvert annotation = field.getAnnotation(DictConvert.class);
                if (annotation == null) continue;

                try {
                    // 获取源字段和目标字段
                    Field sourceField = field;
                    sourceField.setAccessible(true);

                    Field targetField = getField(clazz, annotation.targetField());
                    targetField.setAccessible(true);

                    // 创建反射信息对象
                    infos.put(field.getName(), new DictReflectionInfo(
                        annotation.dicType(),
                        sourceField,
                        targetField,
                        annotation.idField(),
                        annotation.nameField(),
                        annotation.source()
                    ));
                } catch (Exception e) {
                    log.error("DictConvert解析失败|class={}|field={}",
                        clazz.getSimpleName(), field.getName(), e);
                }
            }

            return infos;
        });
    }

    /**
     * 从本地缓存获取名称
     *
     * @param dicType 字典类型
     * @param idValue ID值
     * @param idFieldName ID字段名
     * @param nameFieldName 名称字段名
     * @return 对应的名称值
     */
    private static String getLocalCacheName(String dicType, Object idValue,
                                            String idFieldName, String nameFieldName) {
        // 从全局缓存获取原始字典列表
        List<?> dictList = getDictListFromCache(dicType);
        if (dictList == null || dictList.isEmpty()) {
            return "未知";
        }

        // 在列表中查找匹配项
        try {
            for (Object dictItem : dictList) {
                Field idField = getField(dictItem.getClass(), idFieldName);
                idField.setAccessible(true);
                Object itemIdValue = idField.get(dictItem);

                if (idValue.equals(itemIdValue)) {
                    Field nameField = getField(dictItem.getClass(), nameFieldName);
                    nameField.setAccessible(true);
                    Object nameValue = nameField.get(dictItem);
                    return nameValue != null ? nameValue.toString() : "未知";
                }
            }
        } catch (Exception e) {
            log.warn("字典项解析失败|id={}", idValue, e);
        }
        return "未知";
    }

    /**
     * 从全局缓存获取字典列表
     *
     * @param dicType 字典类型
     * @return 字典项列表
     */
    private static List<?> getDictListFromCache(String dicType) {
        Object cacheObj = CrmServerApplication.cacheMap.get(dicType);
        return (cacheObj instanceof List) ? (List<?>) cacheObj : null;
    }

    /**
     * 递归查找字段（处理继承关系）
     *
     * @param clazz 类对象
     * @param fieldName 字段名
     * @return 字段对象
     * @throws NoSuchFieldException 如果字段不存在
     */
    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getField(superClass, fieldName);
            }
            throw e;
        }
    }

    /**
     * 字典字段反射信息缓存对象
     */
    private static class DictReflectionInfo {
        final String dicType;
        final Field sourceField;
        final Field targetField;
        final String idFieldName;
        final String nameFieldName;
        final DictConvert.SourceType sourceType;

        DictReflectionInfo(String dicType, Field sourceField, Field targetField,
                           String idFieldName, String nameFieldName,
                           DictConvert.SourceType sourceType) {
            this.dicType = dicType;
            this.sourceField = sourceField;
            this.targetField = targetField;
            this.idFieldName = idFieldName;
            this.nameFieldName = nameFieldName;
            this.sourceType = sourceType;
        }
    }
}
