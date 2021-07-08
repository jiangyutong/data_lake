package com.boyoi.base.controller;

import com.boyoi.base.service.OptLogService;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.OptLog;
import com.boyoi.core.entity.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 操作日志
 *
 * @author ZhouJL
 * @date 2020/4/29 09:37
 */
@RestController
@RequestMapping("/optLog")
public class OptLogController {

    @Resource
    private OptLogService optLogService;

    @PostMapping("/add")
    public void add(@RequestBody OptLog optLog) {
        optLogService.add(optLog);
    }

    /**
     * 分页条件查询(周佳林)
     */
    @PostMapping("/list")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(optLogService.findByGridRequest(gridRequest));
    }

    /**
     * 根据主键查询(周佳林)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody OptLog optLog) {
        return Result.success(optLogService.findById(optLog.getGuid()));
    }

    /**
     * 首页系统操作次数查询
     *
     * @return
     */
    @GetMapping("/query")
    public Result queryData() {
        return Result.success(optLogService.queryData());
    }
}
