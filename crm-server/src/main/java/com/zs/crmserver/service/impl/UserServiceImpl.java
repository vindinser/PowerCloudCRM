package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.crmserver.mapper.TUserMapper;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  @Resource
  private TUserMapper tUserMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    TUser tuser = tUserMapper.selectByLoginAct(username);

    if(tuser == null){
      throw new UsernameNotFoundException("登录账号不存在");
    }

    return tuser;
  }

    @Override
    public PageInfo<TUser> getUserByPage(Integer page, Integer size, String keyword, String sortField, String sortOrder) {
        // 1.设置PageHelper
        PageHelper.startPage(page, size);
        // 2.查询 BaseQuery.builder().build()
        List<TUser> list = tUserMapper.selectUserByPage(keyword, sortField, sortOrder);
        // 3.封装分页数据到PageInfo
        PageInfo<TUser> info = new PageInfo<>(list);
        return info;
    }

    @Override
    public TUser getUserById(Integer id) {
        // TUser tUser = tUserMapper.selectByPrimaryKey(id);
        TUser tUser = tUserMapper.selectDetailById(id);
        return tUser;
    }
}
