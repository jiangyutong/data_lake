package com.boyoi.work.service;

import com.boyoi.core.service.BaseService;
import com.boyoi.work.entity.LinkTable;
import com.boyoi.core.entity.EasyGridRequest;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 已复制到本地表信息 业务接口层
 *
 * @author Xiaopeng
 * @date 2020-09-23
 */
public interface LinkTableService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param map 集合
     * @return 受影响的个数
     */
    int add(Map<String, Object> map);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<LinkTable> list);

    /**
     * 删除一个实体
     *
     * @param linkTable 实体内容
     * @return 受影响的个数
     */
    int del(LinkTable linkTable);

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
     * @param map 集合
     * @return 受影响的个数
     */
    int update(Map<String, Object> map);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param linkTable 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(LinkTable linkTable);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    LinkTable findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<LinkTable> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<LinkTable> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    Map<String, Object> findDetail(Serializable id);
}
