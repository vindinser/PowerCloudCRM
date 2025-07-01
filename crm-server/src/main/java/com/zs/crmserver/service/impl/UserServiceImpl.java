package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.crmserver.mapper.TUserMapper;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.UserQuery;
import com.zs.crmserver.service.UserService;
import com.zs.crmserver.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TUserMapper tUserMapper;

    // 密码加密器
    @Resource
    private PasswordEncoder passwordEncoder;

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

    @Override
    public int saveUser(UserQuery userQuery) {
        TUser tUser = new TUser();
        // 将userQuery中的数据复制到tUser中（要求：两个对象中的属性、属性类型要相同）
        BeanUtils.copyProperties(userQuery,tUser);
        // 密码加密
        tUser.setLoginPwd(passwordEncoder.encode(userQuery.getLoginPwd()));
        // 创建时间
        tUser.setCreateTime(new Date());
        // 创建人
        Integer loginUserId = JWTUtils.parseUserFromJWT(userQuery.getToken()).getId();
        tUser.setCreateBy(loginUserId);

        return tUserMapper.insertSelective(tUser);
    }
}
