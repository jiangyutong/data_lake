package com.boyoi.base.controller;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import com.boyoi.base.service.DataBaseService;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.ApiData;
import com.boyoi.core.entity.CommonException;
import com.boyoi.core.entity.Result;
import com.boyoi.core.entity.User;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.enums.TableType;
import com.boyoi.work.entity.NewWriteApi;
import com.boyoi.work.entity.QueryLog;
import com.boyoi.work.entity.WriteApi;
import com.boyoi.work.entity.WriteLog;
import com.boyoi.work.mq.ProduceMessage;
import com.boyoi.work.mq.QueueConstant;
import com.boyoi.work.service.NewWriteApiService;
import com.boyoi.work.service.QueryLogService;
import com.boyoi.work.service.WriteApiService;
import com.boyoi.work.service.WriteLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author fuwp
 */
@RestController
public class DataController {
    @Resource
    private ProduceMessage produceMessage;
    @Resource
    private WriteLogService writeLogService;
    @Resource
    private QueryLogService queryLogService;
    @Resource
    private WriteApiService writeApiService;
    @Resource
    private NewWriteApiService newWriteApiService;
    @Resource
    private DataBaseService dataBaseService;

    /**
     * 数据写入
     *
     * @param data   数据
     * @param apiUrl 写入地址
     * @return r
     */
    @PostMapping("/write/{apiUrl}")
    public Result write(@RequestBody String data, @PathVariable String apiUrl) {
        String uuid = IdUtil.simpleUUID();
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        NewWriteApi newWriteApi = newWriteApiService.findByUrl(apiUrl, guid);
        if (newWriteApi == null) {
            throw new CommonException(ResultCode.API_URL_NULL);
        } else if (!BooleanUtil.toBoolean(newWriteApi.getState())) {
            throw new CommonException(ResultCode.API_DEACTIVATE);
        } else {
            boolean existTable = dataBaseService.isExistTable(newWriteApi.getTemplateName(), TableType.TEMPLATE_DATA);
            if (!existTable) {
                throw new CommonException(ResultCode.TEMPLATE_DATA_TABLE_NOT_EXIST);
            }
        }
        ApiData apiData = new ApiData();
        apiData.setUuid(uuid);
        apiData.setUserId(guid);
        apiData.setData(data);
        ObjectMapper mapper = new ObjectMapper();
        try {
            //写入日志
            WriteLog writeLog = new WriteLog();
            User user = RedisUtils.getThisUser();
            writeLog.setOptPerson(Objects.requireNonNull(user).getUserName());
            writeLog.setCreateUser(user.getGuid());
            writeLog.setGuid(uuid);
            writeLog.setApiUrl(apiUrl);
            writeLogService.add(writeLog);
            //写入消息队列
            produceMessage.sendMessage(QueueConstant.WRITE_EXCHANGE, QueueConstant.WRITE_QUEUE, mapper.writeValueAsString(apiData));
            return Result.success(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(ResultCode.WRITE_TASK_ERR);
        }
    }


    /**
     * 数据查询
     *
     * @param data   数据
     * @param apiUrl 写入地址
     * @return r
     */
    @PostMapping("/query/{apiUrl}")
    public Result query(@RequestBody String data, @PathVariable String apiUrl) {
        String uuid = IdUtil.simpleUUID();
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        ApiData apiData = new ApiData();
        apiData.setUuid(uuid);
        apiData.setUserId(guid);
        apiData.setData(data);
        ObjectMapper mapper = new ObjectMapper();
        try {
            //写入日志
            QueryLog queryLog = new QueryLog();
            User user = RedisUtils.getThisUser();
            queryLog.setGuid(uuid);
            queryLog.setOptPerson(Objects.requireNonNull(user).getUserName());
            queryLog.setCreateUser(user.getGuid());
            queryLog.setApiUrl(apiUrl);
            queryLog.setParameter(data);
            queryLogService.add(queryLog);
            produceMessage.sendMessage(QueueConstant.QUERY_EXCHANGE, QueueConstant.QUERY_QUEUE, mapper.writeValueAsString(apiData));
            return Result.success(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(ResultCode.WRITE_TASK_ERR);
        }
    }
}
