package com.zs.crmserver.mapper;

import com.zs.crmserver.commons.DataScope;
import com.zs.crmserver.model.TActivity;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TActivityMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TActivity record);

    int insertSelective(TActivity record);

    TActivity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TActivity record);

    int updateByPrimaryKey(TActivity record);

    @DataScope(tableAlias = "ta", tableField = "owner_id")
    List<TActivity> selectActivitiesByPage(BaseQuery query, BasePageQuery pageQuery, List<Long> ownerIds);

    TActivity selectDetailByPrimaryKey(Integer id);

    int deleteActivities(Integer[] ids);
}