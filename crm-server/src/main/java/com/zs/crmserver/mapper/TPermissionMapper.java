package com.zs.crmserver.mapper;

import com.zs.crmserver.model.TPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TPermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TPermission record);

    int insertSelective(TPermission record);

    TPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TPermission record);

    int updateByPrimaryKey(TPermission record);

    List<TPermission> selectMenuPermissionByUserId(Integer id);

    List<TPermission> selectButtonPermissionByUserId(Integer userId);

    List<TPermission> selectMenuTree();

    List<TPermission> selectAllPermissions();

    int countByParentId(Integer parentId);

    int deleteByIds(@Param("ids") Integer[] ids);
}