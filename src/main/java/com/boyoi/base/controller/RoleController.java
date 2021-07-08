package com.boyoi.base.controller;

import com.boyoi.base.service.RoleService;
import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import com.boyoi.core.entity.Role;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.core.groups.RoleGroup;
import com.boyoi.core.multi.MultiRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 * 角色 控制层
 *
 * @author ZhouJL
 * @date 2020-05-12
 */
@RestController
@RequestMapping(value = "/role")
@Slf4j
public class RoleController extends BaseController<Role, RoleService> {

    @Resource
    protected RoleService service;

    /**
     * 添加(周佳林)
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "角色管理", businessType = BusinessType.INSERT)
    public Result add(@MultiRequestBody Role role, @MultiRequestBody List<String> premissions) {
        service.add(role, premissions);
        return Result.success();
    }

    /**
     * 修改(周佳林)
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "角色管理", businessType = BusinessType.UPDATE)
    public Result update(@MultiRequestBody Role role, @MultiRequestBody List<String> premissions) {
        service.update(role, premissions);
        return Result.success();
    }

    /**
     * 根据主键删除(周佳林)
     */
    @DeleteMapping("/del")
    @OptLogAnnotation(title = "角色管理", businessType = BusinessType.DELETE)
    public Result del(@Validated(value = RoleGroup.Del.class) @RequestBody Role role) {
        return Result.success(service.del(role));
    }

    /**
     * 根据主键查询(周佳林)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody Role role) {
        return Result.success(service.findById(role.getGuid()));
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
    public Result check(@RequestBody Role role) {
        return Result.success(service.findByCheck(role));
    }

    /**
     * 分页条件查询(周佳林)
     */
    @PostMapping("/findAll")
    public Result findAll() {
        return Result.success(service.findAll());
    }

}
