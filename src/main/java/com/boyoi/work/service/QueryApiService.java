package com.boyoi.work.service;

import com.boyoi.core.service.BaseService;
import com.boyoi.work.entity.QueryApi;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.work.entity.WriteApi;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 查询接口 业务接口层
 *
 * @author fuwp
 * @date 2020-10-20
 */
public interface QueryApiService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param queryApi 实体
     * @return 受影响的个数
     */
    int add(QueryApi queryApi);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<QueryApi> list);

    /**
     * 删除一个实体
     *
     * @param queryApi 实体内容
     * @return 受影响的个数
     */
    int del(QueryApi queryApi);

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
     * @param queryApi 实体
     * @return 受影响的个数
     */
    int update(QueryApi queryApi);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param queryApi 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(QueryApi queryApi);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    QueryApi findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<QueryApi> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<QueryApi> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 更新启用状态
     *
     * @param queryApi obj
     * @return int
     */
    int updateState(QueryApi queryApi);

    List<QueryApi> validList();

    /**
     * 通过url查询
     *
     * @param apiUrl url
     * @param userId userId
     * @return obj
     */
    QueryApi findByUrl(String apiUrl, String userId);
}
