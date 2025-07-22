package com.zs.crmserver.mapper;

import com.zs.crmserver.commons.DataScope;
import com.zs.crmserver.model.TActivityRemark;
import com.zs.crmserver.query.ActivityRemarkQuery;
import com.zs.crmserver.query.BasePageQuery;

import java.util.List;

public interface TActivityRemarkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TActivityRemark record);

    int insertSelective(TActivityRemark record);

    TActivityRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TActivityRemark record);

    int updateByPrimaryKey(TActivityRemark record);

    @DataScope(tableAlias = "tar", tableField = "create_by")
    List<TActivityRemark> selectActivityRemarkByPage(ActivityRemarkQuery activityRemarkQuery, BasePageQuery pageQuery);

    int batchUpdateDeletedStatus(List<Integer> ids, int deleted);

    void deleteByActivityId(Integer activityId);

    void batchDeleteByActivityIds(Integer[] ids);
}