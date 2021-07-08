package com.boyoi.base.service;

import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Role;
import com.boyoi.core.service.BaseService;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 角色 业务接口层
 *
 * @author ZhouJL
 * @date 2020-05-12
 */
public interface RoleService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param role        实体
     * @param premissions 权限
     * @return 受影响的个数
     */
    void add(Role role, List<String> premissions);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<Role> list);

    /**
     * 删除一个实体
     *
     * @param role 实体内容
     * @return 受影响的个数
     */
    int del(Role role);

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
     * @param role 实体
     * @return 受影响的个数
     */
    void update(Role role, List<String> premissions);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param role 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(Role role);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    Role findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<Role> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<Role> findByGridRequest(EasyGridRequest gridRequest);

}
