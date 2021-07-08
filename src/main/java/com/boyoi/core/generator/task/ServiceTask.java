package com.boyoi.core.generator.task;


import com.boyoi.core.generator.task.base.AbstractTask;
import com.boyoi.core.generator.utils.ConfigUtil;
import com.boyoi.core.generator.utils.FileUtil;
import com.boyoi.core.generator.utils.FreemarketConfigUtils;
import com.boyoi.core.generator.utils.StringUtil;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * service 任务
 *
 * @author ZhouJL
 * @date 2019/2/14 10:13
 */
public class ServiceTask extends AbstractTask {

    public ServiceTask(String className, String moduleName) {
        super(className, moduleName);
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 生成Service填充数据
        System.out.println("Generating " + className + "Service.java");
        Map<String, String> serviceData = new HashMap<>(10);
        serviceData.put("BasePackageName", ConfigUtil.getConfiguration().getPackageName());
        serviceData.put("ServicePackageName", ConfigUtil.getConfiguration().getPath().getService());
        serviceData.put("EntityPackageName", ConfigUtil.getConfiguration().getPath().getEntity());
        serviceData.put("DaoPackageName", ConfigUtil.getConfiguration().getPath().getDao());
        serviceData.put("Author", ConfigUtil.getConfiguration().getAuthor());
        serviceData.put("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        serviceData.put("ClassName", className);
        serviceData.put("moduleName", moduleName);
        serviceData.put("EntityName", StringUtil.firstToLowerCase(className));
        String filePath = StringUtil.package2Path(ConfigUtil.getConfiguration().getFileUrl()) + "//java//" + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName()) + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getService());
        String fileName = className + "Service.java";

        // 生成Service文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_SERVICE, serviceData, filePath + fileName);
    }
}
