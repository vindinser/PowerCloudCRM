package com.zs.crmserver.service.impl;

import com.zs.crmserver.mapper.TPermissionMapper;
import com.zs.crmserver.mapper.TRolePermissionMapper;
import com.zs.crmserver.model.TPermission;
import com.zs.crmserver.service.PermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private TPermissionMapper tPermissionMapper;

    @Resource
    private TRolePermissionMapper tRolePermissionMapper;

    @Override
    public List<TPermission> getMenuTree() {
        return tPermissionMapper.selectMenuTree();
    }

    @Override
    public List<TPermission> getAllPermissions() {
        return tPermissionMapper.selectAllPermissions();
    }

    @Override
    public TPermission getPermissionById(Integer id) {
        return tPermissionMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int savePermission(TPermission permission) {
        return tPermissionMapper.insertSelective(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePermission(TPermission permission) {
        return tPermissionMapper.updateByPrimaryKeySelective(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePermissionById(Integer id) {
        // 存在子菜单/按钮时不允许删除，避免产生孤儿子节点
        if (tPermissionMapper.countByParentId(id) > 0) {
            throw new RuntimeException("请先删除子菜单或按钮");
        }
        // 清理角色权限关系，避免外键约束失败
        tRolePermissionMapper.deleteByPermissionId(id);
        return tPermissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeletePermissions(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        for (Integer id : ids) {
            if (tPermissionMapper.countByParentId(id) > 0) {
                TPermission permission = tPermissionMapper.selectByPrimaryKey(id);
                String name = permission != null ? permission.getName() : String.valueOf(id);

                throw new RuntimeException("请先删除[" + name + "]的子菜单或按钮");
            }
            tRolePermissionMapper.deleteByPermissionId(id);
        }
        return tPermissionMapper.deleteByIds(ids);
    }
}
