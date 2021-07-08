package com.boyoi.base.dao;


import com.boyoi.core.dao.BaseDao;
import com.boyoi.core.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 用户  Dao层
 *
 * @author ZhouJL
 * @date 2020-05-13
 */
@Mapper
public interface UserDao extends BaseDao {

    /**
     * 用户添加角色关联
     *
     * @param list 用户角色关联
     */
    void addRoleToUser(List<Map<String, String>> list);

    /**
     * 删除用户的角色权限
     *
     * @param guid 用户ID
     */
    void delRoleToUser(String guid);

    /**
     * 用户添加权限关联
     *
     * @param list 用户权限关联
     */
    void addPermissionToUser(List<Map<String, String>> list);

    /**
     * 删除用户的独立权限
     *
     * @param guid 用户ID
     */
    void delPermissionToUser(String guid);

    /**
     * 用户添加供应商关联
     *
     * @param list 用户供应商关联
     */
    void addSupplierToUser(List<Map<String, String>> list);

    /**
     * 删除用户的供应商权限
     *
     * @param guid 用户ID
     */
    void delSupplierToUser(String guid);

    /**
     * 根据手机号查询用户
     *
     * @param telephone 手机号
     * @return 用户
     */
    User login(@Param("telephone") String telephone);

}
