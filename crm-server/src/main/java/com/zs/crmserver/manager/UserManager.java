package com.zs.crmserver.manager;

import com.zs.crmserver.mapper.TPermissionMapper;
import com.zs.crmserver.mapper.TRoleMapper;
import com.zs.crmserver.model.TPermission;
import com.zs.crmserver.model.TRole;
import com.zs.crmserver.model.TUser;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserManager {

    // 角色
    @Resource
    private TRoleMapper tRoleMapper;

    @Resource
    private TPermissionMapper tPermissionMapper;

    public UserDetails loadUser(TUser tUser) {
        Integer userId = tUser.getId();

        // 查询用户角色
        List<TRole> tRoleList = tRoleMapper.selectByUserId(userId);
        List<String> stringRoleList = new ArrayList<>();
        tRoleList.forEach(tRole -> {
            stringRoleList.add(tRole.getRole());
        });
        tUser.setRoleList(stringRoleList);

        // 菜单权限
        List<TPermission> menuPermissionList = tPermissionMapper.selectMenuPermissionByUserId(userId);
        tUser.setMenuPermissionList(menuPermissionList);

        // 功能权限
        List<TPermission> buttonPermissionList = tPermissionMapper.selectButtonPermissionByUserId(userId);
        List<String> stringPermissionList = new ArrayList<>();
        buttonPermissionList.forEach(tPermission -> {
            stringPermissionList.add(tPermission.getCode()); // 权限标识符
        });
        tUser.setPermissionList(stringPermissionList); // 设置用户的权限标识符

        return tUser;
    }
}
