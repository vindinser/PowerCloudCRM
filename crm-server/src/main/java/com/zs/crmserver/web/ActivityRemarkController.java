package com.zs.crmserver.web;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.model.TActivityRemark;
import com.zs.crmserver.query.ActivityRemarkQuery;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.ActivityRemarkService;
import com.zs.crmserver.util.PageResponseUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
public class ActivityRemarkController {

    @Resource
    private ActivityRemarkService activityRemarkService;

    @GetMapping("/api/activity/remark")
    public R activityRemarkPage(
        ActivityRemarkQuery activityRemarkQuery,
        BasePageQuery pageQuery
    ) {
        PageInfo<TActivityRemark> activityRemarkList = activityRemarkService.getActivityRemarkByPage(activityRemarkQuery, pageQuery);
        return PageResponseUtils.buildPageResponse(activityRemarkList);
    }

    @PostMapping("/api/activity/remark")
    public R addActivityRemark(
        @RequestBody ActivityRemarkQuery activityRemarkQuery,
        @RequestHeader(Constants.TOKEN_NAME) String token
    ) {
        activityRemarkQuery.setToken(token);
        int save = activityRemarkService.saveActivityRemark(activityRemarkQuery);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    @PutMapping("/api/activity/remark")
    public R updateActivityRemark(
        @RequestBody ActivityRemarkQuery activityRemarkQuery,
        @RequestHeader(Constants.TOKEN_NAME) String token
    ) {
        activityRemarkQuery.setToken(token);
        int update = activityRemarkService.updateActivityRemark(activityRemarkQuery);
        return update >= 1 ? R.OK() : R.FAIL();
    }

    @DeleteMapping("api/activities/remark/{id}")
    public R deleteActivityRemark(@PathVariable(value = "id") Integer id) {
        int del = activityRemarkService.delActivityRemarkById(id);
        return del >= 1 ?  R.OK() : R.FAIL();
    }

    @DeleteMapping("api/activities/remark")
    public R deleteActivityRemark(@RequestParam(value = "ids") Integer[] ids) {
        int batchDel = activityRemarkService.batchDelActivityRemarkById(ids);
        return batchDel >= 1 ?  R.OK() : R.FAIL();
    }
}
