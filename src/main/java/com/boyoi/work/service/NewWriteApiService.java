package com.boyoi.work.service;

import com.boyoi.core.service.BaseService;
import com.boyoi.work.entity.NewWriteApi;
import com.boyoi.work.entity.WriteApi;
import com.boyoi.core.entity.EasyGridRequest;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 新的查询接口 业务接口层
 *
 * @author fuwp
 * @date 2021-5-21
 */
public interface NewWriteApiService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param map 实体
     * @return 受影响的个数
     */
    int add(Map<String, Object> map);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<NewWriteApi> list);

    /**
     * 删除一个实体
     *
     * @param newWriteApi 实体内容
     * @return 受影响的个数
     */
    int del(NewWriteApi newWriteApi);

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
     * @param map 实体
     * @return 受影响的个数
     */
    int update(Map<String, Object> map);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param newWriteApi 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(NewWriteApi newWriteApi);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    NewWriteApi findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<NewWriteApi> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<NewWriteApi> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 修改启用状态
     *
     * @param newWriteApi obj
     * @return int
     */
    int updateState(NewWriteApi newWriteApi);

    /**
     * 查询有效的
     *
     * @return list
     */
    List<NewWriteApi> validList();

    /**
     * 通过url查询
     *
     * @param apiUrl url
     * @param userId userId
     * @return obj
     */
    NewWriteApi findByUrl(String apiUrl, String userId);
}
