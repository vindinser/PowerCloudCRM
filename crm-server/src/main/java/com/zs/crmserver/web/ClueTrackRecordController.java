package com.zs.crmserver.web;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.model.TClueRemark;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.ClueTrackRecordQuery;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.ClueTrackRecordService;
import com.zs.crmserver.util.PageResponseUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClueTrackRecordController {

    @Resource
    private ClueTrackRecordService clueTrackRecordService;

    @GetMapping("/api/clues/record")
    public R getClueTrackRecords(
        ClueTrackRecordQuery clueTrackRecordQuery,
        BasePageQuery basePageQuery
    ) {
        PageInfo<TClueRemark> list =  clueTrackRecordService.getClueTrackRecords(clueTrackRecordQuery, basePageQuery);
        return PageResponseUtils.buildPageResponse(list);
    }

    @PostMapping("/api/clues/record")
    public R addClueTrackRecord(
        @RequestBody ClueTrackRecordQuery clueTrackRecordQuery,
        @RequestHeader(Constants.TOKEN_NAME) String token
    ) {
        clueTrackRecordQuery.setToken(token);
        int save = clueTrackRecordService.saveClueTrackRecord(clueTrackRecordQuery);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    @PutMapping("/api/clues/record")
    public R updateClueTrackRecord(
        @RequestBody ClueTrackRecordQuery clueTrackRecordQuery,
        @RequestHeader(Constants.TOKEN_NAME) String token
    ) {
        clueTrackRecordQuery.setToken(token);
        int update = clueTrackRecordService.updateClueTrackRecord(clueTrackRecordQuery);
        return update >= 1 ? R.OK() : R.FAIL();
    }

    @DeleteMapping("/api/clues/record/{id}")
    public R delClueTrackRecord(@PathVariable Integer id) {
        int del = clueTrackRecordService.delClueTrackRecord(id);
        return del >= 1 ? R.OK() : R.FAIL();
    }

    @DeleteMapping("/api/clues/record")
    public R batchDelClueTrackRecords(Integer[] ids) {
        int del = clueTrackRecordService.batchDelClueTrackRecords(ids);
        return del >= 1 ? R.OK() : R.FAIL();
    }
}
