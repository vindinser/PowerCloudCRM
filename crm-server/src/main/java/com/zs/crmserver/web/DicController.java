package com.zs.crmserver.web;

import com.zs.crmserver.CrmServerApplication;
import com.zs.crmserver.model.TActivity;
import com.zs.crmserver.model.TDicValue;
import com.zs.crmserver.model.TProduct;
import com.zs.crmserver.result.R;
import com.zs.crmserver.result.DicEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DicController {

    @GetMapping(value = "/api/dicvalue/{typeCode}")
    public R dicData(@PathVariable String typeCode) {
        if (typeCode.equals(DicEnum.ACTIVITY.getCode())) { //activity
            List<TActivity> tActivityList = (List<TActivity>) CrmServerApplication.cacheMap.get(typeCode);
            return R.OK(tActivityList);
        } else if (typeCode.equals(DicEnum.PRODUCT.getCode())) {
            List<TProduct> tProductList = (List<TProduct>) CrmServerApplication.cacheMap.get(typeCode);
            return R.OK(tProductList);
        } else {
            List<TDicValue> tDicValueList = (List<TDicValue>) CrmServerApplication.cacheMap.get(typeCode);
            return R.OK(tDicValueList);
        }
    }
}
