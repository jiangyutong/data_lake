package com.boyoi.work.controller;

import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.core.groups.Groups;
import com.boyoi.work.entity.NewWriteApi;
import com.boyoi.work.entity.WriteApi;
import com.boyoi.work.groups.NewWriteApiGroup;
import com.boyoi.work.service.NewWriteApiService;
import com.boyoi.work.service.WriteApiService;
import com.boyoi.work.groups.WriteApiGroup;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 描述：
 * 查询接口 控制层
 *
 * @author fuwp
 * @date 2020-10-19
 */
@RestController
@RequestMapping(value = "/newWriteApi")
@Slf4j
public class NewWriteApiController extends BaseController<NewWriteApi, NewWriteApiService> {

    @Resource
    protected NewWriteApiService service;

    /**
     * TODO 添加(李玉成)
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "新增写入接口", businessType = BusinessType.INSERT)
    public Result add(@Validated(value = NewWriteApiGroup.Add.class) @RequestBody Map<String, Object> map) {
        return Result.success(service.add(map));
    }

    /**
     *
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "更新写入接口", businessType = BusinessType.UPDATE)
    public Result update(@Validated(value = NewWriteApiGroup.Update.class) @RequestBody Map<String, Object> map) {
        return Result.success(service.update(map));
    }

    /**
     * 修改写入接口状态
     * 陈俊林
     */
    @PutMapping("/updateState")
    @OptLogAnnotation(title = "更新写入接口状态", businessType = BusinessType.UPDATE)
    public Result updateState(@Validated(value = NewWriteApiGroup.UpdateStatus.class) @RequestBody NewWriteApi newWriteApi) {
        return Result.success(service.updateState(newWriteApi));
    }

    /**
     * 根据主键查询(陈俊林)
     */
    // TODO: detail的信息不仅仅有这些，还需要修改！！！
    @PostMapping("/detail")
    public Result detail(@RequestBody NewWriteApi newWriteApi) {
        return Result.success(service.findById(newWriteApi.getGuid()));
    }

    /**
     * 分页条件查询(李玉成)
     */
    // TODO 这里还有点小问题，map2查找不出来
    @PostMapping("/listto")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        System.out.println(gridRequest.toString());
        return Result.success(service.findByGridRequest(gridRequest));
    }


    /**
     * 查询有效的(李玉成)
     */
    @PostMapping("/validList")
    public Result validList() {
        return Result.success(service.validList());
    }
}
