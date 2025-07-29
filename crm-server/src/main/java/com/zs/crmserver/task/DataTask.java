package com.zs.crmserver.task;

import com.zs.crmserver.CrmServerApplication;
import com.zs.crmserver.model.TActivity;
import com.zs.crmserver.model.TDicType;
import com.zs.crmserver.model.TDicValue;
import com.zs.crmserver.model.TProduct;
import com.zs.crmserver.result.DicEnum;
import com.zs.crmserver.service.ActivityService;
import com.zs.crmserver.service.DicTypeService;
import com.zs.crmserver.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务 存入字典表数据
 */
@EnableScheduling
@Component
public class DataTask {

    @Resource
    private DicTypeService dicTypeService;

    @Resource
    private ProductService productService;

    @Resource
    private ActivityService activityService;

    @Scheduled(fixedDelayString = "${project.task.delay}", zone = "Asia/Shanghai", timeUnit = TimeUnit.MILLISECONDS, initialDelay  = 1000)
    public void task() {
        //定时任务要执行的业务逻辑代码
        System.out.println("定时任务业务逻辑执行......" + new Date());

        List<TDicType> tDicTypeList = dicTypeService.loadAllDicData();
        tDicTypeList.forEach(tDicType -> {
            String typeCode = tDicType.getTypeCode();
            List<TDicValue> tDicValueList = tDicType.getDicValueList();
            CrmServerApplication.cacheMap.put(typeCode, tDicValueList);
        });

        // 查询所有在售产品
        List<TProduct> tProductList = productService.getAllOnSaleProduct();
        CrmServerApplication.cacheMap.put(DicEnum.PRODUCT.getCode(), tProductList);

        //查询所有正在进行的市场活动
        List<TActivity> tActivityList = activityService.getOngoingActivity();
        CrmServerApplication.cacheMap.put(DicEnum.ACTIVITY.getCode(), tActivityList);
    }
}
