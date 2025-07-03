package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.crmserver.mapper.TRoleMapper;
import com.zs.crmserver.mapper.TUserMapper;
import com.zs.crmserver.model.TRole;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.query.UserQuery;
import com.zs.crmserver.service.UserService;
import com.zs.crmserver.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TUserMapper tUserMapper;

    // 密码加密器
    @Resource
    private PasswordEncoder passwordEncoder;

    // 角色
    @Resource
    private TRoleMapper tRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TUser tuser = tUserMapper.selectByLoginAct(username);

        if(tuser == null){
            throw new UsernameNotFoundException("登录账号不存在");
        }

        // 查询用户角色
        List<TRole> tRoleList = tRoleMapper.selectByUserId(tuser.getId());

        List<String> stringRoleList = new ArrayList<>();
        tRoleList.forEach(tRole -> {
            stringRoleList.add(tRole.getRole());
        });

        tuser.setRoleList(stringRoleList);

        return tuser;
    }

    @Override
    public PageInfo<TUser> getUserByPage(Integer page, Integer size, String keyword, String sortField, String sortOrder) {
        // 1.设置PageHelper
        PageHelper.startPage(page, size);
        // 2.查询
        List<TUser> list = tUserMapper.selectUserByPage(BaseQuery.builder().build(), keyword, sortField, sortOrder);
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

    @Override
    public int updateUser(UserQuery userQuery, Integer loginUserId, Boolean isEditPwd) {
        TUser tUser = new TUser();
        // 将userQuery中的数据复制到tUser中（要求：两个对象中的属性、属性类型要相同）
        BeanUtils.copyProperties(userQuery,tUser);

        // 是否修改密码
        if(isEditPwd) {
            // 密码加密
            tUser.setLoginPwd(passwordEncoder.encode(userQuery.getLoginPwd()));
        }

        // 编辑时间
        tUser.setEditTime(new Date());
        // 编辑人
        tUser.setEditBy(loginUserId);

        return tUserMapper.updateByPrimaryKeySelective(tUser);
    }

    @Override
    public int delUserById(Integer id) {
        return tUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchDelUserByIds(Integer[] ids) {
        return tUserMapper.deleteByIds(ids);
    }
}
