package com.zs.crmserver.web;

import com.zs.crmserver.model.TPermission;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.PermissionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @PreAuthorize(value = "hasAuthority('permission:list')")
    @GetMapping("/api/permissions/menuTree")
    public R getMenuTree() {
        List<TPermission> menuTree = permissionService.getMenuTree();
        return R.OK(menuTree);
    }

    @PreAuthorize(value = "hasAuthority('permission:list')")
    @GetMapping("/api/permissions")
    public R getAllPermissions() {
        List<TPermission> permissions = permissionService.getAllPermissions();
        return R.OK(permissions);
    }

    @PreAuthorize(value = "hasAuthority('permission:view')")
    @GetMapping("/api/permission/{id}")
    public R getPermissionDetail(@PathVariable(value = "id") Integer id) {
        TPermission permission = permissionService.getPermissionById(id);
        return R.OK(permission);
    }

    @PreAuthorize(value = "hasAuthority('permission:add')")
    @PostMapping("/api/permission/add")
    public R addPermission(@Valid @RequestBody TPermission permission) {
        int save = permissionService.savePermission(permission);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('permission:edit')")
    @PutMapping("/api/permission/update")
    public R updatePermission(@Valid @RequestBody TPermission permission) {
        int result = permissionService.updatePermission(permission);
        return result >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('permission:delete')")
    @DeleteMapping("/api/permission/{id}")
    public R delPermission(@PathVariable(value = "id") Integer id) {
        int del = permissionService.deletePermissionById(id);
        return del >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('permission:delete')")
    @DeleteMapping("/api/permission/del")
    public R batchDelPermission(@RequestParam(value = "ids") Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return R.FAIL("请选择要删除的数据");
        }
        int batchDel = permissionService.batchDeletePermissions(ids);
        return batchDel >= 1 ? R.OK() : R.FAIL();
    }
}
