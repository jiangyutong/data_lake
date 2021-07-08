package com.boyoi.base.service;

import com.boyoi.core.entity.Area;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.service.BaseService;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 区域 业务接口层
 *
 * @author Zhangyq
 * @date 2020-07-29
 */
public interface AreaService extends BaseService {


    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<Area> list);

    /**
     * 删除一个实体
     *
     * @param area 实体内容
     * @return 受影响的个数
     */
    int del(Area area);

    /**
     * 批量删除实体
     *
     * @param ids id的List集合
     * @return 受影响的个数
     */
    int delBatch(List<Serializable> ids);

    /**
     * 更新一个实体
     *
     * @param area 实体
     * @return 受影响的个数
     */
    int update(Area area);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param area 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(Area area);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    Area findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<Area> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    PageInfo<Area> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 查询省份数据
     *
     * @return
     */
    List<Map<String, String>> findProvince();

    /**
     * 通过父级Id进行下级城市数据查询
     *
     * @param areaId
     * @return
     */
    List<Map<String, String>> findCitysByParentId(String areaId);

    /**
     * 中国
     *
     * @return
     */
    List<Map<String, Object>> findChina();

    /**
     * 查询所有省份及下属城市
     *
     * @return
     */
    List<Map<String, Object>> getProvinceCity();

    /**
     * 获取省市合集
     *
     * @return
     */
    List<Map<String, Object>> getProvinceCity1();
}
