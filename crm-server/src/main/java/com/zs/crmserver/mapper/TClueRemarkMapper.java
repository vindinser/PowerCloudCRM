package com.zs.crmserver.mapper;

import com.zs.crmserver.model.TClueRemark;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.ClueTrackRecordQuery;

import java.util.List;

public interface TClueRemarkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TClueRemark record);

    int insertSelective(TClueRemark record);

    TClueRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TClueRemark record);

    int updateByPrimaryKey(TClueRemark record);

    List<TClueRemark> selectClueRecordByPage(ClueTrackRecordQuery clueTrackRecordQuery, BasePageQuery basePageQuery);

    int batchUpdateDeletedStatus(Integer[] ids, int deleted);

    void deleteByClueId(Integer clueId);

    void batchDeleteByClueIds(Integer[] ids);
}