package com.zs.crmserver.query;

import com.zs.crmserver.constants.Constants;
import lombok.Data;

/**
 * 分页排序基础参数封装类
 * 所有列表查询接口的公共参数父类
 * Lombok 自动生成Getter/Setter
 */
@Data
public class BasePageQuery {
    /**
     * 当前页码（默认第1页）
     */
    private Integer page = 1;

    /**
     * 每页数据量（默认使用系统常量）
     */
    private Integer size = Constants.DEFAULT_PAGE_SIZE;

    /**
     * 排序字段（需在Mapper中做安全校验）
     */
    private String sortField;

    /**
     * 排序规则：ascending(升序)/descending(降序)
     */
    private String sortOrder;
}