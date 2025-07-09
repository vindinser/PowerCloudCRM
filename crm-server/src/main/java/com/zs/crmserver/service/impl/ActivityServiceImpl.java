package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.mapper.TActivityMapper;
import com.zs.crmserver.model.TActivity;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.service.ActivityService;
import com.zs.crmserver.util.PageHelperUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private TActivityMapper tActivityMapper;

    @Override
    public PageInfo<TActivity> getActivitiesByPage(BaseQuery query, BasePageQuery pageQuery, List<Long> ownerIds) {
        return PageHelperUtils.pageQuery(
            pageQuery,
            () -> tActivityMapper.selectActivitiesByPage(query, pageQuery, ownerIds)
        );
    }
}
