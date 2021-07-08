package com.boyoi.work.dao;

import com.boyoi.core.dao.BaseDao;
import com.boyoi.work.entity.Link;

import java.util.*;


/**
 * 描述：
 * 数据链接  Dao层
 *
 * @author Harvey ylq
 * @date 2020-09-17
 */
public interface LinkDao extends BaseDao {

    List<Link> findBydum(Link link);

}
