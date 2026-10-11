package com.zs.crmserver.service.impl;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.mapper.TRoleMapper;
import com.zs.crmserver.mapper.TRolePermissionMapper;
import com.zs.crmserver.mapper.TUserRoleMapper;
import com.zs.crmserver.model.TRole;
import com.zs.crmserver.model.TRolePermission;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.service.RoleService;
import com.zs.crmserver.util.PageHelperUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private TRoleMapper tRoleMapper;

    @Resource
    private TRolePermissionMapper tRolePermissionMapper;

    @Resource
    private TUserRoleMapper tUserRoleMapper;

    @Override
    public PageInfo<TRole> getRoleByPage(BasePageQuery query, String keyword) {
        return PageHelperUtils.pageQuery(query, () -> tRoleMapper.selectRoleByPage(keyword));
    }

    @Override
    public List<TRole> getAllRoles() {
        return tRoleMapper.selectAll();
    }

    @Override
    public TRole getRoleById(Integer id) {
        return tRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveRole(TRole role) {
        return tRoleMapper.insertSelective(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(TRole role) {
        return tRoleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleById(Integer id) {
        // 先清理角色权限关系
        tRolePermissionMapper.deleteByRoleId(id);
        // 再清理用户角色关系
        tUserRoleMapper.deleteByRoleId(id);
        return tRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteRoles(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        for (Integer id : ids) {
            tRolePermissionMapper.deleteByRoleId(id);
            tUserRoleMapper.deleteByRoleId(id);
        }
        return tRoleMapper.deleteByIds(ids);
    }

    @Override
    public List<Integer> getPermissionIdsByRoleId(Integer roleId) {
        return tRolePermissionMapper.selectPermissionIdsByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int assignPermissions(Integer roleId, List<Integer> permissionIds) {
        // 先删除该角色原有的权限关系
        tRolePermissionMapper.deleteByRoleId(roleId);

        // 再批量插入新的权限关系
        if (permissionIds == null || permissionIds.isEmpty()) {
            return 0;
        }
        List<TRolePermission> list = new ArrayList<>();
        permissionIds.forEach(permissionId -> {
            TRolePermission rp = new TRolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionId(permissionId);
            list.add(rp);
        });
        return tRolePermissionMapper.batchInsert(list);
    }
}
