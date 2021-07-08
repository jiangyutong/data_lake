package com.boyoi.work.service;

import com.boyoi.core.service.BaseService;
import com.boyoi.work.entity.WriteLog;
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
 * @date 2020-10-21
 */
public interface WriteLogService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param writeLog 实体
     * @return 受影响的个数
     */
    int add(WriteLog writeLog);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<WriteLog> list);

    /**
     * 删除一个实体
     *
     * @param writeLog 实体内容
     * @return 受影响的个数
     */
    int del(WriteLog writeLog);

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
     * @param writeLog 实体
     * @return 受影响的个数
     */
    int update(WriteLog writeLog);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param writeLog 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(WriteLog writeLog);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    WriteLog findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<WriteLog> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<WriteLog> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 首页写入数据量查询
     *
     * @return
     */
    Map<String, Object> queryData();
}
