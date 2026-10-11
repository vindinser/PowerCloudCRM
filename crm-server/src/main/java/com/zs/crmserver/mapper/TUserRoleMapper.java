package com.zs.crmserver.mapper;

import com.zs.crmserver.model.TUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TUserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUserRole record);

    int insertSelective(TUserRole record);

    TUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUserRole record);

    int updateByPrimaryKey(TUserRole record);

    int deleteByUserId(Integer userId);

    int deleteByRoleId(Integer roleId);

    int deleteByUserIds(@Param("userIds") Integer[] userIds);

    int batchInsert(List<TUserRole> list);

    List<Integer> selectRoleIdsByUserId(Integer userId);

    List<TUserRole> selectRoleIdsByUserIds(@Param("userIds") Integer[] userIds);
}