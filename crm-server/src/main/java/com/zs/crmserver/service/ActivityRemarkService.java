package com.zs.crmserver.service;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TActivityRemark;
import com.zs.crmserver.query.ActivityRemarkQuery;
import com.zs.crmserver.query.BasePageQuery;

public interface ActivityRemarkService {

    PageInfo<TActivityRemark> getActivityRemarkByPage(ActivityRemarkQuery activityRemarkQuery, BasePageQuery pageQuery);

    int saveActivityRemark(ActivityRemarkQuery activityRemarkQuery);

    int updateActivityRemark(ActivityRemarkQuery activityRemarkQuery);

    int delActivityRemarkById(Integer id);

    int batchDelActivityRemarkById(Integer[] ids);
}
