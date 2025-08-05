package com.zs.crmserver.service;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TCustomer;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.result.CustomerExcel;

import java.util.List;

public interface CustomerService {

    PageInfo<TCustomer> getCustomersByPage(BaseQuery query, BasePageQuery pageQuery, List<Long> ownerIds);

    List<TCustomer> getCustomerByExcel(List<String> idList);
}
