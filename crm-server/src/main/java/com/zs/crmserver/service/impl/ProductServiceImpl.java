package com.zs.crmserver.service.impl;

import com.zs.crmserver.mapper.TProductMapper;
import com.zs.crmserver.model.TProduct;
import com.zs.crmserver.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private TProductMapper tProductMapper;

    @Override
    public List<TProduct> getAllOnSaleProduct() {
        return tProductMapper.selectAllOnSaleProduct();
    }
}
