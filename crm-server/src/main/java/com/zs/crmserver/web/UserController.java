package com.zs.crmserver.web;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.UserQuery;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/api/login/info")
    public R loginInfo(Authentication authentication) {
        TUser tUser = (TUser) authentication.getPrincipal();
        return R.OK(tUser);
    }

    @GetMapping("/api/login/free")
    public R freeLogin() {
        return R.OK();
    }

    @GetMapping("/api/users")
    public R userPage(
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "size", defaultValue = Constants.PAGE_SIZE) Integer size,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "sortField", required = false) String sortField,
        @RequestParam(value = "sortOrder", required = false) String sortOrder
    ) {
        // required = false 表示参数可以传可以不传
        // required = true 表示参数必传（默认为true）
        PageInfo<TUser> userList = userService.getUserByPage(page, size, keyword, sortField, sortOrder);
        return R.OK(userList);
    }

    @GetMapping("/api/user/{id}")
    public R userDetail(@PathVariable(value = "id") Integer id) {
        TUser tUser = userService.getUserById(id);
        return R.OK(tUser);
    }

    @PostMapping("/api/user/add")
    public R addUser(@RequestBody UserQuery userQuery, @RequestHeader("Authorization") String token) {
        // 设置Token
        userQuery.setToken(token);

        int save = userService.saveUser(userQuery);
        return save >= 1 ? R.OK() : R.FAIL();
    }
}
