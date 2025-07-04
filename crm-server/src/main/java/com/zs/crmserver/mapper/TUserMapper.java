package com.zs.crmserver.mapper;

import com.zs.crmserver.commons.DataScope;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.BaseQuery;
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

    @DataScope(tableAlias = "tu", tableField = "id")
    List<TUser> selectUserByPage(
        BaseQuery query,
        @Param("keyword") String keyword,
        @Param("sortField") String sortField,
        @Param("sortOrder") String sortOrder
    );

    TUser selectDetailById(Integer id);

    int deleteByIds(Integer[] ids);
}