package com.boyoi.work.controller;

import cn.hutool.core.util.StrUtil;
import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.work.entity.LinkTable;
import com.boyoi.work.service.LinkTableService;
import com.boyoi.work.groups.LinkTableGroup;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 描述：
 * 已复制到本地表信息 控制层
 *
 * @author Xiaopeng
 * @date 2020-09-23
 */
@RestController
@RequestMapping(value = "/linkTable")
@Slf4j
public class LinkTableController extends BaseController<LinkTable, LinkTableService> {

    @Resource
    protected LinkTableService service;

    /**
     * 添加(肖鹏)
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "新增表信息", businessType = BusinessType.INSERT)
    public Result add(@RequestBody Map<String, Object> map) {
        return Result.success(service.add(map));
    }

    /**
     * 修改(肖鹏)
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "修改表信息", businessType = BusinessType.UPDATE)
    public Result update(@RequestBody Map<String, Object> map) {
        return Result.success(service.update(map));
    }

    /**
     * 根据主键删除(肖鹏)
     */
    @DeleteMapping("/del")
    @OptLogAnnotation(title = "删除表信息", businessType = BusinessType.DELETE)
    public Result del(@RequestBody LinkTable linkTable) {
        return Result.success(service.del(linkTable));
    }

    /**
     * 根据主键查询(肖鹏)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody LinkTable linkTable) {
        return Result.success(service.findById(linkTable.getGuid()));
    }

    /**
     * 分页条件查询(肖鹏)
     */
    @PostMapping("/list/{type}")
    public Result list(@RequestBody EasyGridRequest gridRequest, @PathVariable String type) {
        if (StrUtil.isNotBlank(type)) {
            gridRequest.getMap2().put("type", type);
        }
        return Result.success(service.findByGridRequest(gridRequest));
    }

    /**
     * 重复数据校验(肖鹏)
     */
    @PostMapping("/check")
    public Result check(@RequestBody LinkTable linkTable) {
        return Result.success(service.findByCheck(linkTable));
    }

    /**
     * 分页条件查询(肖鹏)
     */
    @PostMapping("/listto")
    public Result listto(@RequestBody EasyGridRequest gridRequest) {
        Map<String, String> mmp = gridRequest.getMap2();
        mmp.put("optPerson", RedisUtils.getLoginUser().getStr("guid"));
        gridRequest.setMap2(mmp);
        return Result.success(service.findByGridRequest(gridRequest));
    }

    /**
     * 查询详情
     *
     * @param linkTable
     * @return
     */
    @PostMapping("/findDetail")
    public Result findDetail(@RequestBody LinkTable linkTable) {
        return Result.success(service.findDetail(linkTable.getGuid()));
    }


    @GetMapping("/query")
    public Result queryData() {
        LinkTable linkTable = new LinkTable();
        return Result.success(service.findByCheck(linkTable));
    }
}
