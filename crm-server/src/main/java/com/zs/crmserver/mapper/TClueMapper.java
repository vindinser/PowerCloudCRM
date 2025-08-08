package com.zs.crmserver.mapper;

import com.zs.crmserver.commons.DataScope;
import com.zs.crmserver.model.TClue;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.result.NameValue;

import java.util.List;

public interface TClueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TClue record);

    int insertSelective(TClue record);

    TClue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TClue record);

    int updateByPrimaryKey(TClue record);

    @DataScope(tableAlias = "tc", tableField = "owner_id")
    List<TClue> selectClueByPage(BaseQuery query, BasePageQuery pageQuery, List<Long> ownerIds);

    int selectByCount(String phone);

    TClue selectDetailById(String id);

    Integer deleteCluesByPrimaryKey(Integer[] ids);

    void saveClue(List<TClue> tClueList);

    Integer selectClueByCount();

    List<NameValue> selectBySource();
}