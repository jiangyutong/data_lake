package com.boyoi.work.dao;

import com.boyoi.core.dao.BaseDao;
import com.boyoi.work.entity.QueryApi;
import com.boyoi.work.entity.WriteApi;
import org.apache.ibatis.annotations.Param;


/**
 * 描述：
 * 查询接口  Dao层
 *
 * @author fuwp
 * @date 2020-10-20
 */
public interface QueryApiDao extends BaseDao {
    /**
     * 根据url查询
     *
     * @param apiUrl     url
     * @param createUser 创建者
     * @return obj
     */
    QueryApi findByUrl(@Param("apiUrl") String apiUrl, @Param("createUser") String createUser);
}
