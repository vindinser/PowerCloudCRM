package com.zs.crmserver.service;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TRole;
import com.zs.crmserver.query.BasePageQuery;

import java.util.List;

public interface RoleService {

    /**
     * 分页查询角色列表
     *
     * @param query   分页参数（page/size）
     * @param keyword 搜索关键词（角色标识/角色名称，可选）
     * @return 分页结果
     */
    PageInfo<TRole> getRoleByPage(BasePageQuery query, String keyword);

    /**
     * 查询所有角色（下拉选择用）
     *
     * @return 角色列表
     */
    List<TRole> getAllRoles();

    /**
     * 根据ID查询角色
     *
     * @param id 角色ID
     * @return 角色信息
     */
    TRole getRoleById(Integer id);

    /**
     * 新增角色
     *
     * @param role 角色信息
     * @return 影响行数
     */
    int saveRole(TRole role);

    /**
     * 修改角色
     *
     * @param role 角色信息
     * @return 影响行数
     */
    int updateRole(TRole role);

    /**
     * 删除角色（联动清理角色权限关系、用户角色关系）
     *
     * @param id 角色ID
     * @return 影响行数
     */
    int deleteRoleById(Integer id);

    /**
     * 批量删除角色（联动清理角色权限关系、用户角色关系）
     *
     * @param ids 角色ID数组
     * @return 影响行数
     */
    int batchDeleteRoles(Integer[] ids);

    /**
     * 查询角色已授权的权限ID列表
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Integer> getPermissionIdsByRoleId(Integer roleId);

    /**
     * 分配权限（全量覆盖：先清空原有关联再插入）
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     * @return 实际插入的关系行数
     */
    int assignPermissions(Integer roleId, List<Integer> permissionIds);
}
