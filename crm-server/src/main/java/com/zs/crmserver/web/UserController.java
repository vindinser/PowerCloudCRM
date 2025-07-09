package com.zs.crmserver.web;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.UserQuery;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.RedisService;
import com.zs.crmserver.service.UserService;
import com.zs.crmserver.util.JWTUtils;
import com.zs.crmserver.util.PageResponseUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

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
        BasePageQuery query,
        @RequestParam(value = "keyword", required = false) String keyword
        // @RequestParam(value = "page", defaultValue = "1") Integer page,
        // @RequestParam(value = "size", defaultValue = Constants.PAGE_SIZE) Integer size,
        // @RequestParam(value = "sortField", required = false) String sortField,
        // @RequestParam(value = "sortOrder", required = false) String sortOrder
    ) {
        // required = false 表示参数可以传可以不传
        // required = true 表示参数必传（默认为true）
        PageInfo<TUser> userList = userService.getUserByPage(query, keyword);

        // return R.OK(userList);
        return PageResponseUtils.buildPageResponse(userList);
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

    @PutMapping("/api/user/update")
    public R updateUser(@RequestBody UserQuery userQuery, @RequestHeader("Authorization") String token) {
        // 设置Token
        userQuery.setToken(token);

        // 当前登录用户ID
        Integer loginUserId = JWTUtils.parseUserFromJWT(userQuery.getToken()).getId();
        // 是否修改密码
        boolean isEditPwd = StringUtils.hasText(userQuery.getLoginPwd());

        int result = userService.updateUser(userQuery, loginUserId, isEditPwd);
        if(result >= 1) {
            // 操作成功，判断是否修改了自己的密码
            if(loginUserId.equals(userQuery.getId()) && isEditPwd) {
                redisService.removeValue(Constants.REDIS_JWT_KEY + loginUserId);
                Map<String, Boolean> data = new HashMap<>();
                data.put("requireReLogin", true);
                return R.OK(data);
            }
            return R.OK();
        } else {
            return R.FAIL();
        }
    }

    @DeleteMapping("/api/user/{id}")
    public R delUser(@PathVariable(value = "id") Integer id) {
        int del = userService.delUserById(id);
        return del >= 1 ? R.OK() : R.FAIL();
    }

    @DeleteMapping("/api/user/del")
    public R batchDelUser(@RequestParam(value = "ids") Integer[] ids) {
        int batchDel = userService.batchDelUserByIds(ids);
        return batchDel >= 1 ? R.OK() : R.FAIL();
    }

    @GetMapping("/api/owers")
    public R getOwerList() {
        List<TUser> owerList = userService.getOwerList();
        return R.OK(owerList);
    }
}
