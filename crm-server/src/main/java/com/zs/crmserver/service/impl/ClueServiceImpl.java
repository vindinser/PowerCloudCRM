package com.zs.crmserver.service.impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.zs.crmserver.commons.EnableDictConversion;
import com.zs.crmserver.config.listener.UploadDataListener;
import com.zs.crmserver.manager.CustomerManager;
import com.zs.crmserver.mapper.TClueMapper;
import com.zs.crmserver.mapper.TClueRemarkMapper;
import com.zs.crmserver.model.TClue;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.query.ClueQuery;
import com.zs.crmserver.query.CustomerQuery;
import com.zs.crmserver.service.ClueService;
import com.zs.crmserver.util.JWTUtils;
import com.zs.crmserver.util.PageHelperUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {

    @Resource
    private TClueMapper tClueMapper;

    @Resource
    private TClueRemarkMapper tClueRemarkMapper;

    @Resource
    private CustomerManager customerManager;

    @Override
    @EnableDictConversion(EnableDictConversion.ReturnType.PAGE)
    public PageInfo<TClue> getClueByPage(BaseQuery query, BasePageQuery pageQuery, List<Long> ownerIds) {
        return PageHelperUtils.pageQuery(
            pageQuery,
            () -> tClueMapper.selectClueByPage(query, pageQuery, ownerIds)
        );
    }

    @Override
    public Boolean checkPhone(String phone) {
        int count = tClueMapper.selectByCount(phone);
        return count <= 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveClue(ClueQuery clueQuery) {
        TClue tClue = new TClue();
        BeanUtils.copyProperties(clueQuery, tClue);
        Integer loginId = JWTUtils.parseUserFromJWT(clueQuery.getToken()).getId();
        tClue.setCreateBy(loginId);
        tClue.setCreateTime(new Date());
        return tClueMapper.insertSelective(tClue);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateClue(ClueQuery clueQuery) {
        TClue tClue = new TClue();
        BeanUtils.copyProperties(clueQuery, tClue);
        Integer loginId = JWTUtils.parseUserFromJWT(clueQuery.getToken()).getId();
        tClue.setEditBy(loginId);
        tClue.setEditTime(new Date());
        return tClueMapper.updateByPrimaryKeySelective(tClue);
    }

    @Override
    @EnableDictConversion(EnableDictConversion.ReturnType.SINGLE)
    public TClue getClueById(String id) {
        return tClueMapper.selectDetailById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer delClueById(Integer id) {
        // 先删除跟踪记录
        tClueRemarkMapper.deleteByClueId(id);
        return tClueMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer batchDelClues(Integer[] ids) {
        tClueRemarkMapper.batchDeleteByClueIds(ids);
        return tClueMapper.deleteCluesByPrimaryKey(ids);
    }

    @Override
    public void importClue(InputStream inputStream, String token) {
        EasyExcel.read(inputStream, TClue.class, new UploadDataListener(tClueMapper, token))
            .sheet()
            .doRead();
    }

    @Override
    public Boolean transferCustomer(CustomerQuery customerQuery) {
        return customerManager.transferCustomer(customerQuery);
    }
}
