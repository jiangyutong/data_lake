package com.boyoi.work.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.controller.BaseController;
import com.boyoi.core.entity.CommonException;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.work.entity.QueryLog;
import com.boyoi.work.service.QueryLogService;
import com.boyoi.work.groups.QueryLogGroup;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述：
 * 数据写入日志 控制层
 *
 * @author fuwp
 * @date 2020-10-22
 */
@RestController
@RequestMapping(value = "/queryLog")
@Slf4j
public class QueryLogController extends BaseController<QueryLog, QueryLogService> {

    @Resource
    protected QueryLogService service;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 添加(付文攀)
     */
    @PostMapping("/add")
    public Result add(@Validated(value = QueryLogGroup.Add.class) @RequestBody QueryLog queryLog) {
        return Result.success(service.add(queryLog));
    }

    /**
     * 修改(付文攀)
     */
    @PutMapping("/update")
    public Result update(@Validated(value = QueryLogGroup.Update.class) @RequestBody QueryLog queryLog) {
        return Result.success(service.update(queryLog));
    }

    /**
     * 根据主键删除(付文攀)
     */
    @DeleteMapping("/del")
    public Result del(@Validated(value = QueryLogGroup.Del.class) @RequestBody QueryLog queryLog) {
        return Result.success(service.del(queryLog));
    }

    /**
     * 根据主键查询(付文攀)
     */
    @PostMapping("/detail")
    public Result detail(@RequestBody QueryLog queryLog) {
        return Result.success(service.findById(queryLog.getGuid()));
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
    public Result check(@RequestBody QueryLog queryLog) {
        return Result.success(service.findByCheck(queryLog));
    }

    @GetMapping("/downData/{guid}")
    public Result downData(@PathVariable String guid) throws IOException {
        QueryLog byId = service.findById(guid);
        if (byId == null || !"C".equalsIgnoreCase(byId.getState())) {
            throw new CommonException(ResultCode.QUERY_LOG_NOT_EXIST);
        }
        String data = redisUtils.get("data:lake:query:" + guid);
        if (StrUtil.isBlank(data)) {
            throw new CommonException(ResultCode.RESULT_NOT_EXIST);
        }
        ObjectMapper om = new ObjectMapper();
        CollectionLikeType type = om.getTypeFactory().constructCollectionLikeType(List.class, Map.class);
        List<Map<String, Object>> list = om.readValue(data, type);
        return Result.success(list);
    }

    @GetMapping("/downFile/{guid}/{format}")
    public void downFile(@PathVariable String guid, @PathVariable String format, HttpServletResponse response) throws IOException {
        QueryLog byId = service.findById(guid);
        if (byId == null || !"C".equalsIgnoreCase(byId.getState())) {
            throw new CommonException(ResultCode.QUERY_LOG_NOT_EXIST);
        }
        String data = redisUtils.get("data:lake:query:" + guid);
        if (StrUtil.isBlank(data)) {
            throw new CommonException(ResultCode.RESULT_NOT_EXIST);
        }
        if ("xls".equals(format)) {
            ObjectMapper om = new ObjectMapper();
            CollectionLikeType type = om.getTypeFactory().constructCollectionLikeType(List.class, Map.class);
            List<Map<String, Object>> list = om.readValue(data, type);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode(guid, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<String> headers = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(list)) {
                Map<String, Object> one = list.get(0);
                one.forEach((key, value) -> {
                    headers.add(key);
                });
            }
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("sheet");
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
                cell.setCellValue(text);
            }
            int rowNum = 1;
            for (Map<String, Object> map : list) {
                HSSFRow row1 = sheet.createRow(rowNum);
                AtomicInteger j = new AtomicInteger();
                map.forEach((key, value) -> {
                    row1.createCell(j.get()).setCellValue(String.valueOf(map.get(key)));
                    j.getAndIncrement();
                });
                rowNum++;
            }
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } else {
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode(guid, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".json");
            response.getOutputStream().write(data.getBytes());
            response.flushBuffer();
        }
    }

    /**
     * 首页查询记录数据查询
     *
     * @return
     */
    @GetMapping("/query")
    public Result queryData() {
        return Result.success(service.queryData());
    }
}
