package com.boyoi.work.mq.receiver;

import cn.hutool.core.util.BooleanUtil;
import com.boyoi.base.dao.DataBaseDao;
import com.boyoi.base.service.DataBaseService;
import com.boyoi.core.entity.ApiData;
import com.boyoi.core.entity.CommonException;
import com.boyoi.core.entity.DataState;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.enums.TableType;
import com.boyoi.core.util.FtpUtil;
import com.boyoi.work.entity.NewWriteApi;
import com.boyoi.work.entity.WriteApi;
import com.boyoi.work.entity.WriteLog;
import com.boyoi.work.mq.QueueConstant;
import com.boyoi.work.service.NewWriteApiService;
import com.boyoi.work.service.WriteApiService;
import com.boyoi.work.service.WriteLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.google.protobuf.Api;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author fuwp
 */
@Slf4j
@Component
@RabbitListener(queues = QueueConstant.WRITE_QUEUE)
public class WriteQueueHandler {
    @Resource
    private DataBaseService dataBaseService;
    @Resource
    private WriteApiService writeApiService;
    @Resource
    private WriteLogService writeLogService;
    @Resource
    private NewWriteApiService newWriteApiService;
    @Resource
    private DataBaseDao dataBaseDao;

    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void process(String msg, Channel channel, Message message) throws IOException {
        try {
            //这里从原来的写入改成了现在的写入
//            process(msg);
            newProcess(msg);
            //消费消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            //拒收消息
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    /**
     * 这里需要做一个映射得到最终的写入结果
     *
     * @param msg
     * @throws Exception
     */
    private void newProcess(String msg) throws Exception {
        ObjectMapper om = new ObjectMapper();
        ApiData apiData = om.readValue(msg, ApiData.class);
        String uuid = apiData.getUuid();
        String userId = apiData.getUserId();
        String data = apiData.getData();

        WriteLog writeLog = writeLogService.findById(uuid);
        String apiUrl = writeLog.getApiUrl();
        NewWriteApi newWriteApi = newWriteApiService.findByUrl(apiUrl, userId);
        if (newWriteApi == null) {
            writeLog.setState("C");
            writeLog.setErr(om.writeValueAsString(new DataState(ResultCode.API_URL_NULL)));
        } else if (!BooleanUtil.toBoolean(newWriteApi.getState())) {
            writeLog.setState("C");
            writeLog.setErr(om.writeValueAsString(new DataState(ResultCode.API_DEACTIVATE)));
        } else {
            //接口可用
            writeLog.setApiId(newWriteApi.getGuid());
            String upload = FtpUtil.upload(data, "writedata", uuid + ".json");
            writeLog.setDataPath(upload);
            try {
                CollectionLikeType type = om.getTypeFactory().constructCollectionLikeType(List.class, Map.class);
                List<Map<String, Object>> list = om.readValue(data, type);
                List<Map<String, Object>> resList = fieldMapperFunc(list, newWriteApi.getTemplateName(), newWriteApi.getMapperGuid());
                dataBaseService.batchInsert(TableType.TEMPLATE_DATA, newWriteApi.getTemplateName(), resList);
                writeLog.setState("B");
                writeLog.setLine(list.size());
            } catch (Exception e) {
                e.printStackTrace();
                writeLog.setState("D");
                if (e instanceof CommonException) {
                    e.printStackTrace();
                    CommonException commonException = (CommonException) e;
                    writeLog.setErr(om.writeValueAsString(new DataState((commonException.getResultCode()))));
                } else {
                    String message = e.getMessage();
                    writeLog.setErr(om.writeValueAsString(new DataState(ResultCode.WRITE_DB_ERR, message)));
                }
            }
        }
        writeLog.setOptTime(new Date());
        writeLogService.updateByNotEmpty(writeLog);
    }

    //这里是字段映射，将list的key 映射到 mapper 的key
    private List<Map<String, Object>> fieldMapperFunc(List<Map<String, Object>> list, String templateName, String mapperGuid) {
        Map<String, Object> mapper = dataBaseDao.findByGuid(TableType.TEMPLATE_FIELD_MAPPER, templateName, "local_guid", mapperGuid);
        List<Map<String, Object>> resultList = new ArrayList<>();
        list.forEach(map -> {
            Map<String, Object> resultMap = new HashMap<>();
            mapper.forEach((key, value) -> {
                if (map.containsKey(value.toString())) {
                    resultMap.put(key, map.get(value.toString()));
                }
            });
            resultList.add(resultMap);
        });
        return resultList;
    }


    private void process(String msg) throws Exception {
        ObjectMapper om = new ObjectMapper();
        //反序列化消息
        ApiData apiData = om.readValue(msg, ApiData.class);
        String uuid = apiData.getUuid();
        String userId = apiData.getUserId();
        String data = apiData.getData();
        //日志
        WriteLog writeLog = writeLogService.findById(uuid);
        //url
        String apiUrl = writeLog.getApiUrl();
        //api对象
        WriteApi writeApi = writeApiService.findByUrl(apiUrl, userId);
        if (writeApi == null) {
            //没找到接口
            writeLog.setState("C");
            writeLog.setErr(om.writeValueAsString(new DataState(ResultCode.API_URL_NULL)));
        } else if (!BooleanUtil.toBoolean(writeApi.getState())) {
            //接口已停用
            writeLog.setState("C");
            writeLog.setErr(om.writeValueAsString(new DataState(ResultCode.API_DEACTIVATE)));
        } else {
            writeLog.setApiId(writeApi.getGuid());
            //写入文件 (这里真的有必要吗 ？？？)
            String upload = FtpUtil.upload(data, "writedata", uuid + ".json");
            writeLog.setDataPath(upload);
            try {
                //类型转换
                CollectionLikeType type = om.getTypeFactory().constructCollectionLikeType(List.class, Map.class);
                List<Map<String, Object>> list = om.readValue(data, type);
                //批量添加
                dataBaseService.batchInsert(writeApi.getTableType(), writeApi.getTableName(), list);
                writeLog.setState("B");
                writeLog.setLine(list.size());
            } catch (Exception e) {
                e.printStackTrace();
                //运行异常
                writeLog.setState("D");
                if (e instanceof CommonException) {
                    e.printStackTrace();
                    CommonException commonException = (CommonException) e;
                    writeLog.setErr(om.writeValueAsString(new DataState(commonException.getResultCode())));
                } else {
                    String message = e.getMessage();
                    writeLog.setErr(om.writeValueAsString(new DataState(ResultCode.WRITE_DB_ERR, message)));
                }
            }
        }
        writeLog.setOptTime(new Date());
        writeLogService.updateByNotEmpty(writeLog);
    }
}
