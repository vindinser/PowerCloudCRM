package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.manager.RedisManager;
import com.zs.crmserver.manager.UserManager;
import com.zs.crmserver.mapper.TUserMapper;
import com.zs.crmserver.mapper.TUserRoleMapper;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.model.TUserRole;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private TUserRoleMapper tUserRoleMapper;

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
        PageInfo<TUser> pageInfo = PageHelperUtils.pageQuery(
            query,
            () -> tUserMapper.selectUserByPage(BaseQuery.builder().build(), query, keyword)
        );
        // 回填角色ID列表（供用户编辑时回显，避免保存时误清空角色）
        List<TUser> userList = pageInfo.getList();

        if (!userList.isEmpty()) {
            Integer[] userIds = userList.stream().map(TUser::getId).toArray(Integer[]::new);
            List<TUserRole> userRoles = tUserRoleMapper.selectRoleIdsByUserIds(userIds);
            Map<Integer, List<Integer>> roleIdMap = userRoles.stream()
                .collect(Collectors.groupingBy(
                    TUserRole::getUserId,
                    Collectors.mapping(TUserRole::getRoleId, Collectors.toList())
                ));
            userList.forEach(user ->
                user.setRoleIds(roleIdMap.getOrDefault(user.getId(), new ArrayList<>())));
        }
        return pageInfo;
    }

    @Override
    public TUser getUserById(Integer id) {
        // TUser tUser = tUserMapper.selectByPrimaryKey(id);
        TUser tUser = tUserMapper.selectDetailById(id);
        return tUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

        int result = tUserMapper.insertSelective(tUser);

        // 绑定角色
        if (result >= 1 && userQuery.getRoleIds() != null && !userQuery.getRoleIds().isEmpty()) {
            saveUserRoles(tUser.getId(), userQuery.getRoleIds());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

        int result = tUserMapper.updateByPrimaryKeySelective(tUser);

        // 更新角色绑定
        if (result >= 1 && userQuery.getRoleIds() != null) {
            // 先删除原有角色关系
            tUserRoleMapper.deleteByUserId(userQuery.getId());
            // 再绑定新角色
            if (!userQuery.getRoleIds().isEmpty()) {
                saveUserRoles(userQuery.getId(), userQuery.getRoleIds());
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delUserById(Integer id) {
        // 先清理用户角色关系，避免外键约束失败
        tUserRoleMapper.deleteByUserId(id);
        return tUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelUserByIds(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        // 先批量清理用户角色关系，避免外键约束失败
        tUserRoleMapper.deleteByUserIds(ids);
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

    /**
     * 保存用户角色关系
     */
    private void saveUserRoles(Integer userId, List<Integer> roleIds) {
        List<TUserRole> list = new ArrayList<>();
        roleIds.forEach(roleId -> {
            TUserRole userRole = new TUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            list.add(userRole);
        });
        tUserRoleMapper.batchInsert(list);
    }

    /**
     * 获取用户详情（包含角色ID列表）
     */
    @Override
    public TUser getUserDetailWithRoles(Integer id) {
        TUser tUser = tUserMapper.selectDetailById(id);
        if (tUser != null) {
            List<Integer> roleIds = tUserRoleMapper.selectRoleIdsByUserId(id);
            tUser.setRoleIds(roleIds);
        }
        return tUser;
    }
}
