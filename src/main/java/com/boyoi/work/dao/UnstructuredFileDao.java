package com.boyoi.work.dao;

import com.boyoi.core.dao.BaseDao;

import java.util.*;


/**
 * 描述：
 * 文件上传存储信息表  Dao层
 *
 * @author Xiaopeng
 * @date 2020-10-22
 */
public interface UnstructuredFileDao extends BaseDao {

    List<Map<String, Object>> queryData();
}
