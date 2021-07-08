package com.boyoi.base.dao;


import com.boyoi.core.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 部门  Dao层
 *
 * @author ZhouJL
 * @date 2020-05-09
 */
@Mapper
public interface DeptDao extends BaseDao {

    List<Map<String, Object>> findAllTest(@Param("params") Map<String, String> map);
}
