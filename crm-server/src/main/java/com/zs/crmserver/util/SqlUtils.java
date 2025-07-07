package com.zs.crmserver.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * SQL操作安全工具类
 */
public class SqlUtils {

    /**
     * 生成安全的ORDER BY语句
     * @param field 前端传入字段名
     * @param order 排序规则（asc/desc）
     * @return 安全SQL片段
     */
    public static String safeSort(String field, String order, String defaultField, String... allowedFields) {

        // 解析白名单（允许字段）
        List<String> whiteList = Arrays.asList(allowedFields);

        // 校验字段合法性
        String safeField = whiteList.contains(field) ? field : defaultField;

        // 校验排序规则
        String safeOrder = "ASCENDING".equalsIgnoreCase(order) ? "ASC" : "DESC";

        return safeField + " " + safeOrder;
    }
}