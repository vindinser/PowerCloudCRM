package com.zs.crmserver.service;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.UserQuery;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    PageInfo<TUser> getUserByPage(BasePageQuery query, String keyword);

    TUser getUserById(Integer id);

    int saveUser(UserQuery userQuery);

    int updateUser(UserQuery userQuery, Integer loginUserId, Boolean isEditPwd);

    int delUserById(Integer id);

    int batchDelUserByIds(Integer[] ids);
}
