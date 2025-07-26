package com.zs.crmserver.config.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.zs.crmserver.CrmServerApplication;
import com.zs.crmserver.model.TDicValue;
import com.zs.crmserver.model.TProduct;
import com.zs.crmserver.result.DicEnum;

import java.util.List;

public class IntentionProductConverter implements Converter<Integer> {

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {

        String cellIntentionProductName = cellData.getStringValue();

        List<TProduct> tDicValueList = (List<TProduct>) CrmServerApplication.cacheMap.get(DicEnum.PRODUCT.getCode());
        for (TProduct tProduct : tDicValueList) {
            Integer id  = tProduct.getId();
            String name = tProduct.getName();

            if (cellIntentionProductName.equals(name)) {
                return id;
            }
        }
        return -1;
    }
}
