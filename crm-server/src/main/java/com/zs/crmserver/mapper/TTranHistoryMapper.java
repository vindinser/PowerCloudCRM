package com.zs.crmserver.mapper;

import com.zs.crmserver.model.TTranHistory;

public interface TTranHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TTranHistory record);

    int insertSelective(TTranHistory record);

    TTranHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TTranHistory record);

    int updateByPrimaryKey(TTranHistory record);
}