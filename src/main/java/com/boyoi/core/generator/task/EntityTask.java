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
 * 实体类 任务
 *
 * @author ZhouJL
 * @date 2019/2/14 10:12
 */
public class EntityTask extends AbstractTask {

    /**
     * 1.单表生成  2.多表时生成子表实体
     */
    public EntityTask(String className, List<ColumnInfo> infos, String moduleName) {
        this(className, null, null, infos, moduleName);
    }

    /**
     * 一对多关系生成主表实体
     */
    public EntityTask(String className, String parentClassName, String foreignKey, List<ColumnInfo> tableInfos, String moduleName) {
        this(className, parentClassName, foreignKey, null, tableInfos, moduleName);
    }

    /**
     * 多对多关系生成主表实体
     */
    public EntityTask(String className, String parentClassName, String foreignKey, String parentForeignKey, List<ColumnInfo> tableInfos, String moduleName) {
        super(className, parentClassName, foreignKey, parentForeignKey, tableInfos, moduleName);
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 生成Entity填充数据
        System.out.println("Generating " + className + ".java");
        Map<String, String> entityData = new HashMap<>(10);
        entityData.put("BasePackageName", ConfigUtil.getConfiguration().getPackageName());
        entityData.put("EntityPackageName", ConfigUtil.getConfiguration().getPath().getEntity());
        entityData.put("GroupPackageName", ConfigUtil.getConfiguration().getPath().getGroup());
        entityData.put("Author", ConfigUtil.getConfiguration().getAuthor());
        entityData.put("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        entityData.put("ClassName", className);
        entityData.put("moduleName", moduleName);
        // 多对多：主表实体
        if (!StringUtil.isBlank(parentForeignKey)) {
            entityData.put("Methods", GeneratorUtil.generateEntityMethods(parentClassName, tableInfos));
            // 多对一：主表实体
        } else if (!StringUtil.isBlank(foreignKey)) {
            entityData.put("Properties", GeneratorUtil.generateEntityProperties(parentClassName, tableInfos, foreignKey));
            entityData.put("Methods", GeneratorUtil.generateEntityMethods(parentClassName, tableInfos, foreignKey));
            // 单表关系
        } else {
            entityData.put("Properties", GeneratorUtil.generateEntityProperties(tableInfos, className));
        }
        String filePath = StringUtil.package2Path(ConfigUtil.getConfiguration().getFileUrl()) + "//java//" + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName()) + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getEntity());
        String fileName = className + ".java";
        // 生成Entity文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_ENTITY, entityData, filePath + fileName);
    }
}
