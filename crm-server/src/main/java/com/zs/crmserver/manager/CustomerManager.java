package com.zs.crmserver.manager;

import com.zs.crmserver.mapper.TClueMapper;
import com.zs.crmserver.mapper.TCustomerMapper;
import com.zs.crmserver.model.TClue;
import com.zs.crmserver.model.TCustomer;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.CustomerQuery;
import com.zs.crmserver.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class CustomerManager {

    @Resource
    private TClueMapper tClueMapper;

    @Resource
    private TCustomerMapper tCustomerMapper;

    @Transactional(rollbackFor = Exception.class)
    public Boolean transferCustomer(CustomerQuery customerQuery) {

        Integer clueId = customerQuery.getClueId();

        // 判断该转客户的线索是否已经转过客户
        TClue tClue = tClueMapper.selectByPrimaryKey(clueId);
        if(tClue.getState() == -1) {
            throw new RuntimeException("已转客户，请勿频繁操作！");
        }

        // 向客户表插入线索
        TCustomer tCustomer = new TCustomer();
        BeanUtils.copyProperties(customerQuery, tCustomer);
        Integer loginId = JWTUtils.parseUserFromJWT(customerQuery.getToken()).getId();
        tCustomer.setCreateBy(loginId);
        tCustomer.setCreateTime(new Date());
        int insert = tCustomerMapper.insertSelective(tCustomer);

        // 修改线索表转客户状态
        TClue clue = new TClue();
        clue.setId(clueId);
        clue.setState(-1);
        int update = tClueMapper.updateByPrimaryKeySelective(clue);

        return insert >= 1 && update >= 1;
    }
}
