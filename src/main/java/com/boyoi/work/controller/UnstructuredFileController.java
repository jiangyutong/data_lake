package com.boyoi.work.controller;

import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.core.util.FtpUtil;
import com.boyoi.work.entity.UnstructuredFile;
import com.boyoi.work.service.UnstructuredFileService;
import com.boyoi.work.groups.UnstructuredFileGroup;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 描述：
 * 文件上传存储信息表 控制层
 *
 * @author Xiaopeng
 * @date 2020-10-22
 */
@RestController
@RequestMapping(value = "/unstructuredFile")
@Slf4j
public class UnstructuredFileController extends BaseController<UnstructuredFile, UnstructuredFileService> {

    @Resource
    protected UnstructuredFileService service;

    /**
     * 添加(肖鹏)
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "新增文件上传存储信息", businessType = BusinessType.INSERT)
    public Result add(@RequestBody Map<String, Object> map) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("fname");
        for (Map<String, Object> mmp : list) {
            UnstructuredFile unstructuredFile = new UnstructuredFile();
            unstructuredFile.setFtype(map.get("ftype").toString());
            unstructuredFile.setKeywords(map.get("keywords").toString());
            unstructuredFile.setFname(mmp.get("fname").toString());
            unstructuredFile.setFsize(Integer.valueOf(mmp.get("size").toString()));
            unstructuredFile.setFaddress(mmp.get("faddess").toString());
            service.add(unstructuredFile);
        }
        return Result.success();
    }

    /**
     * 修改(肖鹏)
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "修改文件上传存储信息", businessType = BusinessType.UPDATE)
    public Result update(@Validated(value = UnstructuredFileGroup.Update.class) @RequestBody UnstructuredFile unstructuredFile) {
        return Result.success(service.update(unstructuredFile));
    }

    /**
     * 根据主键删除(肖鹏)
     */
    @DeleteMapping("/del")
    @OptLogAnnotation(title = "删除文件上传存储信息", businessType = BusinessType.DELETE)
    public Result del(@Validated(value = UnstructuredFileGroup.Del.class) @RequestBody UnstructuredFile unstructuredFile) {
        return Result.success(service.del(unstructuredFile));
    }

    /**
     * 根据主键查询(肖鹏)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody UnstructuredFile unstructuredFile) {
        return Result.success(service.findById(unstructuredFile.getGuid()));
    }

    /**
     * 分页条件查询(肖鹏)
     */
    @PostMapping("/list")
    public Result list(@RequestBody EasyGridRequest gridRequest) {
        return Result.success(service.findByGridRequest(gridRequest));
    }

    /**
     * 重复数据校验(肖鹏)
     */
    @PostMapping("/check")
    public Result check(@RequestBody UnstructuredFile unstructuredFile) {
        return Result.success(service.findByCheck(unstructuredFile));
    }

    /**
     * 首页表数据信息查询
     *
     * @return
     */
    @GetMapping("/query")
    public Result queryData() {
        return Result.success(service.queryData());
    }

    /**
     * 下载文件
     *
     * @param response 响应
     * @param param    obj
     */
    @PostMapping("/downData")
    public void downData(@RequestBody Map<String, String> param, HttpServletResponse response) {
        FtpUtil.downloadFile(param.get("filePath"), response);
    }
}
