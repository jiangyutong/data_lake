package com.boyoi.base.dao;


import com.boyoi.core.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 描述：
 * 操作日志  Dao层
 *
 * @Author ZhouJL
 * @Date 2020-04-28
 */
@Mapper
public interface OptLogDao extends BaseDao {

    Map<String, Object> queryData(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
