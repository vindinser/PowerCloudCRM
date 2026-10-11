package com.zs.crmserver.web;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TRole;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.RoleService;
import com.zs.crmserver.util.PageResponseUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RoleController {

    @Resource
    private RoleService roleService;

    @PreAuthorize(value = "hasAuthority('role:list')")
    @GetMapping("/api/roles")
    public R getRoles() {
        List<TRole> roles = roleService.getAllRoles();
        return R.OK(roles);
    }

    @PreAuthorize(value = "hasAuthority('role:list')")
    @GetMapping("/api/role/page")
    public R rolePage(
        BasePageQuery query,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        PageInfo<TRole> page = roleService.getRoleByPage(query, keyword);
        return PageResponseUtils.buildPageResponse(page);
    }

    @PreAuthorize(value = "hasAuthority('role:view')")
    @GetMapping("/api/role/{id}")
    public R getRoleDetail(@PathVariable(value = "id") Integer id) {
        TRole role = roleService.getRoleById(id);
        return R.OK(role);
    }

    @PreAuthorize(value = "hasAuthority('role:add')")
    @PostMapping("/api/role/add")
    public R addRole(@Valid @RequestBody TRole role) {
        int save = roleService.saveRole(role);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('role:edit')")
    @PutMapping("/api/role/update")
    public R updateRole(@Valid @RequestBody TRole role) {
        int result = roleService.updateRole(role);
        return result >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('role:delete')")
    @DeleteMapping("/api/role/{id}")
    public R delRole(@PathVariable(value = "id") Integer id) {
        int del = roleService.deleteRoleById(id);
        return del >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('role:delete')")
    @DeleteMapping("/api/role/del")
    public R batchDelRole(@RequestParam(value = "ids") Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return R.FAIL("请选择要删除的数据");
        }
        int batchDel = roleService.batchDeleteRoles(ids);
        return batchDel >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('role:view')")
    @GetMapping("/api/role/{id}/permissions")
    public R getRolePermissions(@PathVariable(value = "id") Integer id) {
        List<Integer> permissionIds = roleService.getPermissionIdsByRoleId(id);
        return R.OK(permissionIds);
    }

    @PreAuthorize(value = "hasAuthority('role:edit')")
    @PostMapping("/api/role/{id}/permissions")
    public R assignPermissions(@PathVariable(value = "id") Integer id, @RequestBody Map<String, List<Integer>> body) {
        List<Integer> permissionIds = body.get("permissionIds");

        if (permissionIds == null) {
            permissionIds = new ArrayList<>();
        }
        int result = roleService.assignPermissions(id, permissionIds);
        // 插入行数与请求数量一致才算成功（清空全部权限时结果为 0 也属成功）
        return result == permissionIds.size() ? R.OK() : R.FAIL();
    }
}
