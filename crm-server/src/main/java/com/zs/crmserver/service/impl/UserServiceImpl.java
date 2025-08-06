package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.manager.RedisManager;
import com.zs.crmserver.manager.UserManager;
import com.zs.crmserver.mapper.TUserMapper;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.query.DropDownOptionsQuery;
import com.zs.crmserver.query.UserQuery;
import com.zs.crmserver.service.UserService;
import com.zs.crmserver.util.CacheUtils;
import com.zs.crmserver.util.JWTUtils;
import com.zs.crmserver.util.PageHelperUtils;
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

    @Resource
    private RedisManager redisManager;

    @Resource
    private UserManager userManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TUser tUser = tUserMapper.selectByLoginAct(username);

        if(tUser == null){
            throw new UsernameNotFoundException("登录账号不存在");
        }

        return userManager.loadUser(tUser);
    }

    @Override
    public PageInfo<TUser> getUserByPage(BasePageQuery query, String keyword) {
        return PageHelperUtils.pageQuery(
            query,
            () -> tUserMapper.selectUserByPage(BaseQuery.builder().build(), query, keyword)
        );
        // // 1.设置PageHelper
        // PageHelper.startPage(query.getPage(), query.getSize());
        // // 2.查询
        // List<TUser> list = tUserMapper.selectUserByPage(BaseQuery.builder().build(), query, keyword);
        // // 3.封装分页数据到PageInfo
        // PageInfo<TUser> info = new PageInfo<>(list);
        // return info;
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

    @Override
    public List<DropDownOptionsQuery> getOwerList() {
        return CacheUtils.getCacheData(() -> {
            // redis 查询
            return (List<DropDownOptionsQuery>) redisManager.getValue(Constants.REDIS_OWNER_KEY);
        }, () -> {
            // redis 查询不到,从数据库查询
            return tUserMapper.selectByOwner();
        }, (t) -> {
            // 将数据存入redis
            redisManager.setValue(Constants.REDIS_OWNER_KEY, t);
        });
    }
}
