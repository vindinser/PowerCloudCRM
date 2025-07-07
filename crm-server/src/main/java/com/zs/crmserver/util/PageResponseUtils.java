package com.zs.crmserver.util;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.result.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class PageResponseUtils {
    /**
     * 构建标准分页响应
     * @param pageInfo 分页查询结果
     * @param <T>      数据类型
     * @return 格式化后的响应对象
     */
    public static <T> R buildPageResponse(PageInfo<T> pageInfo) {
        // 1. 构建分页元数据
        Map<String, Object> pageMeta = buildPageMeta(pageInfo);

        // 2. 创建响应对象
        return R.OK(pageMeta, pageInfo.getList());
    }

    /**
     * 构建分页元数据（可扩展）
     * @param pageInfo 分页数据
     * @return 分页元数据Map
     */
    private static Map<String, Object> buildPageMeta(PageInfo<?> pageInfo) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("total", pageInfo.getTotal());      // 总记录数
        meta.put("pages", pageInfo.getPages());      // 总页数
        meta.put("pageNum", pageInfo.getPageNum());  // 当前页码
        meta.put("pageSize", pageInfo.getPageSize());// 每页条数
        meta.put("hasNext", pageInfo.isHasNextPage());// 是否有下一页
        return meta;
    }
}
