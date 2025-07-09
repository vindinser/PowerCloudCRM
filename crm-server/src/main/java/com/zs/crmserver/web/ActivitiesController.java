package com.zs.crmserver.web;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TActivity;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.ActivityService;
import com.zs.crmserver.util.PageResponseUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class ActivitiesController {

    @Resource
    private ActivityService activitiesService;

    @GetMapping("/api/activities")
    public R activitiesPage(
        BaseQuery query,
        BasePageQuery pageQuery,
        @RequestParam(value = "ownerIds", required = false) List<Long> ownerIds
    ) {
        PageInfo<TActivity> userList = activitiesService.getActivitiesByPage(query, pageQuery, ownerIds);
        return PageResponseUtils.buildPageResponse(userList);
    }
}
