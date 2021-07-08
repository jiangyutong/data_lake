package com.boyoi.work.service;

import com.boyoi.core.service.BaseService;
import com.boyoi.work.entity.Link;
import com.boyoi.core.entity.EasyGridRequest;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 数据链接 业务接口层
 *
 * @author Harvey ylq
 * @date 2020-09-17
 */
public interface LinkService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param link 实体
     * @return 受影响的个数
     */
    int add(Link link);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<Link> list);

    /**
     * 删除一个实体
     *
     * @param link 实体内容
     * @return 受影响的个数
     */
    int del(Link link);

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
     * @param link 实体
     * @return 受影响的个数
     */
    int update(Link link);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param link 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(Link link);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    Link findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<Link> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<Link> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 复制目标数据库的表
     *
     * @param map
     * @return
     */
    int copyTable(Map<String, Object> map);

    /**
     * 根据对象查询
     *
     * @param link
     * @return
     */
    List<Link> findBydum(Link link);

    /**
     * 查询未复制表的信息
     *
     * @param map
     * @return
     */
    Map<String, Object> getNotCopyTable(Map<String, Object> map);
}
