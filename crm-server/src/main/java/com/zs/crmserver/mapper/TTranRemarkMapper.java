package com.zs.crmserver.mapper;

import com.zs.crmserver.model.TTranRemark;

public interface TTranRemarkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TTranRemark record);

    int insertSelective(TTranRemark record);

    TTranRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TTranRemark record);

    int updateByPrimaryKey(TTranRemark record);
}