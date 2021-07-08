package com.boyoi.base.controller;

import com.boyoi.base.service.LoginLogService;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.LoginLog;
import com.boyoi.core.entity.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录日志
 *
 * @author ZhouJL
 * @date 2020/4/29 09:37
 */
@RestController
@RequestMapping("/loginLog")
public class LoginLogController {

    @Resource
    private LoginLogService loginLogService;


    /**
     * 分页条件查询(周佳林)
     */
    @PostMapping("/list")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(loginLogService.findByGridRequest(gridRequest));
    }

    /**
     * 根据主键查询(周佳林)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody LoginLog loginLog) {
        return Result.success(loginLogService.findById(loginLog.getGuid()));
    }
}
