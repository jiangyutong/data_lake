package com.boyoi.work.dao;

import com.boyoi.core.dao.BaseDao;
import com.boyoi.work.entity.WriteApi;
import org.apache.ibatis.annotations.Param;


/**
 * 描述：
 * 查询接口  Dao层
 *
 * @author fuwp
 * @date 2020-10-19
 */
public interface WriteApiDao extends BaseDao {
    /**
     * 通过url查找
     *
     * @param apiUrl     url
     * @param createUser createUser
     * @return o
     */
    WriteApi findByUrl(@Param("apiUrl") String apiUrl, @Param("createUser") String createUser);
}
