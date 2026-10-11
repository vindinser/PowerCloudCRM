package com.zs.crmserver.mapper;

import com.zs.crmserver.model.TRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TRole record);

    int insertSelective(TRole record);

    TRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TRole record);

    int updateByPrimaryKey(TRole record);

    List<TRole> selectByUserId(Integer userId);

    List<TRole> selectAll();

    List<TRole> selectRoleByPage(@Param("keyword") String keyword);

    int deleteByIds(@Param("ids") Integer[] ids);
}