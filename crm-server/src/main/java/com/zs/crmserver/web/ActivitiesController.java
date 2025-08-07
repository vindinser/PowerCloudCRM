package com.zs.crmserver.web;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.model.TActivity;
import com.zs.crmserver.query.ActivityQuery;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.ActivityService;
import com.zs.crmserver.util.PageResponseUtils;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActivitiesController {

    @Resource
    private ActivityService activitiesService;

    @PreAuthorize(value = "hasAuthority('activity:list')")
    @GetMapping("/api/activities")
    public R activitiesPage(
        BaseQuery query,
        BasePageQuery pageQuery,
        @RequestParam(value = "ownerIds", required = false) List<Long> ownerIds
    ) {
        PageInfo<TActivity> userList = activitiesService.getActivitiesByPage(query, pageQuery, ownerIds);
        return PageResponseUtils.buildPageResponse(userList);
    }

    @PreAuthorize(value = "hasAuthority('activity:add')")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/api/activities/add")
    public R addActivity(@RequestBody ActivityQuery activityQuery, @RequestHeader(Constants.TOKEN_NAME) String token) {
        activityQuery.setToken(token);
        int save = activitiesService.saveActivity(activityQuery);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('activity:edit')")
    @Transactional(rollbackFor = Exception.class)
    @PutMapping("/api/activities/update")
    public R updateActivity(@RequestBody ActivityQuery activityQuery, @RequestHeader(Constants.TOKEN_NAME) String token) {
        activityQuery.setToken(token);
        int update = activitiesService.updateActivity(activityQuery);
        return update >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('activity:view')")
    @GetMapping("/api/activities/{id}")
    public R getActivityById(@PathVariable(value = "id") Integer id) {
        TActivity tActivity = activitiesService.getActivityById(id);
        return R.OK(tActivity);
    }

    @PreAuthorize(value = "hasAuthority('activity:delete')")
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("/api/activities/{id}")
    public R delActivity(@PathVariable(value = "id") Integer id) {
        int del = activitiesService.delActivityById(id);
        return del >= 1 ?  R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('activity:delete')")
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("api/activities/del")
    public R batchDelActivities(@RequestParam(value = "ids") Integer[] ids) {
        int batchDel = activitiesService.batchDelActivities(ids);
        return batchDel >= 1 ?  R.OK() : R.FAIL();
    }
}
