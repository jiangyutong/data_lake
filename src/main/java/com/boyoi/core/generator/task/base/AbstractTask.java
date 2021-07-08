package com.boyoi.core.generator.task.base;

import com.boyoi.core.generator.entity.ColumnInfo;
import com.boyoi.core.generator.utils.ConfigUtil;
import com.boyoi.core.generator.utils.StringUtil;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 任务基类
 *
 * @author ZhouJL
 * @date 2019/2/14 10:11
 */
public abstract class AbstractTask implements Serializable {
    protected String tableName;
    protected String className;
    protected String moduleName;
    protected String parentTableName;
    protected String parentClassName;
    protected String foreignKey;
    protected String relationalTableName;
    protected String parentForeignKey;
    protected List<ColumnInfo> tableInfos;
    protected List<ColumnInfo> parentTableInfos;

    /**
     * Controller、Service、Dao
     *
     * @param className
     */
    public AbstractTask(String className, String moduleName) {
        this.className = className;
        this.moduleName = moduleName;
    }

    /**
     * Entity
     *
     * @param className
     * @param parentClassName
     * @param foreignKey
     * @param tableInfos
     */
    public AbstractTask(String className, String parentClassName, String foreignKey, String parentForeignKey, List<ColumnInfo> tableInfos, String moduleName) {
        this.className = className;
        this.parentClassName = parentClassName;
        this.foreignKey = foreignKey;
        this.parentForeignKey = parentForeignKey;
        this.tableInfos = tableInfos;
        this.moduleName = moduleName;
    }


    /**
     * Mapper
     *
     * @param tableName
     * @param className
     * @param parentTableName
     * @param parentClassName
     * @param foreignKey
     * @param parentForeignKey
     * @param tableInfos
     * @param parentTableInfos
     */
    public AbstractTask(String tableName, String className, String parentTableName, String parentClassName, String foreignKey, String parentForeignKey, String relationalTableName, List<ColumnInfo> tableInfos, List<ColumnInfo> parentTableInfos) {
        this.tableName = tableName;
        this.className = className;
        this.parentTableName = parentTableName;
        this.parentClassName = parentClassName;
        this.foreignKey = foreignKey;
        this.parentForeignKey = parentForeignKey;
        this.relationalTableName = relationalTableName;
        this.tableInfos = tableInfos;
        this.parentTableInfos = parentTableInfos;
    }

    /**
     * 启动
     *
     * @throws IOException       流异常
     * @throws TemplateException 模板异常
     */
    public abstract void run() throws IOException, TemplateException;

    @Deprecated
    protected void createFilePathIfNotExists(String filePath) {
        // 用户配置了包名，不进行检测
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPackageName())) {
            return;
        }
        File file = new File(filePath);
        // 检测文件路径是否存在，不存在则创建
        if (!file.exists()) {
            file.mkdir();
        }
    }

}
