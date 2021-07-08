package com.boyoi.work.controller;

import com.boyoi.core.controller.BaseController;
import com.boyoi.core.util.FtpUtil;
import com.boyoi.work.entity.WriteLog;
import com.boyoi.work.service.WriteLogService;
import com.boyoi.work.groups.WriteLogGroup;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * 描述：
 * 数据写入日志 控制层
 *
 * @author fuwp
 * @date 2020-10-21
 */
@RestController
@RequestMapping(value = "/writeLog")
@Slf4j
public class WriteLogController extends BaseController<WriteLog, WriteLogService> {

    @Resource
    protected WriteLogService service;

    /**
     * 添加(付文攀)
     */
    @PostMapping("/add")
    public Result add(@Validated(value = WriteLogGroup.Add.class) @RequestBody WriteLog writeLog) {
        return Result.success(service.add(writeLog));
    }

    /**
     * 修改(付文攀)
     */
    @PutMapping("/update")
    public Result update(@Validated(value = WriteLogGroup.Update.class) @RequestBody WriteLog writeLog) {
        return Result.success(service.update(writeLog));
    }

    /**
     * 根据主键删除(付文攀)
     */
    @DeleteMapping("/del")
    public Result del(@Validated(value = WriteLogGroup.Del.class) @RequestBody WriteLog writeLog) {
        return Result.success(service.del(writeLog));
    }

    /**
     * 根据主键查询(付文攀)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody WriteLog writeLog) {
        return Result.success(service.findById(writeLog.getGuid()));
    }

    /**
     * 分页条件查询(付文攀)
     */
    @PostMapping("/list")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(service.findByGridRequest(gridRequest));
    }

    /**
     * 重复数据校验(付文攀)
     */
    @PostMapping("/check")
    public Result check(@RequestBody WriteLog writeLog) {
        return Result.success(service.findByCheck(writeLog));
    }

    /**
     * 下载文件
     *
     * @param response 响应
     * @param writeLog obj
     */
    @PostMapping("/downData")
    public void downData(HttpServletResponse response, @RequestBody @Validated(value = WriteLogGroup.DownData.class) WriteLog writeLog) {
        WriteLog byId = service.findById(writeLog.getGuid());
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + byId.getGuid() + ".json");
        FtpUtil.downloadFile(byId.getDataPath(), response);
    }

    /**
     * 首页写入数据量查询
     *
     * @return
     */
    @GetMapping("/query")
    public Result queryData() {
        return Result.success(service.queryData());
    }
}
