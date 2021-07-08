package com.boyoi.base.controller;

import com.boyoi.base.service.DeptService;
import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.entity.Dept;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.core.groups.DeptGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 描述：
 * 部门 控制层
 *
 * @author ZhouJL
 * @date 2020-05-09
 */
@RestController
@RequestMapping(value = "/dept")
@Slf4j
public class DeptController extends BaseController<Dept, DeptService> {

    @Resource
    protected DeptService service;

    /**
     * 添加(周佳林)
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "部门管理", businessType = BusinessType.INSERT)
    public Result add(@Validated(value = DeptGroup.Add.class) @RequestBody Dept dept) {
        return Result.success(service.add(dept));
    }

    /**
     * 修改(周佳林)
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "部门管理", businessType = BusinessType.UPDATE)
    public Result update(@Validated(value = DeptGroup.Update.class) @RequestBody Dept dept) {
        return Result.success(service.updateByNotEmpty(dept));
    }

    /**
     * 根据主键删除(周佳林)
     */
    @DeleteMapping("/del")
    @OptLogAnnotation(title = "部门管理", businessType = BusinessType.DELETE)
    public Result del(@Validated(value = DeptGroup.Del.class) @RequestBody Dept dept) {
        return Result.success(service.del(dept));
    }

    /**
     * 根据主键查询(周佳林)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody Dept dept) {
        return Result.success(service.findById(dept.getGuid()));
    }

    /**
     * 分页条件查询(周佳林)
     */
    @PostMapping("/list")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(service.findByGridRequest(gridRequest));
    }

    /**
     * 重复数据校验(周佳林)
     */
    @PostMapping("/check")
    public Result check(@RequestBody Dept dept) {
        return Result.success(service.findByCheck(dept));
    }

    @PostMapping("/findAll")
    public Result findAll(@RequestBody Map<String, String> param) {

        return Result.success(service.findAllTest(param));
    }
}
