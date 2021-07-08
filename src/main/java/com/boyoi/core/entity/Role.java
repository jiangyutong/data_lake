package com.boyoi.core.entity;

import com.boyoi.core.groups.RoleGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 描述：
 * 角色 实体类
 *
 * @author ZhouJL
 * @date 2020-05-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Guid
     */
    @NotBlank(groups = {RoleGroup.Update.class, RoleGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 角色名称
     */
    @NotBlank(groups = {RoleGroup.Add.class, RoleGroup.Update.class})
    @Size(max = 16)
    private String roleName;

    /**
     * 备注说明
     */
    @NotBlank(groups = {RoleGroup.Add.class, RoleGroup.Update.class})
    @Size(max = 128)
    private String describe;

    /**
     * 角色拥有的权限 没有层级
     */
    private List<Permission> permissionList;

    /**
     * 角色拥有的权限 有层级关系
     */
    private List<Permission> routers;


}
