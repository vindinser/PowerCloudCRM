package com.zs.crmserver.service;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.UserQuery;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    PageInfo<TUser> getUserByPage(Integer page, Integer size, String keyword, String sortField, String sortOrder);

    TUser getUserById(Integer id);

    int saveUser(UserQuery userQuery);
}
