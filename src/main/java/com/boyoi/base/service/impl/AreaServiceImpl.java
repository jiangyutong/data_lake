package com.boyoi.base.service.impl;

import com.boyoi.base.dao.AreaDao;
import com.boyoi.base.service.AreaService;
import com.boyoi.core.entity.Area;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 描述：
 * 地址信息 业务实现类
 *
 * @author ZhouJL
 * @date 2018/12/20 15:33
 */
@Service
public class AreaServiceImpl extends BaseServiceImpl<AreaDao> implements AreaService {

    @Resource
    private AreaDao dao;

    @Override
    public int addBatch(List<Area> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(Area area) {
        return dao.del(area);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    public int update(Area area) {
        return dao.update(area);
    }

    @Override
    public int updateByNotEmpty(Area area) {
        return dao.updateByNotEmpty(area);
    }

    @Override
    public Area findById(Serializable id) {
        return dao.findById(id);
    }

    @Override
    public List<Area> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<Area> findByGridRequest(EasyGridRequest gridRequest) {
        return PageHelper
                .offsetPage(gridRequest.getPage(), gridRequest.getRows()) //分页
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder()) //排序
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest)); //数据
    }

    @Override
    public List<Map<String, String>> findProvince() {
        return dao.findProvince();
    }

    /**
     * 通过父级Id进行下级城市数据查询
     *
     * @param areaId
     * @return
     */
    @Override
    public List<Map<String, String>> findCitysByParentId(String areaId) {
        return dao.findCitysByParentId(areaId);
    }

    @Override
    public List<Map<String, Object>> getProvinceCity() {
        List<Map<String, Object>> returnList = new ArrayList<>();
        List<Area> provinces = dao.getProvinceCity();
        for (Area thisArea : provinces) {
            Map<String, Object> provinceMap = new HashMap<>();
            provinceMap.put("value", thisArea.getAreaId());
            provinceMap.put("label", thisArea.getAreaName());
            List<Map<String, Object>> cityList = new ArrayList<>();
            for (Area thisArea1 : thisArea.getChildrens()) {
                Map<String, Object> cityMap = new HashMap<>();
                cityMap.put("value", thisArea1.getAreaId());
                cityMap.put("label", thisArea1.getAreaName());
                List<Map<String, Object>> areaList = new ArrayList<>();
                for (Area thisArea2 : thisArea1.getChildrens()) {
                    Map<String, Object> areaMap = new HashMap<>();
                    areaMap.put("value", thisArea2.getAreaId());
                    areaMap.put("label", thisArea2.getAreaName());
                    areaList.add(areaMap);
                }
                cityMap.put("children", areaList);
                cityList.add(cityMap);
            }
            provinceMap.put("children", cityList);
            returnList.add(provinceMap);
        }
        return returnList;
    }

    @Override
    public List<Map<String, Object>> getProvinceCity1() {
        List<Map<String, Object>> returnList = new ArrayList<>();

        Set<String> stringSet = new HashSet<>();
        stringSet.add("北京市");
        stringSet.add("上海市");
        stringSet.add("重庆市");
        stringSet.add("天津市");
        List<Area> provinces = dao.getProvinceCity1();
        for (Area thisArea : provinces) {
            Map<String, Object> provinceMap = new HashMap<>();
            provinceMap.put("value", thisArea.getAreaId());
            provinceMap.put("label", thisArea.getAreaName());
            if (stringSet.add(thisArea.getAreaName())) {
                List<Map<String, Object>> cityList = new ArrayList<>();
                for (Area thisArea1 : thisArea.getChildrens()) {
                    Map<String, Object> cityMap = new HashMap<>();
                    cityMap.put("value", thisArea1.getAreaId());
                    cityMap.put("label", thisArea1.getAreaName());
                    cityList.add(cityMap);
                }
                provinceMap.put("children", cityList);
            } else {
                provinceMap.put("children", new ArrayList<>());
            }
            returnList.add(provinceMap);
        }
        return returnList;
    }


    /**
     * 中国
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findChina() {
        List<Map<String, Object>> returnList = new ArrayList<>();
        List<Area> country = dao.findChina();
        for (Area thisArea : country) {
            Map<String, Object> provinceMap = new HashMap<>();
            provinceMap.put("value", thisArea.getAreaId());
            provinceMap.put("label", thisArea.getAreaName());
            List<Map<String, Object>> cityList = new ArrayList<>();
            for (Area thisArea1 : thisArea.getChildrens()) {
                Map<String, Object> cityMap = new HashMap<>();
                cityMap.put("value", thisArea1.getAreaId());
                cityMap.put("label", thisArea1.getAreaName());
                List<Map<String, Object>> areaList = new ArrayList<>();
                for (Area thisArea2 : thisArea1.getChildrens()) {
                    Map<String, Object> areaMap = new HashMap<>();
                    areaMap.put("value", thisArea2.getAreaId());
                    areaMap.put("label", thisArea2.getAreaName());
                    areaList.add(areaMap);
                }
                cityMap.put("children", areaList);
                cityList.add(cityMap);
            }
            provinceMap.put("children", cityList);
            returnList.add(provinceMap);
        }
        return returnList;
    }
}
