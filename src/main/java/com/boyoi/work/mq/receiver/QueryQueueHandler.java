package com.boyoi.work.mq.receiver;

import cn.hutool.core.util.BooleanUtil;
import com.boyoi.base.service.DataBaseService;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.ApiData;
import com.boyoi.core.entity.CommonException;
import com.boyoi.core.entity.DataState;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.work.entity.QueryApi;
import com.boyoi.work.entity.QueryLog;
import com.boyoi.work.mq.QueueConstant;
import com.boyoi.work.service.QueryApiService;
import com.boyoi.work.service.QueryLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fuwp
 */
@Slf4j
@Component
@RabbitListener(queues = QueueConstant.QUERY_QUEUE)
public class QueryQueueHandler {
    @Resource
    private DataBaseService dataBaseService;
    @Resource
    private QueryApiService queryApiService;
    @Resource
    private QueryLogService queryLogService;
    @Resource
    private RedisUtils redisUtils;

    @RabbitHandler
    public void process(String msg, Channel channel, Message message) throws IOException {
        try {
            process(msg);
            //消费消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            //拒收消息
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    private void process(String msg) throws Exception {
        ObjectMapper om = new ObjectMapper();
        //反序列化消息
        ApiData apiData = om.readValue(msg, ApiData.class);
        String uuid = apiData.getUuid();
        String data = apiData.getData();
        String userId = apiData.getUserId();
        //日志
        QueryLog queryLog = queryLogService.findById(uuid);
        //url
        String apiUrl = queryLog.getApiUrl();
        //api对象
        QueryApi queryApi = queryApiService.findByUrl(apiUrl, userId);
        if (queryApi == null) {
            //没找到接口
            queryLog.setState("D");
            queryLog.setErr(om.writeValueAsString(new DataState(ResultCode.API_URL_NULL)));
        } else if (!BooleanUtil.toBoolean(queryApi.getState())) {
            //接口已停用
            queryLog.setState("D");
            queryLog.setErr(om.writeValueAsString(new DataState(ResultCode.API_DEACTIVATE)));
        } else {
            queryLog.setApiId(queryApi.getGuid());
            queryLog.setOptTime(new Date());
            try {
                //设置状态为查询中
                queryLog.setState("B");
                queryLogService.updateByNotEmpty(queryLog);
                //类型转换
                MapType mapType = om.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
                Map<String, Object> parameter = om.readValue(data, mapType);
                //查询
                List<Map<String, Object>> query = dataBaseService.query(queryApi.getIBoundSql(), parameter);
                redisUtils.set("data:lake:query:" + uuid, om.writeValueAsString(query), 3 * 24 * 60 * 60);
                queryLog = queryLogService.findById(uuid);
                queryLog.setState("C");
                queryLog.setLine(query.size());
            } catch (Exception e) {
                queryLog = queryLogService.findById(uuid);
                e.printStackTrace();
                //运行异常
                queryLog.setState("D");
                if (e instanceof CommonException) {
                    CommonException commonException = (CommonException) e;
                    queryLog.setErr(om.writeValueAsString(new DataState(commonException.getResultCode())));
                } else {
                    queryLog.setErr(om.writeValueAsString(new DataState(ResultCode.QUERY_DB_ERR, e.getMessage())));
                }
            }
        }
        queryLog.setOptTime(new Date());
        queryLogService.updateByNotEmpty(queryLog);
    }
}
