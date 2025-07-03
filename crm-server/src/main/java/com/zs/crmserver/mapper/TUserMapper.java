package com.zs.crmserver.mapper;

import com.zs.crmserver.model.TUser;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    TUser selectByLoginAct(String username);

    List<TUser> selectUserByPage(
        @Param("keyword") String keyword,
        @Param("sortField") String sortField,
        @Param("sortOrder") String sortOrder
    );

    TUser selectDetailById(Integer id);

    int deleteByIds(Integer[] ids);
}