package com.boyoi.work.dao;

import com.boyoi.core.dao.BaseDao;
import com.boyoi.work.entity.NewWriteApi;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewWriteApiDao extends BaseDao {

    /**
     * 通过url查找
     *
     * @param apiUrl     url
     * @param createUser createUser
     * @return o
     */
    NewWriteApi findByUrl(@Param("apiUrl") String apiUrl, @Param("createUser") String createUser);

    List<NewWriteApi> findValidList();
}
