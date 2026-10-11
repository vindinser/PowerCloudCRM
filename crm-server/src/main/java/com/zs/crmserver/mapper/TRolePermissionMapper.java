package com.zs.crmserver.mapper;

import com.zs.crmserver.model.TRolePermission;

import java.util.List;

public interface TRolePermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TRolePermission record);

    int insertSelective(TRolePermission record);

    TRolePermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TRolePermission record);

    int updateByPrimaryKey(TRolePermission record);

    int deleteByRoleId(Integer roleId);

    int deleteByPermissionId(Integer permissionId);

    int batchInsert(List<TRolePermission> list);

    List<Integer> selectPermissionIdsByRoleId(Integer roleId);
}