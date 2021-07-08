package com.boyoi.base.dao;

import com.boyoi.core.dao.BaseDao;
import com.boyoi.core.entity.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 地址信息  Dao层
 *
 * @author Harvey Y.L.Q.
 * @date 2020-05-08
 */
@Mapper
public interface AreaDao extends BaseDao {

    /**
     * 查询省份数据
     *
     * @return
     */
    List<Map<String, String>> findProvince();

    /**
     * 通过父级Id进行子级数据查询
     *
     * @param areaId
     * @return
     */
    List<Map<String, String>> findCitysByParentId(@Param("areaId") String areaId);

    /**
     * 中国
     *
     * @return
     */
    List<Area> findChina();

    /**
     * 查询所有省份及下属城市
     *
     * @return
     */
    List<Area> getProvinceCity();

    List<Area> getProvinceCity1();

    List<Map<String, String>> findCitysByprovinceId(@Param("areaId") String provinceId);
}
