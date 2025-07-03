package com.zs.crmserver.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseQuery {
    // jwt
    public String token;

    // 数据权限过滤条件
    public String filterSQL;
}
