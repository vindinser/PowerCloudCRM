package com.zs.crmserver.service.impl;

import com.zs.crmserver.mapper.TDicTypeMapper;
import com.zs.crmserver.model.TDicType;
import com.zs.crmserver.service.DicTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DicTypeServiceImpl implements DicTypeService {

    @Resource
    private TDicTypeMapper tDicTypeMapper;

    @Override
    public List<TDicType> loadAllDicData() {
        return tDicTypeMapper.selectByAll();
    }
}
