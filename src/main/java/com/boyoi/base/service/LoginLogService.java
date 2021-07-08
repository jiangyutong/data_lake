package com.boyoi.base.service;

import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.LoginLog;
import com.boyoi.core.service.BaseService;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 系统访问记录 业务接口层
 *
 * @Author ZhouJL
 * @Date 2020-04-28
 */
public interface LoginLogService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param loginLog 实体
     * @return 受影响的个数
     */
    int add(LoginLog loginLog);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<LoginLog> list);

    /**
     * 删除一个实体
     *
     * @param loginLog 实体内容
     * @return 受影响的个数
     */
    int del(LoginLog loginLog);

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
     * @param loginLog 实体
     * @return 受影响的个数
     */
    int update(LoginLog loginLog);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param loginLog 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(LoginLog loginLog);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    LoginLog findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<LoginLog> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<LoginLog> findByGridRequest(EasyGridRequest gridRequest);

}
