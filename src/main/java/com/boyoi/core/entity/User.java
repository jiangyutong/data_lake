package com.boyoi.core.entity;

import com.boyoi.core.groups.UserGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 用户 实体类
 *
 * @author ZhouJL
 * @date 2020-05-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Guid
     */
    @NotBlank(groups = {UserGroup.Update.class, UserGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 用户名
     */
    @NotBlank(groups = {UserGroup.Add.class, UserGroup.Update.class})
    @Size(max = 32)
    private String userName;

    /**
     * 手机号
     */
    @NotBlank(groups = {UserGroup.Add.class, UserGroup.Update.class})
    @Size(max = 16)
    private String telephone;

    /**
     * 密码
     */
    @Size(max = 64)
    private String password;

    /**
     * 头像
     */
    @Size(max = 2048)
    private String photo;

    /**
     * 简介
     */
    @Size(max = 256)
    private String remark;

    /**
     * 是否启用 0：启用 1：停用
     */
    @Size(max = 5)
    private Integer isEnable;

    /**
     * 是否删除 0:未删除  1:已删除
     */
    @Size(max = 5)
    private Integer isDeleted;

    /**
     * 0：男 1：女
     */
    @NotNull(groups = {UserGroup.Add.class, UserGroup.Update.class})
    private Integer gender;

    /**
     * 0:管理员 1：供应商
     */
    @NotNull(groups = {UserGroup.Add.class, UserGroup.Update.class})
    private Integer type;

    /**
     * 部门ID
     */
    @Size(max = 32)
    private String deptId;

    /**
     * 角色集合
     */
    private List<Role> roles;

    /**
     * 独立权限
     */
    private List<Permission> permissions;

    /**
     * 部门
     */
    private Dept dept;

    /**
     * 用户拥有的权限 有层级关系
     */
    private List<Permission> routers;

    private List<Permission> allPermissions;

    /**
     * 用户拥有的权限 有层级关系 用于前端页面
     */
    private List<Map<String, Object>> routersToPage;
}
