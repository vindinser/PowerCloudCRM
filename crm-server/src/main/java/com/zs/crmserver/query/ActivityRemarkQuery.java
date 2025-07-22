package com.zs.crmserver.query;

import lombok.Data;

@Data
public class ActivityRemarkQuery extends BaseQuery {
    private Integer id;

    private Integer activityId;

    private String noteContent;
}
