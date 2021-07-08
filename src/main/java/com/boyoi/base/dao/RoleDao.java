package com.boyoi.base.dao;


import com.boyoi.core.dao.BaseDao;
import com.boyoi.core.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 角色  Dao层
 *
 * @author ZhouJL
 * @date 2020-05-12
 */
@Mapper
public interface RoleDao extends BaseDao {

    /**
     * 添加角色权限
     *
     * @param list 权限集合
     */
    void addPermission(List<Map<String, String>> list);


    /**
     * 删除角色权限
     *
     * @param guid 角色ID
     */
    void delPermission(String guid);

    /**
     * 查询用户的角色
     *
     * @param guid 用户ID
     * @return 角色集合
     */
    List<Role> findByUser(String guid);
}
