package com.zs.crmserver.query;

import lombok.Data;

@Data
public class ClueTrackRecordQuery extends BaseQuery {

    private Integer id;

    private Integer clueId;

    private String noteContent;

    private Integer noteWay;
}
