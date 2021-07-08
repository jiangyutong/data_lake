package com.boyoi.work.controller;

import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.entity.User;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.work.entity.QueryApi;
import com.boyoi.work.service.QueryApiService;
import com.boyoi.work.groups.QueryApiGroup;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 描述：
 * 查询接口 控制层
 *
 * @author fuwp
 * @date 2020-10-20
 */
@RestController
@RequestMapping(value = "/queryApi")
@Slf4j
public class QueryApiController extends BaseController<QueryApi, QueryApiService> {

    @Resource
    protected QueryApiService service;

    /**
     * 添加(付文攀)
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "新增查询接口", businessType = BusinessType.UPDATE)
    public Result add(@Validated(value = QueryApiGroup.Add.class) @RequestBody QueryApi queryApi) {
        return Result.success(service.add(queryApi));
    }

    /**
     * 修改(付文攀)
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "更新查询接口", businessType = BusinessType.UPDATE)
    public Result update(@Validated(value = QueryApiGroup.Update.class) @RequestBody QueryApi queryApi) {
        return Result.success(service.update(queryApi));
    }

    /**
     * 修改启用状态(付文攀)
     */
    @PutMapping("/updateState")
    @OptLogAnnotation(title = "更新查询接口状态", businessType = BusinessType.UPDATE)
    public Result updateState(@Validated(value = QueryApiGroup.UpdateStatus.class) @RequestBody QueryApi queryApi) {
        return Result.success(service.updateState(queryApi));
    }

    /**
     * 根据主键查询(付文攀)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody QueryApi queryApi) {
        return Result.success(service.findById(queryApi.getGuid()));
    }

    /**
     * 分页条件查询(付文攀)
     */
    @PostMapping("/list")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(service.findByGridRequest(gridRequest));
    }

    /**
     * 查询有效的(付文攀)
     */
    @PostMapping("/validList")
    public Result validList() {
        return Result.success(service.validList());
    }
}
