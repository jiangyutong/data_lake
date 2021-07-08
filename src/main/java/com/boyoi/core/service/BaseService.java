package com.boyoi.core.service;

import com.boyoi.core.entity.BaseEntity;

import java.util.List;

/**
 * 公共服务类
 *
 * @author zhoujl
 */
public interface BaseService {

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


}
