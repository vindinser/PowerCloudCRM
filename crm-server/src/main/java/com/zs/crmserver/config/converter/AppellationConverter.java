package com.zs.crmserver.config.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.zs.crmserver.CrmServerApplication;
import com.zs.crmserver.model.TDicValue;
import com.zs.crmserver.result.DicEnum;

import java.util.List;

/**
 * 称呼转换器
 */
public class AppellationConverter implements Converter<Integer> {

    /**
     * 把excel数据转java
     * @param cellData excel中读取到的数据 先生/女士
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String cellAppellationName = cellData.getStringValue();
        List<TDicValue> tDicValueList = (List<TDicValue>)CrmServerApplication.cacheMap.get(DicEnum.APPELLATION.getCode());
        for (TDicValue tDicValue : tDicValueList) {
            Integer id  = tDicValue.getId();
            String name = tDicValue.getTypeValue();

            if (cellAppellationName.equals(name)) {
                return id;
            }
        }
        return -1;
    }
}
