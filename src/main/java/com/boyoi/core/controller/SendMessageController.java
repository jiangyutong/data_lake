package com.boyoi.core.controller;

import com.boyoi.core.entity.Result;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.util.FtpUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author :
 * @CreateTime :
 * @Description :
 **/
@RestController
@RequestMapping("/pushAdd")
public class SendMessageController {

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法ddd

    @PostMapping("/sendDirectMessage")
    public Result sendDirectMessage(@RequestParam("files") MultipartFile[] files, String folder) {
        Assert.notNull(files, ResultCode.DATA_FILE_BLANK.getMessage());
        Assert.isTrue(files.length != 0, ResultCode.DATA_FILE_BLANK.getMessage());
        Assert.isTrue(files.length <= 5, ResultCode.DATA_IS_MAX.getMessage());
        for (MultipartFile file : files) {
            Assert.isTrue(file.getSize() > 0, ResultCode.DATA_FILE_NULL.getMessage());
        }
        folder = "files";
        Assert.notNull(folder, ResultCode.DATA_FOLDER_NULL.getMessage());
        //  Result.success(FtpUtil.uploadFile(folder, files));
        return Result.success(FtpUtil.uploadFile(folder, files));



        /*String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        return "ok";*/
    }
}