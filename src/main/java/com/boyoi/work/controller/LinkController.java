package com.boyoi.work.controller;

import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.work.entity.Link;
import com.boyoi.work.service.LinkService;
import com.boyoi.work.groups.LinkGroup;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 描述：
 * 数据链接 控制层
 *
 * @author Harvey ylq
 * @date 2020-09-17
 */
@RestController
@RequestMapping(value = "/link")
@Slf4j
public class LinkController extends BaseController<Link, LinkService> {

    @Resource
    protected LinkService service;

    /**
     * 添加(杨龙全)
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "新增数据链接", businessType = BusinessType.INSERT)
    public Result add(@Validated(value = LinkGroup.Add.class) @RequestBody Link link) {
        int i = service.add(link);
        if (i == 1) {
            return Result.success();
        } else if (i == 2) {
            return Result.failure(ResultCode.DATA_LINK_FAILURE);
        } else {
            return Result.failure();
        }
    }

    /**
     * 修改(杨龙全)
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "修改数据链接", businessType = BusinessType.UPDATE)
    public Result update(@RequestBody Link link) {
        return Result.success(service.update(link));
    }

    /**
     * 根据条件更新实体
     *
     * @param link
     * @return
     */
    @PutMapping("/updateByNotEmpty")
    @OptLogAnnotation(title = "修改数据链接", businessType = BusinessType.UPDATE)
    public Result updateByNotEmpty(@RequestBody Link link) {
        int i = service.updateByNotEmpty(link);
        if (i == 1) {
            return Result.success();
        } else if (i == 2) {
            return Result.failure(ResultCode.DATA_LINK_FAILURE);
        } else {
            return Result.failure();
        }
    }

    /**
     * 根据主键删除(杨龙全)
     */
    @DeleteMapping("/del")
    @OptLogAnnotation(title = "删除数据链接", businessType = BusinessType.DELETE)
    public Result del(@Validated(value = LinkGroup.Del.class) @RequestBody Link link) {
        return Result.success(service.del(link));
    }

    /**
     * 根据主键查询(杨龙全)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody Link link) {
        return Result.success(service.findById(link.getGuid()));
    }

    /**
     * 分页条件查询(杨龙全)
     */
    @PostMapping("/list")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(service.findByGridRequest(gridRequest));
    }

    /**
     * 重复数据校验(杨龙全)
     */
    @PostMapping("/check")
    public Result check(@RequestBody Link link) {
        return Result.success(service.findByCheck(link));
    }

    /**
     * 校验链接别名是否存在
     *
     * @param link
     * @return
     */
    @PostMapping("/checkLinkName")
    public Result checkLinkName(@RequestBody Link link) {
        return Result.success(service.findByDomain(link));
    }

    /**
     * 复制目标数据库的表
     *
     * @param map
     * @return
     */
    @PostMapping("/copyTable")
    public Result copyTable(@RequestBody Map<String, Object> map) {
        int i = service.copyTable(map);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    /**
     * 修改链接数据验证
     *
     * @param link
     * @return
     */
    @PostMapping("/findBydum")
    public Result findBydum(@RequestBody Link link) {
        return Result.success(service.findBydum(link));
    }

    /**
     * 查询未复制表的信息
     *
     * @param map
     * @return
     */
    @PostMapping("/getNotCopyTable")
    public Result getNotCopyTable(@RequestBody Map<String, Object> map) {
        return Result.success(service.getNotCopyTable(map));
    }

    /**
     * 查询所有链接信息
     *
     * @return
     */
    @PostMapping("/getLinkAll")
    public Result getLinkAll() {
        Link link = new Link();
        link.setOptPerson(RedisUtils.getLoginUser().getStr("guid"));
        return Result.success(service.findByDomain(link));
    }

    /**
     * 查询所有链接信息
     *
     * @return
     */
    @PostMapping("/linkAllList")
    public Result linkAllList() {
        Link link = new Link();
        return Result.success(service.findByDomain(link));
    }
}
