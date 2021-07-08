package com.boyoi.core.generator.task;

import com.boyoi.core.generator.entity.ColumnInfo;
import com.boyoi.core.generator.task.base.AbstractTask;
import com.boyoi.core.generator.utils.*;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mapper 任务
 *
 * @author ZhouJL
 * @date 2019/2/14 10:13
 */
public class MapperTask extends AbstractTask {

    /**
     * 单表Mapper
     */
    public MapperTask(String className, String tableName, List<ColumnInfo> infos) {
        this(tableName, className, null, null, null, infos, null);
    }

    /**
     * 一对多Mapper
     */
    public MapperTask(String tableName, String className, String parentTableName, String parentClassName, String foreignKey, List<ColumnInfo> tableInfos, List<ColumnInfo> parentTableInfos) {
        this(tableName, className, parentTableName, parentClassName, foreignKey, null, null, tableInfos, parentTableInfos);
    }

    /**
     * 多对多Mapper
     */
    public MapperTask(String tableName, String className, String parentTableName, String parentClassName, String foreignKey, String parentForeignKey, String relationalTableName, List<ColumnInfo> tableInfos, List<ColumnInfo> parentTableInfos) {
        super(tableName, className, parentTableName, parentClassName, foreignKey, parentForeignKey, relationalTableName, tableInfos, parentTableInfos);
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 生成Mapper填充数据
        System.out.println("Generating " + className + "-map.xml");
        Map<String, String> mapperData = new HashMap<>(30);
        mapperData.put("PackageName", ConfigUtil.getConfiguration().getPackageName() + "." + ConfigUtil.getConfiguration().getPath().getDao());
        mapperData.put("BasePackageName", ConfigUtil.getConfiguration().getPackageName());
        mapperData.put("DaoPackageName", ConfigUtil.getConfiguration().getPath().getDao());
        mapperData.put("EntityPackageName", ConfigUtil.getConfiguration().getPath().getEntity());
        mapperData.put("ClassName", className);
        mapperData.put("EntityName", StringUtil.firstToLowerCase(className));
        mapperData.put("TableName", tableName);
        mapperData.put("InsertProperties", GeneratorUtil.generateMapperInsertProperties(tableInfos));
        mapperData.put("PrimaryKey", getPrimaryKeyColumnInfo(tableInfos).getColumnName());
        mapperData.put("WhereId", "#{" + getPrimaryKeyColumnInfo(tableInfos).getColumnName() + "}");
        mapperData.put("Id", "#{id}");
        // 多对多
        if (!StringUtil.isBlank(parentForeignKey)) {
            mapperData.put("ColumnMap", GeneratorUtil.generateMapperColumnMap(tableName, parentTableName, tableInfos, parentTableInfos, StringUtil.firstToLowerCase(parentClassName)));
            mapperData.put("ResultMap", GeneratorUtil.generateMapperResultMap(tableInfos));
            mapperData.put("Association", "");
            mapperData.put("Collection", GeneratorUtil.generateMapperCollection(parentTableInfos, parentClassName, ConfigUtil.getConfiguration().getPackageName() + ConfigUtil.getConfiguration().getPath().getEntity()));
            mapperData.put("InsertBatchValues", GeneratorUtil.generateMapperInsertBatchValues(tableInfos, StringUtil.firstToLowerCase(className)));
            mapperData.put("InsertValues", GeneratorUtil.generateMapperInsertValues(tableInfos));
            mapperData.put("UpdateProperties", GeneratorUtil.generateMapperUpdateProperties(tableInfos));
            mapperData.put("Joins", GeneratorUtil.generateMapperJoins(tableName, parentTableName, relationalTableName, foreignKey, parentForeignKey, getPrimaryKeyColumnInfo(tableInfos).getColumnName(), getPrimaryKeyColumnInfo(parentTableInfos).getColumnName()));
            // 一对多
        } else if (!StringUtil.isBlank(foreignKey)) {
            mapperData.put("ColumnMap", GeneratorUtil.generateMapperColumnMap(tableName, parentTableName, tableInfos, parentTableInfos, StringUtil.firstToLowerCase(parentClassName), foreignKey));
            mapperData.put("ResultMap", GeneratorUtil.generateMapperResultMap(tableInfos));
            mapperData.put("Association", GeneratorUtil.generateMapperAssociation(parentTableInfos, parentClassName, ConfigUtil.getConfiguration().getPackageName() + ConfigUtil.getConfiguration().getPath().getEntity()));
            mapperData.put("Collection", "");
            mapperData.put("InsertBatchValues", GeneratorUtil.generateMapperInsertBatchValues(tableInfos, StringUtil.firstToLowerCase(className), StringUtil.firstToLowerCase(parentClassName), foreignKey, getPrimaryKeyColumnInfo(parentTableInfos).getPropertyName()));
            mapperData.put("InsertValues", GeneratorUtil.generateMapperInsertValues(tableInfos, StringUtil.firstToLowerCase(parentClassName), foreignKey, getPrimaryKeyColumnInfo(parentTableInfos).getPropertyName()));
            mapperData.put("UpdateProperties", GeneratorUtil.generateMapperUpdateProperties(tableInfos, StringUtil.firstToLowerCase(parentClassName), foreignKey, getPrimaryKeyColumnInfo(parentTableInfos).getPropertyName()));
            mapperData.put("Joins", GeneratorUtil.generateMapperJoins(tableName, parentTableName, foreignKey, getPrimaryKeyColumnInfo(parentTableInfos).getColumnName()));
        } else { // 单表
            mapperData.put("ColumnMap", GeneratorUtil.generateMapperColumnMap(tableName, tableInfos));
            mapperData.put("ResultMap", GeneratorUtil.generateMapperResultMap(tableInfos));
            mapperData.put("InsertBatchValues", GeneratorUtil.generateMapperInsertBatchValues(tableInfos, StringUtil.firstToLowerCase(className)));
            mapperData.put("InsertValues", GeneratorUtil.generateMapperInsertValues(tableInfos));
            mapperData.put("UpdateProperties", GeneratorUtil.generateMapperUpdateProperties(tableInfos));
            mapperData.put("UpdateIfProperties", GeneratorUtil.generateMapperUpdateIfProperties(tableInfos));
            mapperData.put("WhereProperties", GeneratorUtil.generateMapperWhereProperties(tableInfos));
            mapperData.put("WherePropertiesNotP", GeneratorUtil.generateMapperWherePropertiesNotP(tableInfos));
        }
        String filePath = StringUtil.package2Path(ConfigUtil.getConfiguration().getFileUrl()) + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getMapper());
        String fileName = className + "-map.xml";
        // 生成Mapper文件
        FileUtil.generateToJava(FreemarketConfigUtils.TYPE_MAPPER, mapperData, filePath + fileName);
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
