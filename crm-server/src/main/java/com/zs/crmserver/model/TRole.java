package com.zs.crmserver.model;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 角色表
 * t_role
 */
@Data
public class TRole implements Serializable {
    private Integer id;

    @NotBlank(message = "角色标识不能为空")
    private String role;

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    private static final long serialVersionUID = 1L;
}