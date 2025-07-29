package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.mapper.TActivityMapper;
import com.zs.crmserver.mapper.TActivityRemarkMapper;
import com.zs.crmserver.model.TActivity;
import com.zs.crmserver.query.ActivityQuery;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.service.ActivityService;
import com.zs.crmserver.util.JWTUtils;
import com.zs.crmserver.util.PageHelperUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private TActivityMapper tActivityMapper;

    @Resource
    private TActivityRemarkMapper tActivityRemarkMapper;

    @Override
    public PageInfo<TActivity> getActivitiesByPage(BaseQuery query, BasePageQuery pageQuery, List<Long> ownerIds) {
        return PageHelperUtils.pageQuery(
            pageQuery,
            () -> tActivityMapper.selectActivitiesByPage(query, pageQuery, ownerIds)
        );
    }

    @Override
    public int saveActivity(ActivityQuery activityQuery) {
        TActivity tActivity = new TActivity();
        // 将userQuery中的数据复制到tUser中（要求：两个对象中的属性、属性类型要相同）
        BeanUtils.copyProperties(activityQuery, tActivity);

        // 创建时间
        tActivity.setCreateTime(new Date());
        // 创建人
        Integer loginUserId = JWTUtils.parseUserFromJWT(activityQuery.getToken()).getId();
        tActivity.setCreateBy(loginUserId);

        return tActivityMapper.insertSelective(tActivity);
    }

    @Override
    public int updateActivity(ActivityQuery activityQuery) {
        TActivity tActivity = new TActivity();
        BeanUtils.copyProperties(activityQuery, tActivity);
        tActivity.setEditTime(new Date());
        Integer loginUserId = JWTUtils.parseUserFromJWT(activityQuery.getToken()).getId();
        tActivity.setEditBy(loginUserId);
        return tActivityMapper.updateByPrimaryKeySelective(tActivity);
    }

    @Override
    public TActivity getActivityById(Integer id) {
        return tActivityMapper.selectDetailByPrimaryKey(id);
    }

    @Transactional
    @Override
    public int delActivityById(Integer id) {
        // 先删除备注
        tActivityRemarkMapper.deleteByActivityId(id);
        // 再删除主体活动
        return tActivityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchDelActivities(Integer[] ids) {
        // 先删除所有关联的备注
        tActivityRemarkMapper.batchDeleteByActivityIds(ids);
        // 删除主体活动
        return tActivityMapper.deleteActivities(ids);
    }

    @Override
    public List<TActivity> getOngoingActivity() {
        return tActivityMapper.selectOngoingActivity();
    }
}
