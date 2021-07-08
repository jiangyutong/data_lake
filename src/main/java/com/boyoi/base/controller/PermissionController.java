package com.boyoi.base.controller;

import com.boyoi.base.service.PermissionService;
import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Permission;
import com.boyoi.core.entity.Result;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.core.groups.PermissionGroup;
import com.boyoi.core.multi.MultiRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 权限 控制层
 *
 * @author ZhouJL
 * @date 2019-04-26
 */
@RestController
@RequestMapping(value = "/permission")
@Slf4j
public class PermissionController extends BaseController<Permission, PermissionService> {

    @Resource
    protected PermissionService service;

    /**
     * 添加
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "权限管理", businessType = BusinessType.INSERT)
    public Result add(@MultiRequestBody(groups = {PermissionGroup.Add.class}) Permission permission, @MultiRequestBody List<Map<String, String>> childrens) {
        service.add(permission, childrens);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "权限管理", businessType = BusinessType.UPDATE)
    public Result update(@MultiRequestBody(groups = {PermissionGroup.Update.class}) Permission permission, @MultiRequestBody List<Map<String, String>> childrens) {
        service.update(permission, childrens);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/del")
    @OptLogAnnotation(title = "权限管理", businessType = BusinessType.DELETE)
    public Result findById(@Validated(value = PermissionGroup.Del.class) @RequestBody Permission permission) {
        return Result.success(service.del(permission));
    }

    /**
     * 查询
     */
    @PostMapping("/detail")
    public Result findById(@MultiRequestBody String guid) {
        return Result.success(service.findById(guid));
    }

    /**
     * 列表查询
     */
    @PostMapping("/list")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(service.findByGridRequest(gridRequest));
    }

    /**
     * 重复数据校验
     */
    @PostMapping("/check")
    public Result findByCheck(@RequestBody Permission permission) {
        return Result.success(service.findByCheck(permission));
    }

    /**
     * 查询所有一级菜单
     */
    @PostMapping("/allParent")
    public Result allParent() {
        return Result.success(service.allParent());
    }

//    @PostMapping("/findPerByUser")
//    @ApiOperation(value = "根据用户查询二级路由")
//    public Result findPerByUser() {
//        return Result.success(service.findPerByUser());
//    }


}
