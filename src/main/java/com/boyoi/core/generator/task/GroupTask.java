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
 * 验证组 任务
 *
 * @author ZhouJL
 * @date 2019/2/14 10:12
 */
public class GroupTask extends AbstractTask {

    public GroupTask(String className, String moduleName) {
        super(className, moduleName);
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 生成Service填充数据
        System.out.println("Generating " + className + "Group.java");
        Map<String, String> groupData = new HashMap<>(8);
        groupData.put("BasePackageName", ConfigUtil.getConfiguration().getPackageName());
        groupData.put("GroupPackageName", ConfigUtil.getConfiguration().getPath().getGroup());
        groupData.put("Author", ConfigUtil.getConfiguration().getAuthor());
        groupData.put("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        groupData.put("ClassName", className);
        groupData.put("moduleName", moduleName);
        groupData.put("EntityName", StringUtil.firstToLowerCase(className));
        String filePath = StringUtil.package2Path(ConfigUtil.getConfiguration().getFileUrl()) + "//java//" + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName()) + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getGroup());
        String fileName = className + "Group.java";

        // 生成Group文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_GROUP, groupData, filePath + fileName);
    }
}
