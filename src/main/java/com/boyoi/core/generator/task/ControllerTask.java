package com.boyoi.core.generator.task;

import com.boyoi.core.generator.entity.ColumnInfo;
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
import java.util.List;
import java.util.Map;

/**
 * controller 任务
 *
 * @author ZhouJL
 * @date 2019/2/14 10:11
 */
public class ControllerTask extends AbstractTask {

    public ControllerTask(String className, String moduleName, List<ColumnInfo> infos) {
        super(className, null, null, null, infos, moduleName);
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 生成Controller填充数据
        System.out.println("Generating " + className + "Controller.java");
        Map<String, String> controllerData = new HashMap<>(12);
        controllerData.put("BasePackageName", ConfigUtil.getConfiguration().getPackageName());
        controllerData.put("ControllerPackageName", ConfigUtil.getConfiguration().getPath().getController());
        controllerData.put("ServicePackageName", ConfigUtil.getConfiguration().getPath().getService());
        controllerData.put("EntityPackageName", ConfigUtil.getConfiguration().getPath().getEntity());
        controllerData.put("GroupPackageName", ConfigUtil.getConfiguration().getPath().getGroup());
        controllerData.put("Author", ConfigUtil.getConfiguration().getAuthor());
        controllerData.put("PrimaryKey", getPrimaryKeyColumnInfo(tableInfos).getColumnName());
        controllerData.put("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        controllerData.put("ClassName", className);
        controllerData.put("moduleName", moduleName);
        controllerData.put("authorZH", ConfigUtil.getConfiguration().getAuthorZh());
        controllerData.put("EntityName", StringUtil.firstToLowerCase(className));
        String filePath = StringUtil.package2Path(ConfigUtil.getConfiguration().getFileUrl()) + "//java//" + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName()) + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getController());
        String fileName = className + "Controller.java";
        // 生成Controller文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_CONTROLLER, controllerData, filePath + fileName);
    }

    private ColumnInfo getPrimaryKeyColumnInfo(List<ColumnInfo> list) {
        for (ColumnInfo columnInfo : list) {
            if (columnInfo.isPrimaryKey()) {
                return columnInfo;
            }
        }
        return null;
    }
}
