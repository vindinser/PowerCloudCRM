package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.commons.EnableDictConversion;
import com.zs.crmserver.mapper.TCustomerMapper;
import com.zs.crmserver.model.TCustomer;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.result.CustomerExcel;
import com.zs.crmserver.service.CustomerService;
import com.zs.crmserver.util.PageHelperUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private TCustomerMapper tCustomerMapper;

    @Override
    @EnableDictConversion(EnableDictConversion.ReturnType.PAGE)
    public PageInfo<TCustomer> getCustomersByPage(BaseQuery query, BasePageQuery pageQuery, List<Long> ownerIds) {
        return PageHelperUtils.pageQuery(
            pageQuery,
            () -> tCustomerMapper.getCustomersByPage(query, pageQuery, ownerIds)
        );
    }

    @Override
    @EnableDictConversion
    public List<TCustomer> getCustomerByExcel(List<String> idList) {
        return tCustomerMapper.selectCustomerByExcel(idList);
    }
}
