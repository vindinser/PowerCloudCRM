package com.zs.crmserver.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseQuery {
    // jwt
    public String token;

    // 数据权限过滤条件
    public String filterSQL;

    private String keyword;

    /**
     * 前端传过来的是一个格式为：YYYY-MM-DD HH:mm:ss 的字符串日期，后台接收要把字符串的日期转成java.util.Date日期，需要使用@DateTimeFormat注解
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
