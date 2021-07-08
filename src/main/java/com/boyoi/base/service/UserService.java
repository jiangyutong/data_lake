package com.boyoi.base.service;

import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Token;
import com.boyoi.core.entity.User;
import com.boyoi.core.service.BaseService;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 用户 业务接口层
 *
 * @author ZhouJL
 * @date 2020-05-13
 */
public interface UserService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param user                  实体
     * @param roleIds               角色ID集合
     * @param otherCheckPermissions 独立权限ID
     * @param supplierIds           数据权限，供应商ID集合
     * @return 受影响的个数
     */
    void add(User user, List<String> roleIds, List<String> otherCheckPermissions, List<String> supplierIds);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<User> list);

    /**
     * 删除一个实体
     *
     * @param user 实体内容
     * @return 受影响的个数
     */
    int del(User user);

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
     * @param user                  实体
     * @param roleIds               角色ID集合
     * @param otherCheckPermissions 独立权限ID
     * @param supplierIds           数据权限，供应商ID集合
     * @return 受影响的个数
     */
    void update(User user, List<String> roleIds, List<String> otherCheckPermissions, List<String> supplierIds);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param user 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(User user);

    /**
     * 重置密码
     *
     * @param user 用户
     * @return 影响个数
     */
    int resetPassword(User user);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    User findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<User> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<User> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<User> findByGridRequest2(EasyGridRequest gridRequest);

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<User> findByGridRequest3(EasyGridRequest gridRequest);

    /**
     * 登录
     *
     * @param user 登录信息
     * @return token信息
     */
    Token login(User user);

    /**
     * 查询密码是否正确
     *
     * @param password 密码
     * @return 是否正确
     */
    int checkPassword(String password);

    /**
     * 修改密码
     *
     * @param oldPassword 原密码
     * @param password    新密码
     */
    void updatePassword(String oldPassword, String password);

    /**
     * 修改个人信息
     *
     * @param user 用户信息
     */
    void updateInfo(User user);
}
