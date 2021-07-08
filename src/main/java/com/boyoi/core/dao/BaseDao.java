package com.boyoi.core.dao;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.core.entity.EasyGridRequest;

import java.io.Serializable;
import java.util.List;

/**
 * dao层基类,继承此接口,获得增删改查的基本功能
 *
 * @author zhoujl
 */
public interface BaseDao {

    /**
     * 保存一个实体
     *
     * @param t   实体
     * @param <T> 实体泛型
     * @return 受影响的个数
     */
    <T extends BaseEntity> int add(T t);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @param <T>  实体泛型
     * @return 受影响的个数
     */
    <T extends BaseEntity> int addBatch(List<T> list);

    /**
     * 删除一个实体
     *
     * @param t 实体内容
     * @return 受影响的个数
     */
    <T extends BaseEntity> int del(T t);

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
     * @param t   实体
     * @param <T> 实体泛型
     * @return 受影响的个数
     */
    <T extends BaseEntity> int update(T t);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param t   实体
     * @param <T> 实体泛型
     * @return 受影响的个数
     */
    <T extends BaseEntity> int updateByNotEmpty(T t);

    /**
     * 通过ID查找实体
     *
     * @param id  id
     * @param <T> 实体泛型
     * @return 实体
     */
    <T extends BaseEntity> T findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @param <T> 实体泛型
     * @return 实体集合
     */
    <T extends BaseEntity> List<T> findAll();

    /**
     * 通过实体查找查找有多少重复实体
     *
     * @param t 实体
     * @return 集合
     */
    <T extends BaseEntity> Integer findByCheck(T t);

    /**
     * 通过实体查找查找有多少实体
     *
     * @param t 实体
     * @return 集合
     */
    <T extends BaseEntity> List<T> findByDomain(T t);

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    <T extends BaseEntity> List<T> findByGridRequest(EasyGridRequest gridRequest);


}
