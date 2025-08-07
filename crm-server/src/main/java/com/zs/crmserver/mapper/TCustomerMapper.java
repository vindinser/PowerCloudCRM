package com.zs.crmserver.mapper;

import com.zs.crmserver.commons.DataScope;
import com.zs.crmserver.model.TCustomer;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;

import java.util.List;

public interface TCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TCustomer record);

    int insertSelective(TCustomer record);

    TCustomer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TCustomer record);

    int updateByPrimaryKey(TCustomer record);

    @DataScope(tableAlias = "tct", tableField = "create_by")
    List<TCustomer> getCustomersByPage(BaseQuery query, BasePageQuery pageQuery, List<Long> ownerIds);

    List<TCustomer> selectCustomerByExcel(List<String> idList);
}