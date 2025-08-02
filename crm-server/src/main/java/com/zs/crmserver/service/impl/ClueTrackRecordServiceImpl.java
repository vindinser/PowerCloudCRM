package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.CrmServerApplication;
import com.zs.crmserver.commons.EnableDictConversion;
import com.zs.crmserver.mapper.TClueRemarkMapper;
import com.zs.crmserver.model.TClueRemark;
import com.zs.crmserver.model.TDicValue;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.ClueTrackRecordQuery;
import com.zs.crmserver.service.ClueTrackRecordService;
import com.zs.crmserver.util.JWTUtils;
import com.zs.crmserver.util.PageHelperUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClueTrackRecordServiceImpl implements ClueTrackRecordService {

    @Resource
    private TClueRemarkMapper  tClueRemarkMapper;

    @Override
    @EnableDictConversion(EnableDictConversion.ReturnType.PAGE)
    public PageInfo<TClueRemark> getClueTrackRecords(
        ClueTrackRecordQuery clueTrackRecordQuery,
        BasePageQuery basePageQuery
    ) {
        return PageHelperUtils.pageQuery(
            basePageQuery,
            () -> tClueRemarkMapper.selectClueRecordByPage(clueTrackRecordQuery, basePageQuery)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveClueTrackRecord(ClueTrackRecordQuery clueTrackRecordQuery) {
        TClueRemark tClueRemark = new TClueRemark();
        BeanUtils.copyProperties(clueTrackRecordQuery, tClueRemark);

        Integer loginUserId = JWTUtils.parseUserFromJWT(clueTrackRecordQuery.getToken()).getId();
        tClueRemark.setCreateBy(loginUserId);

        tClueRemark.setCreateTime(new Date());

        return tClueRemarkMapper.insertSelective(tClueRemark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateClueTrackRecord(ClueTrackRecordQuery clueTrackRecordQuery) {
        TClueRemark tClueRemark = new TClueRemark();
        BeanUtils.copyProperties(clueTrackRecordQuery, tClueRemark);

        Integer loginUserId = JWTUtils.parseUserFromJWT(clueTrackRecordQuery.getToken()).getId();
        tClueRemark.setEditBy(loginUserId);

        tClueRemark.setEditTime(new Date());

        return tClueRemarkMapper.updateByPrimaryKeySelective(tClueRemark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delClueTrackRecord(Integer id) {
        TClueRemark tClueRemark = new TClueRemark();
        tClueRemark.setId(id);
        tClueRemark.setDeleted(1);
        return tClueRemarkMapper.updateByPrimaryKeySelective(tClueRemark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelClueTrackRecords(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return tClueRemarkMapper.batchUpdateDeletedStatus(ids, 1);
    }
}
