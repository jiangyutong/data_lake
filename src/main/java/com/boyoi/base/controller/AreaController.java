package com.boyoi.base.controller;

import com.boyoi.base.service.AreaService;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.entity.Area;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import com.boyoi.core.groups.AreaGroup;
import com.boyoi.core.multi.MultiRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 描述：
 * 区域 控制层
 *
 * @author Harvey Y.L.Q.
 * @date 2020-05-08
 */
@RestController
@RequestMapping(value = "/area")
@Slf4j
public class AreaController extends BaseController<Area, AreaService> {

    @Resource
    protected AreaService service;

    /**
     * 修改(张钰邱)
     */
    @PutMapping("/update")
    public Result update(@Validated(value = AreaGroup.Update.class) @RequestBody Area area) {
        return Result.success(service.update(area));
    }

    /**
     * 根据主键删除(张钰邱)
     */
    @DeleteMapping("/del")
    public Result del(@Validated(value = AreaGroup.Del.class) @RequestBody Area area) {
        return Result.success(service.del(area));
    }


    /**
     * 中国
     *
     * @return
     */
    @PostMapping("/findChina")
    public Result findChina() {
        return Result.success(service.findChina());
    }


    /**
     * 根据主键查询(杨龙全)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody Area area) {
        return Result.success(service.findById(area.getAreaId()));
    }

    /**
     * 分页条件查询(杨龙全)
     */
    @PostMapping("/list")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(service.findByGridRequest(gridRequest));
    }

    /**
     * 重复数据校验(张钰邱)
     */
    @PostMapping("/check")
    public Result check(@RequestBody Area area) {
        return Result.success(service.findByCheck(area));
    }

    /**
     * 查询省级城市数据(杨龙全)
     */
    @PostMapping("/findProvince")
    public Result findProvince() {
        return Result.success(service.findProvince());
    }

    /**
     * 通过父级Id进行子级城市数据查询(杨龙全)
     */
    @PostMapping("/findCitysByParentId")
    public Result findCitysByParentId(@MultiRequestBody String areaId) {
        return Result.success(service.findCitysByParentId(areaId));
    }

    @PostMapping("/getProvinceCity")
    public Result getProvinceCity() {
        return Result.success(service.getProvinceCity());
    }

    @PostMapping("/getProvinceCity1")
    public Result getProvinceCity1() {
        return Result.success(service.getProvinceCity1());
    }
}
