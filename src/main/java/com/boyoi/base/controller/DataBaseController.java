package com.boyoi.base.controller;

import cn.hutool.core.util.StrUtil;
import com.boyoi.base.service.DataBaseService;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author fuwp
 */
@RestController
@RequestMapping(value = "/dataBase")
@Slf4j
public class DataBaseController {
    @Resource
    private DataBaseService dataBaseService;


    /**
     * 分页条件查询
     */
    @PostMapping("/list/{type}")
    public Result list(@RequestBody EasyGridRequest gridRequest, @PathVariable String type) {
        String t = gridRequest.getMap2().get("type");
        if (StrUtil.isBlank(t)) {
            gridRequest.getMap2().put("type", type);
        }
        return Result.success(dataBaseService.findByGridRequest(gridRequest));
    }

    @PostMapping("/showData")
    public Result showData(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(dataBaseService.showData(gridRequest));
    }

}
