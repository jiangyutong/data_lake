package com.boyoi.work.dao;

import com.boyoi.core.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


/**
 * 描述：
 * 数据写入日志  Dao层
 *
 * @author fuwp
 * @date 2020-10-21
 */
public interface WriteLogDao extends BaseDao {

    Map<String, Object> queryData(@Param("map") Map<String, Object> ma);

    Map<String, Object> queryDatajl(@Param("map") Map<String, Object> ma);
}
