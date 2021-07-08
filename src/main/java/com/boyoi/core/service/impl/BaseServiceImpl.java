package com.boyoi.core.service.impl;

import com.boyoi.core.dao.BaseDao;
import com.boyoi.core.entity.BaseEntity;
import com.boyoi.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 公共服务类的实现方法
 *
 * @author ZhouJL
 * @date 2019/2/21 10:58
 */
public abstract class BaseServiceImpl<D extends BaseDao> implements BaseService {

    /**
     * 通过spring,把传来的DAO层的接口实例化
     */
    @Autowired
    protected D dao;

    @Override
    public <T extends BaseEntity> Integer findByCheck(T t) {
        return dao.findByCheck(t);
    }

    @Override
    public <T extends BaseEntity> List<T> findByDomain(T t) {
        return dao.findByDomain(t);
    }

}
