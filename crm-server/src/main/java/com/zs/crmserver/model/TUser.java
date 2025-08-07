package com.zs.crmserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.util.internal.ObjectUtil;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

/**
 * 用户表
 * t_user
 */
@Data
public class TUser implements UserDetails, Serializable {
    /**
     * 主键，自动增长，用户ID
     */
    private Integer id;

    /**
     * 登录账号
     */
    private String loginAct;

    /**
     * 登录密码
     */
    private String loginPwd;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户手机
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 账户是否没有过期，0已过期 1正常
     */
    private Integer accountNoExpired;

    /**
     * 密码是否没有过期，0已过期 1正常
     */
    private Integer credentialsNoExpired;

    /**
     * 账号是否没有锁定，0已锁定 1正常
     */
    private Integer accountNoLocked;

    /**
     * 账号是否启用，0禁用 1启用
     */
    private Integer accountEnabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createBy;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 编辑人
     */
    private Integer editBy;

    /**
     * 最近登录时间
     */
    private Date lastLoginTime;

    /**
     * 角色集合
     */
    private List<String> roleList;

    /**
     * 权限集合
     */
    private List<String> permissionList;

    // 创建人信息（一对一）
    private TUser createInfo;
    private TUser editInfo;

    /**
     * 菜单
     */
    private List<TPermission> menuPermissionList;

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!ObjectUtils.isEmpty(this.getRoleList())) {
            // 角色
            this.getRoleList().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role))
            );
        }
        if (!ObjectUtils.isEmpty(this.getPermissionList())) {
            // 权限
            this.getPermissionList().forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission))
            );
        }
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.getLoginPwd();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.getLoginAct();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        // return UserDetails.super.isAccountNonExpired();
        return this.getAccountNoExpired() == 1;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        // return UserDetails.super.isAccountNonLocked();
        return this.getAccountNoLocked() == 1;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return this.getCredentialsNoExpired() == 1;
        // return UserDetails.super.isCredentialsNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return this.getAccountEnabled() == 1;
        // return UserDetails.super.isEnabled();
    }
}