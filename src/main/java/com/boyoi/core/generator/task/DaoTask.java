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
 * dao 任务
 *
 * @author ZhouJL
 * @date 2019/2/14 10:12
 */
public class DaoTask extends AbstractTask {

    public DaoTask(String className, String moduleName) {
        super(className, moduleName);
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 生成Dao填充数据
        System.out.println("Generating " + className + "Dao.java");
        Map<String, String> daoData = new HashMap<>(8);
        daoData.put("BasePackageName", ConfigUtil.getConfiguration().getPackageName());
        daoData.put("DaoPackageName", ConfigUtil.getConfiguration().getPath().getDao());
        daoData.put("EntityPackageName", ConfigUtil.getConfiguration().getPath().getEntity());
        daoData.put("Author", ConfigUtil.getConfiguration().getAuthor());
        daoData.put("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        daoData.put("ClassName", className);
        daoData.put("moduleName", moduleName);
        daoData.put("EntityName", StringUtil.firstToLowerCase(className));
        String filePath = StringUtil.package2Path(ConfigUtil.getConfiguration().getFileUrl()) + "//java//" + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName()) + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getDao());
        String fileName = className + "Dao.java";
        // 生成dao文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_DAO, daoData, filePath + fileName);
    }
}
