package com.zs.crmserver.service;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TClueRemark;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.ClueTrackRecordQuery;

public interface ClueTrackRecordService {
    PageInfo<TClueRemark> getClueTrackRecords(ClueTrackRecordQuery clueTrackRecordQuery, BasePageQuery basePageQuery);

    int saveClueTrackRecord(ClueTrackRecordQuery clueTrackRecordQuery);

    int updateClueTrackRecord(ClueTrackRecordQuery clueTrackRecordQuery);

    int delClueTrackRecord(Integer id);

    int batchDelClueTrackRecords(Integer[] ids);
}
