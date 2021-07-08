package com.boyoi.base.service;

import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.OptLog;
import com.boyoi.core.service.BaseService;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 操作日志 业务接口层
 *
 * @Author ZhouJL
 * @Date 2020-04-28
 */
public interface OptLogService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param optLog 实体
     * @return 受影响的个数
     */
    int add(OptLog optLog);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<OptLog> list);

    /**
     * 删除一个实体
     *
     * @param optLog 实体内容
     * @return 受影响的个数
     */
    int del(OptLog optLog);

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
     * @param optLog 实体
     * @return 受影响的个数
     */
    int update(OptLog optLog);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param optLog 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(OptLog optLog);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    OptLog findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<OptLog> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<OptLog> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 首页系统操作次数查询
     *
     * @return
     */
    Map<String, Object> queryData();
}
