package com.boyoi.work.controller;

import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.core.groups.Groups;
import com.boyoi.work.entity.WriteApi;
import com.boyoi.work.service.WriteApiService;
import com.boyoi.work.groups.WriteApiGroup;
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
 * @date 2020-10-19
 */
@RestController
@RequestMapping(value = "/writeApi")
@Slf4j
public class WriteApiController extends BaseController<WriteApi, WriteApiService> {

    @Resource
    protected WriteApiService service;

    /**
     * 添加(付文攀)
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "新增写入接口", businessType = BusinessType.INSERT)
    public Result add(@Validated(value = WriteApiGroup.Add.class) @RequestBody WriteApi writeApi) {
        return Result.success(service.add(writeApi));
    }

    /**
     * 修改(付文攀)
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "更新写入接口", businessType = BusinessType.UPDATE)
    public Result update(@Validated(value = WriteApiGroup.Update.class) @RequestBody WriteApi writeApi) {
        return Result.success(service.update(writeApi));
    }

    /**
     * 修改启用状态(付文攀)
     */
    @PutMapping("/updateState")
    @OptLogAnnotation(title = "更新写入接口状态", businessType = BusinessType.UPDATE)
    public Result updateState(@Validated(value = WriteApiGroup.UpdateStatus.class) @RequestBody WriteApi writeApi) {
        return Result.success(service.updateState(writeApi));
    }

    /**
     * 根据主键查询(付文攀)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody WriteApi writeApi) {
        return Result.success(service.findById(writeApi.getGuid()));
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
