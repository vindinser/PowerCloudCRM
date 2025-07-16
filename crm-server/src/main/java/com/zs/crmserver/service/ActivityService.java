package com.zs.crmserver.service;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TActivity;
import com.zs.crmserver.query.ActivityQuery;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;

import java.util.List;

public interface ActivityService {
    public PageInfo<TActivity> getActivitiesByPage(BaseQuery query, BasePageQuery pageQuery, List<Long> ownerIds);

    int saveActivity(ActivityQuery activityQuery);

    int updateActivity(ActivityQuery activityQuery);

    TActivity getActivityById(Integer id);

    int delActivityById(Integer id);

    int batchDelActivities(Integer[] ids);
}
