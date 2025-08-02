package com.zs.crmserver.util;

import com.zs.crmserver.commons.UserConvert;
import com.zs.crmserver.query.DropDownOptionsQuery;
import com.zs.crmserver.service.UserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 用户信息转换工具类
 *
 * 负责处理实体类中标记了@UserConvert注解的字段转换
 */
@Component
public class UserConversionUtil {
    private static final Logger log = LoggerFactory.getLogger(UserConversionUtil.class);

    // 用户缓存：本地缓存用户ID到用户名的映射
    private static final Map<String, Map<String, String>> userCache = new ConcurrentHashMap<>();

    // 用户转换缓存：缓存用户转换规则
    private static final Map<Class<?>, Map<String, UserConvertReflectionInfo>> userConvertCache = new ConcurrentHashMap<>();

    private static UserService userService; // 用户服务

    /**
     * 构造函数注入 UserService
     */
    @Autowired
    public UserConversionUtil(UserService userService) {
        this.userService = userService;
    }

    /**
     * 处理单个实体的用户信息转换
     *
     * @param entity 需要转换的实体对象
     */
    public static void convertEntity(Object entity) {
        if (entity == null) return;

        Class<?> entityClass = entity.getClass();

        // 获取缓存的用户转换信息
        Map<String, UserConvertReflectionInfo> userConvertInfos = getUserConvertInfos(entityClass);

        userConvertInfos.forEach((fieldName, info) -> {
            try {
                // 获取用户ID值
                Object userIdValue = info.userIdField.get(entity);
                if (userIdValue == null) return;

                // 获取用户名
                String userName = getRedisUserName(info.dicType, userIdValue.toString());

                // 设置用户名字段值
                info.userNameField.set(entity, userName);
            } catch (Exception e) {
                log.warn("用户信息转换失败|field={}", info.userIdField.getName(), e);
            }
        });
    }

    /**
     * 获取或构建用户转换反射信息
     *
     * @param entityClass 实体类Class对象
     * @return 用户转换信息映射
     */
    private static Map<String, UserConvertReflectionInfo> getUserConvertInfos(Class<?> entityClass) {
        return userConvertCache.computeIfAbsent(entityClass, clazz -> {
            Map<String, UserConvertReflectionInfo> infos = new HashMap<>();

            // 遍历所有字段查找@UserConvert注解
            for (Field field : clazz.getDeclaredFields()) {
                UserConvert annotation = field.getAnnotation(UserConvert.class);
                if (annotation == null) continue;

                try {
                    // 获取源字段和目标字段
                    Field userIdField = field;
                    userIdField.setAccessible(true);

                    Field userNameField = DictConversionUtil.getField(clazz, annotation.userNameField());
                    userNameField.setAccessible(true);

                    // 创建反射信息对象
                    infos.put(field.getName(), new UserConvertReflectionInfo(
                        "user", // dicType 固定为"user"
                        userIdField,
                        userNameField
                    ));
                } catch (Exception e) {
                    log.error("UserConvert解析失败|class={}|field={}",
                        clazz.getSimpleName(), field.getName(), e);
                }
            }

            return infos;
        });
    }

    /**
     * 从Redis获取用户名
     *
     * @param dicType 字典类型（固定为"user"）
     * @param userId 用户ID
     * @return 用户名
     */
    public static String getRedisUserName(String dicType, String userId) {
        // 从本地缓存获取
        Map<String, String> cacheMap = userCache.get(dicType);
        if (cacheMap != null && cacheMap.containsKey(userId)) {
            return cacheMap.get(userId);
        }

        // 从Redis获取用户列表
        List<DropDownOptionsQuery> userList = userService.getOwerList();
        if (userList == null || userList.isEmpty()) {
            return "未知";
        }

        // 构建用户ID到用户名的映射
        Map<String, String> newCache = userList.stream()
                                           .filter(user -> user.getValue() != null && user.getLabel() != null)
                                           .collect(Collectors.toMap(
                                               user -> user.getValue().toString(),
                                               DropDownOptionsQuery::getLabel,
                                               (oldVal, newVal) -> oldVal
                                           ));

        // 更新本地缓存
        userCache.put(dicType, newCache);

        return newCache.getOrDefault(userId, "未知");
    }

    /**
     * 用户转换反射信息缓存对象
     */
    private static class UserConvertReflectionInfo {
        final String dicType;
        final Field userIdField;
        final Field userNameField;

        UserConvertReflectionInfo(String dicType, Field userIdField, Field userNameField) {
            this.dicType = dicType;
            this.userIdField = userIdField;
            this.userNameField = userNameField;
        }
    }
}
