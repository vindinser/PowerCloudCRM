package com.zs.crmserver.service.impl;

import com.zs.crmserver.mapper.TUserMapper;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
