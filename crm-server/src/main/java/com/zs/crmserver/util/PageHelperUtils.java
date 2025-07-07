package com.zs.crmserver.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.crmserver.query.BasePageQuery;

import java.util.List;
import java.util.function.Supplier;

public class PageHelperUtils {
    /**
     * 通用分页查询方法
     * @param query      分页参数
     * @param queryLogic 实际查询逻辑
     * @param <T>        返回数据类型
     * @return PageInfo分页对象
     */
    public static <T> PageInfo<T> pageQuery(BasePageQuery query, Supplier<List<T>> queryLogic) {
        // 设置PageHelper
        PageHelper.startPage(query.getPage(), query.getSize());
        // 查询
        List<T> result = queryLogic.get();
        // 封装分页数据到PageInfo
        return new PageInfo<>(result);
    }
}
