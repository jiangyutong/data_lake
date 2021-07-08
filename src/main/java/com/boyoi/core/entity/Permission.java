package com.boyoi.core.entity;

import com.boyoi.core.groups.PermissionGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 描述：
 * 权限 实体类
 *
 * @author ZhouJL
 * @date 2019-04-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Permission extends BaseEntity {

    private static final long serialVersionUID = -754438157263035198L;

    /**
     * guid
     */
    @NotBlank(groups = {PermissionGroup.Update.class, PermissionGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 上级权限id
     */
    @Size(max = 32)
    private String parentId;

    /**
     * 权限名称
     */
    @NotBlank(groups = {PermissionGroup.Add.class, PermissionGroup.Update.class})
    @Size(max = 50)
    private String name;

    /**
     * 权限唯一键
     */
    @NotBlank(groups = {PermissionGroup.Add.class, PermissionGroup.Update.class})
    @Size(max = 500)
    private String key;

    /**
     * 排序
     */
    @NotNull(groups = {PermissionGroup.Add.class, PermissionGroup.Update.class})
    @Size(max = 10)
    private Integer sort;

    /**
     * 图标
     */
    @Size(max = 255)
    private String icon;

    /**
     * 权限类型  0：路由权限  1：指令权限
     */
    @Size(max = 10)
    private String type;

    /**
     * 指令集合
     */
    List<Permission> instructions;

    /**
     * 下级菜单集合
     */
    List<Permission> children;

}
