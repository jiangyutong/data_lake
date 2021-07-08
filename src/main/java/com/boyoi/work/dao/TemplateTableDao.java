package com.boyoi.work.dao;

import com.boyoi.core.dao.BaseDao;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.work.entity.Link;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 描述：
 * 已复制到本地表信息  Dao层
 *
 * @author Liyucheng
 * @date 2021-4-21
 */
public interface TemplateTableDao extends BaseDao {

    List<Map<String, Object>> getAllFiledMapper(@Param("templateName") String templateName, @Param("fieldNames") List<String> fieldNames, @Param("gridRequest") EasyGridRequest gridRequest);


    List<String> findByName(String templateName);


    List<Map<String, String>> findByFields(@Param("fieldNames") List<String> fieldNames, @Param("templateName") String templateName, @Param("original_table") String original_table);

}