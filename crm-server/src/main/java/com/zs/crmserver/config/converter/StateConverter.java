package com.zs.crmserver.config.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.zs.crmserver.CrmServerApplication;
import com.zs.crmserver.model.TDicValue;
import com.zs.crmserver.result.DicEnum;

import java.util.List;

public class StateConverter implements Converter<Integer> {

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {

        String cellStateName = cellData.getStringValue();

        List<TDicValue> tDicValueList = (List<TDicValue>) CrmServerApplication.cacheMap.get(DicEnum.STATE.getCode());
        for (TDicValue tDicValue : tDicValueList) {
            Integer id  = tDicValue.getId();
            String name = tDicValue.getTypeValue();

            if (cellStateName.equals(name)) {
                return id;
            }
        }
        return -1;
    }
}
