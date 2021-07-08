package com.boyoi.core.generator.task;

import com.boyoi.core.generator.entity.ColumnInfo;
import com.boyoi.core.generator.task.base.AbstractTask;
import com.boyoi.core.generator.utils.*;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 国际化资源文件 任务
 *
 * @author ZhouJL
 * @date 2019/2/14 10:12
 */
public class MessageTask extends AbstractTask {

    public MessageTask(String className, String moduleName, List<ColumnInfo> infos) {
        super(className, null, null, null, infos, moduleName);
    }

    @Override
    public void run() throws IOException, TemplateException {

        Map<String, String> messageData = new HashMap<>(10);
        messageData.put("BasePackageName", ConfigUtil.getConfiguration().getPackageName());
        messageData.put("GroupPackageName", ConfigUtil.getConfiguration().getPath().getGroup());
        messageData.put("Author", ConfigUtil.getConfiguration().getAuthor());
        messageData.put("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        messageData.put("properties", GeneratorUtil.generateMessage(tableInfos, className, moduleName));
        messageData.put("propertiesEN", GeneratorUtil.generateMessageEn(tableInfos, className, moduleName));
        String filePath = StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName()) + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getMessage());
        // 生成message填充数据
        System.out.println("Generating " + className + ".properties");
        String fileName = className + ".properties";
        // 生成Group文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_MESSAGE, messageData, filePath + fileName);
        System.out.println("Generating " + className + "_zh_CN.properties");
        fileName = className + "_zh_CN.properties";
        // 生成Group文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_MESSAGE, messageData, filePath + fileName);
        System.out.println("Generating " + className + "_en_US.properties");
        fileName = className + "_en_US.properties";
        // 生成Group文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_MESSAGE_EN, messageData, filePath + fileName);
    }
}
