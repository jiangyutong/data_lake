package com.boyoi.base.controller;

import com.boyoi.base.service.UserService;
import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import com.boyoi.core.entity.User;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.core.groups.UserGroup;
import com.boyoi.core.multi.MultiRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 * 用户 控制层
 *
 * @author ZhouJL
 * @date 2020-05-13
 */
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController extends BaseController<User, UserService> {

    @Resource
    protected UserService service;

    /**
     * 获取当前登录用户(周佳林)
     */
    @PostMapping("/getUser")
    public Result getUser() {
        return Result.success(RedisUtils.getLoginUser());
    }

    /**
     * 添加(周佳林)
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "用户管理", businessType = BusinessType.INSERT)
    public Result add(@MultiRequestBody(groups = UserGroup.Add.class) User user,
                      @MultiRequestBody List<String> roleIds,
                      @MultiRequestBody List<String> otherCheckPermissions,
                      @MultiRequestBody List<String> supplierIds) {
        service.add(user, roleIds, otherCheckPermissions, supplierIds);
        return Result.success();
    }

    /**
     * 修改(周佳林)
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "用户管理", businessType = BusinessType.UPDATE)
    public Result update(@MultiRequestBody(groups = UserGroup.Update.class) User user,
                         @MultiRequestBody List<String> roleIds,
                         @MultiRequestBody List<String> otherCheckPermissions,
                         @MultiRequestBody List<String> supplierIds) {
        service.update(user, roleIds, otherCheckPermissions, supplierIds);
        return Result.success();
    }

    /**
     * 修改状态(周佳林)
     */
    @PutMapping("/updateStatus")
    @OptLogAnnotation(title = "用户管理", businessType = BusinessType.UPDATE)
    public Result userUpdateStatus(@RequestBody User user) {
        return Result.success(service.updateByNotEmpty(user));
    }

    /**
     * 重置密码(周佳林)
     */
    @PutMapping("/resetPassword")
    @OptLogAnnotation(title = "用户管理", businessType = BusinessType.UPDATE)
    public Result resetPassword(@RequestBody User user) {
        return Result.success(service.resetPassword(user));
    }

    /**
     * 根据主键删除(周佳林)
     */
    @DeleteMapping("/del")
    @OptLogAnnotation(title = "用户管理", businessType = BusinessType.DELETE)
    public Result del(@Validated(value = UserGroup.Del.class) @RequestBody User user) {
        return Result.success(service.del(user));
    }

    /**
     * 根据主键查询(周佳林)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody User user) {
        return Result.success(service.findById(user.getGuid()));
    }

    /**
     * 分页条件查询(周佳林)
     */
    @PostMapping("/list")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(service.findByGridRequest(gridRequest));
    }


    /**
     * 分页条件查询-查询可用的管理员(周佳林)
     */
    @PostMapping("/list2")
    public Result list2(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(service.findByGridRequest2(gridRequest));
    }

    /**
     * 分页条件查询-查询供应商(周佳林)
     */
    @PostMapping("/list3")
    public Result list3(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(service.findByGridRequest3(gridRequest));
    }


    /**
     * 分页条件查询-查询可用的管理员(周佳林)
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        return Result.success(service.login(user));
    }

    /**
     * 查询原密码是否正确
     */
    @PostMapping("/checkPassword")
    public Result checkPassword(@MultiRequestBody String password) {
        return Result.success(service.checkPassword(password));
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    @OptLogAnnotation(title = "用户管理", businessType = BusinessType.OTHER)
    public Result updatePassword(@MultiRequestBody String oldPassword, @MultiRequestBody String password) {
        service.updatePassword(oldPassword, password);
        return Result.success();
    }

    @PostMapping("/findByCheck")
    public Result findByCheck(@MultiRequestBody User user) {
        return Result.success(service.findByCheck(user));
    }

    /**
     * 修改个人信息
     */
    @PutMapping("/updateInfo")
    @OptLogAnnotation(title = "用户管理", businessType = BusinessType.OTHER)
    public Result updateInfo(@MultiRequestBody User user) {
        service.updateInfo(user);
        return Result.success();
    }
}
