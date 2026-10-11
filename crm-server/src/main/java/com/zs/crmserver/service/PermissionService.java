package com.zs.crmserver.service;

import com.zs.crmserver.model.TPermission;

import java.util.List;

public interface PermissionService {

    /**
     * 查询菜单树（一级菜单 + 子菜单/按钮，用于权限分配）
     *
     * @return 菜单树
     */
    List<TPermission> getMenuTree();

    /**
     * 查询所有权限（菜单 + 按钮）
     *
     * @return 权限列表
     */
    List<TPermission> getAllPermissions();

    /**
     * 根据ID查询权限
     *
     * @param id 权限ID
     * @return 权限信息
     */
    TPermission getPermissionById(Integer id);

    /**
     * 新增权限
     *
     * @param permission 权限信息
     * @return 影响行数
     */
    int savePermission(TPermission permission);

    /**
     * 修改权限
     *
     * @param permission 权限信息
     * @return 影响行数
     */
    int updatePermission(TPermission permission);

    /**
     * 删除权限（联动清理角色权限关系，存在子节点时拒绝删除）
     *
     * @param id 权限ID
     * @return 影响行数
     */
    int deletePermissionById(Integer id);

    /**
     * 批量删除权限（联动清理角色权限关系，存在子节点时拒绝删除）
     *
     * @param ids 权限ID数组
     * @return 影响行数
     */
    int batchDeletePermissions(Integer[] ids);
}
