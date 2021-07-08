package com.boyoi.work.controller;

import cn.hutool.core.util.StrUtil;
import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.enums.BusinessType;
import com.boyoi.work.entity.LinkTable;
import com.boyoi.work.entity.TemplateTable;
import com.boyoi.work.service.TemplateTableService;
import com.boyoi.work.groups.LinkTableGroup;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 描述：
 * 模板表信息 控制层
 *
 * @author LiYucheng
 * @date 2021-04-21
 */
@RestController
@RequestMapping(value = "/templateTable")
@Slf4j
public class TemplateTableController extends BaseController<TemplateTable, TemplateTableService> {
    @Resource
    protected TemplateTableService service;

    /**
     * 添加
     */
    @PostMapping("/add")
    @OptLogAnnotation(title = "新增模板表信息", businessType = BusinessType.INSERT)
    public Result add(@RequestBody Map<String, Object> map) {
        return Result.success(service.add(map));
    }

    /**
     * 修改表的信息，这里可能会顺带删除一些表，比较复杂，后面再实现吧
     *
     * @param map
     * @return
     */
    @PutMapping("/update")
    @OptLogAnnotation(title = "修改表信息", businessType = BusinessType.UPDATE)
    public Result update(@RequestBody Map<String, Object> map) {
        return Result.success(service.update(map));
    }

    /**
     * 根据guid去获得template_info的具体数据
     *
     * @param templateTable
     * @return
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody TemplateTable templateTable) {
        return Result.success(service.findDetail(templateTable.getGuid()));
    }

    /**
     * 根据某一个关键字去搜索template_info
     *
     * @param gridRequest
     * @param type
     * @return
     */
    @PostMapping("/list/{type}")
    public Result list(@RequestBody EasyGridRequest gridRequest, @PathVariable String type) {
        if (StrUtil.isNotBlank(type)) {
            gridRequest.getMap2().put("type", type);
        }
        return Result.success(service.findByGridRequest(gridRequest));
    }

    @PostMapping("/getRelatedTables")
    public Result getRelatedTables(@RequestBody Map<String, Object> data) {
        System.out.println(data.toString());
        EasyGridRequest gridRequest = new EasyGridRequest();

        gridRequest.setPage(String.valueOf(((LinkedHashMap) data.get("gridRequest")).get("page")));
        gridRequest.setMap2((Map<String, String>) ((LinkedHashMap) data.get("gridRequest")).get("map2"));
        gridRequest.setRows(String.valueOf(((LinkedHashMap) data.get("gridRequest")).get("rows")));
        return Result.success(service.getFieldMapperInfo((Serializable) data.get("guid"), gridRequest));
    }

    @PostMapping("/getFiledMapper")
    public Result getFiledMapper(@RequestBody Map<String, String> map) {
        return Result.success(service.getmapper(map));
    }


    /**
     * 重复数据校验
     *
     * @param linkTable
     * @return
     */
    @PostMapping("/check")
    public Result check(@RequestBody LinkTable linkTable) {
        return Result.success(service.findByCheck(linkTable));
    }

    /**
     * 分页条件查询，可能有多个type
     *
     * @param gridRequest
     * @return
     */
    @PostMapping("/listto")
    public Result listto(@RequestBody EasyGridRequest gridRequest) {
        Map<String, String> mmp = gridRequest.getMap2();
        mmp.put("optPerson", RedisUtils.getLoginUser().getStr("guid"));
        gridRequest.setMap2(mmp);
        return Result.success(service.findByGridRequest(gridRequest));
    }

    /**
     * 按照guid查询结果
     *
     * @param linkTable
     * @return
     */
    @PostMapping("/findDetail")
    public Result findDetail(@RequestBody LinkTable linkTable) {
        return Result.success(service.findDetail(linkTable.getGuid()));
    }

    /**
     * 这个好像没什么用
     *
     * @return
     */
    @GetMapping("/query")
    public Result queryData() {
        LinkTable linkTable = new LinkTable();
        return Result.success(service.findByCheck(linkTable));
    }

    @PostMapping("/templateData/{templateGuid}")
    public Result templateData(@RequestBody EasyGridRequest gridRequest, @PathVariable String templateGuid) {
        return Result.success(service.getTemplateData(gridRequest, templateGuid));
    }

    @DeleteMapping("/del")
    public Result del(@RequestBody TemplateTable templateTable) {
        return Result.success(service.del(templateTable));
    }
}
