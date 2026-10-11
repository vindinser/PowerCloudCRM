package com.zs.crmserver.service;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.DropDownOptionsQuery;
import com.zs.crmserver.query.UserQuery;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    PageInfo<TUser> getUserByPage(BasePageQuery query, String keyword);

    TUser getUserById(Integer id);

    /**
     * 获取用户详情（包含角色ID列表）
     *
     * @param id 用户ID
     * @return 用户信息（含 roleIds）
     */
    TUser getUserDetailWithRoles(Integer id);

    int saveUser(UserQuery userQuery);

    int updateUser(UserQuery userQuery, Integer loginUserId, Boolean isEditPwd);

    int delUserById(Integer id);

    int batchDelUserByIds(Integer[] ids);

    List<DropDownOptionsQuery> getOwerList();
}
