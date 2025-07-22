package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.mapper.TActivityRemarkMapper;
import com.zs.crmserver.model.TActivityRemark;
import com.zs.crmserver.query.ActivityRemarkQuery;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.service.ActivityRemarkService;
import com.zs.crmserver.util.JWTUtils;
import com.zs.crmserver.util.PageHelperUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Resource
    private TActivityRemarkMapper tActivityRemarkMapper;

    @Override
    public PageInfo<TActivityRemark> getActivityRemarkByPage(ActivityRemarkQuery activityRemarkQuery, BasePageQuery pageQuery) {
        return PageHelperUtils.pageQuery(
            pageQuery,
            () -> tActivityRemarkMapper.selectActivityRemarkByPage(activityRemarkQuery, pageQuery)
        );
    }

    @Override
    public int saveActivityRemark(ActivityRemarkQuery activityRemarkQuery) {
        TActivityRemark tActivityRemark = new TActivityRemark();

        BeanUtils.copyProperties(activityRemarkQuery, tActivityRemark);
        tActivityRemark.setCreateTime(new Date());

        Integer loginUserId = JWTUtils.parseUserFromJWT(activityRemarkQuery.getToken()).getId();
        tActivityRemark.setCreateBy(loginUserId);

        return tActivityRemarkMapper.insertSelective(tActivityRemark);
    }

    @Override
    public int updateActivityRemark(ActivityRemarkQuery activityRemarkQuery) {
        TActivityRemark tActivityRemark = new TActivityRemark();

        BeanUtils.copyProperties(activityRemarkQuery, tActivityRemark);
        tActivityRemark.setEditTime(new Date());

        Integer loginUserId = JWTUtils.parseUserFromJWT(activityRemarkQuery.getToken()).getId();
        tActivityRemark.setEditBy(loginUserId);

        return tActivityRemarkMapper.updateByPrimaryKeySelective(tActivityRemark);
    }

    @Override
    public int delActivityRemarkById(Integer id) {
        //逻辑删除：不删数据，只是修改一下数据的状态，数据依然还在表里面；
        //物理删除：真正的把数据从表里面删掉
        TActivityRemark tActivityRemark = new TActivityRemark();
        tActivityRemark.setId(id);
        tActivityRemark.setDeleted(1); //删除状态（null或者0正常，1删除）
        return tActivityRemarkMapper.updateByPrimaryKeySelective(tActivityRemark);
    }

    @Override
    public int batchDelActivityRemarkById(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        // 转换数组为List（可选步骤，也可直接使用数组）
        List<Integer> idList = Arrays.asList(ids);
        return tActivityRemarkMapper.batchUpdateDeletedStatus(idList, 1);
    }
}
