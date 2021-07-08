package com.boyoi.base.dao;

import com.boyoi.core.dao.BaseDao;
import com.boyoi.core.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述：
 * 权限  Dao层
 *
 * @Author ZhouJL
 * @Date 2019-04-26
 */
@Mapper
public interface PermissionDao extends BaseDao {

    /**
     * 查询角色拥有的权限
     *
     * @param roleId 角色id
     * @return 权限集合
     */
    List<Permission> findByRole(String roleId);

    /**
     * 根据角色获取权限
     *
     * @param guid 角色ID
     * @return 权限指令集合
     */
    List<Permission> findToRouter(String guid);

    /**
     * 查询所有一级菜单
     *
     * @return 所有一级菜单
     */
    @Select("select * from tsys_permission where parent_id is null or parent_id='' order by sort")
    List<Permission> allParent();

    /**
     * 根据用户查询独立权限
     *
     * @param guid 用户ID
     * @return 权限集合
     */
    List<Permission> findByUser(String guid);

    /**
     * 根据用户查询二级路由
     *
     * @param userId 用户ID
     * @return 路由
     */
    List<Permission> findPerByUser(String userId);

    /**
     * 根据 角色查询权限
     *
     * @param roleId 角色ID
     * @return 权限集合
     */
    List<Permission> findByRoleId(String roleId);

    /**
     * 根据用户查询独立权限
     *
     * @param guid 用户ID
     * @return 权限集合
     */
    List<Permission> findByUserId(String guid);

    /**
     * 根据用户查询独立权限
     *
     * @param guid 用户ID
     * @return 权限集合
     */
    List<Permission> findByUserId2(String guid);
}
