package com.boyoi.base.service;


import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Permission;
import com.boyoi.core.service.BaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 权限 业务接口层
 *
 * @Author ZhouJL
 * @Date 2019-04-26
 */
public interface PermissionService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param childrens  指令集合
     * @param permission 实体
     * @return 受影响的个数
     */
    void add(Permission permission, List<Map<String, String>> childrens);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<Permission> list);

    /**
     * 删除一个实体
     *
     * @param permission 实体内容
     * @return 受影响的个数
     */
    int del(Permission permission);

    /**
     * 批量删除实体
     *
     * @param ids id的List集合
     * @return 受影响的个数
     */
    int delBatch(List<Serializable> ids);

    /**
     * 更新一个实体
     *
     * @param childrens  指令集合
     * @param permission 实体
     * @return 受影响的个数
     */
    void update(Permission permission, List<Map<String, String>> childrens);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param permission 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(Permission permission);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    Permission findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<Permission> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    List<Map<String, Object>> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 查询角色拥有的权限
     *
     * @param roleId 角色id
     * @return 权限集合
     */
    List<Permission> findByRole(String roleId);

    /**
     * 查询所有一级菜单
     *
     * @return 一级菜单集合
     */
    List<Permission> allParent();

}
