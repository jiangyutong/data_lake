package com.boyoi.work.service;

import com.boyoi.core.service.BaseService;
import com.boyoi.work.entity.QueryLog;
import com.boyoi.core.entity.EasyGridRequest;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 数据写入日志 业务接口层
 *
 * @author fuwp
 * @date 2020-10-22
 */
public interface QueryLogService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param queryLog 实体
     * @return 受影响的个数
     */
    int add(QueryLog queryLog);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<QueryLog> list);

    /**
     * 删除一个实体
     *
     * @param queryLog 实体内容
     * @return 受影响的个数
     */
    int del(QueryLog queryLog);

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
     * @param queryLog 实体
     * @return 受影响的个数
     */
    int update(QueryLog queryLog);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param queryLog 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(QueryLog queryLog);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    QueryLog findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<QueryLog> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<QueryLog> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 首页查询记录数据查询
     *
     * @return
     */
    Map<String, Object> queryData();
}
