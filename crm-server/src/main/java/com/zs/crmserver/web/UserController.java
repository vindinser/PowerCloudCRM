package com.zs.crmserver.web;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public R userPage(@RequestParam(value = "current", required = false) Integer current, @RequestParam(value = "size", required = false) Integer size) {
        // required = false 表示参数可以传可以不传
        // required = true 表示参数必传（默认为true）
        if(current==null) {
            current = 1;
        }
        if(size==null) {
            size = Constants.PAGE_SIZE;
        }
        PageInfo<TUser> userList = userService.getUserByPage(current, size);
        return R.OK(userList);
    }
}
