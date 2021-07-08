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
 * 接口实现类任务
 *
 * @author ZhouJL
 * @date 2019/2/14 10:12
 */
public class InterfaceTask extends AbstractTask {

    public InterfaceTask(String className, List<ColumnInfo> tableInfos, String moduleName) {
        super(className, null, null, null, tableInfos, moduleName);
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 生成Service接口填充数据
        System.out.println("Generating " + className + "ServiceImpl.java");
        Map<String, String> interfaceData = new HashMap<>(11);
        interfaceData.put("BasePackageName", ConfigUtil.getConfiguration().getPackageName());
        interfaceData.put("InterfacePackageName", ConfigUtil.getConfiguration().getPath().getInterf());
        interfaceData.put("ServicePackageName", ConfigUtil.getConfiguration().getPath().getService());
        interfaceData.put("EntityPackageName", ConfigUtil.getConfiguration().getPath().getEntity());
        interfaceData.put("DaoPackageName", ConfigUtil.getConfiguration().getPath().getDao());
        interfaceData.put("primaryKey", StringUtil.firstToUpperCase(getPrimaryKeyColumnInfo(tableInfos).getColumnName()));
        interfaceData.put("Author", ConfigUtil.getConfiguration().getAuthor());
        interfaceData.put("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        interfaceData.put("ClassName", className);
        interfaceData.put("moduleName", moduleName);
        interfaceData.put("EntityName", StringUtil.firstToLowerCase(className));
        String filePath = StringUtil.package2Path(ConfigUtil.getConfiguration().getFileUrl()) + "//java//" + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName()) + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getInterf());
        String fileName = className + "ServiceImpl.java";
        // 生成Service接口文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_INTERFACE, interfaceData, filePath + fileName);
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
