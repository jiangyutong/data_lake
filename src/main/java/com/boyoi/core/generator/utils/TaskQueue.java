package com.boyoi.core.generator.utils;

import com.boyoi.core.generator.entity.ColumnInfo;
import com.boyoi.core.generator.task.*;
import com.boyoi.core.generator.task.base.AbstractTask;

import java.util.LinkedList;
import java.util.List;

/**
 * 任务分配工具类
 *
 * @author ZhouJL
 * @date 2019/2/14 10:15
 */
public class TaskQueue {

    private LinkedList<AbstractTask> taskQueue = new LinkedList<>();

    private void initCommonTasks(String className, String moduleName, List<ColumnInfo> tableInfos) {
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getController())) {
            taskQueue.add(new ControllerTask(className, moduleName, tableInfos));
        }
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getService())) {
            taskQueue.add(new ServiceTask(className, moduleName));
        }
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getInterf())) {
            taskQueue.add(new InterfaceTask(className, tableInfos, moduleName));
        }
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getDao())) {
            taskQueue.add(new DaoTask(className, moduleName));
        }
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getGroup())) {
            taskQueue.add(new GroupTask(className, moduleName));
        }
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getMessage())) {
            taskQueue.add(new MessageTask(className, moduleName, tableInfos));
        }
    }

    public void initSingleTasks(String className, String tableName, List<ColumnInfo> tableInfos, String moduleName) {
        initCommonTasks(className, moduleName, tableInfos);
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getEntity())) {
            taskQueue.add(new EntityTask(className, tableInfos, moduleName));
        }
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getMapper())) {
            taskQueue.add(new MapperTask(className, tableName, tableInfos));
        }
    }

    public void initOne2ManyTasks(String tableName, String className, String parentTableName, String parentClassName, String foreignKey, List<ColumnInfo> tableInfos, List<ColumnInfo> parentTableInfos, String moduleName) {
        initCommonTasks(className, moduleName, tableInfos);
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getEntity())) {
            taskQueue.add(new EntityTask(className, parentClassName, foreignKey, tableInfos, moduleName));
            taskQueue.add(new EntityTask(parentClassName, parentTableInfos, moduleName));
        }
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getMapper())) {
            taskQueue.add(new MapperTask(tableName, className, parentTableName, parentClassName, foreignKey, tableInfos, parentTableInfos));
        }
    }

    public void initMany2ManyTasks(String tableName, String className, String parentTableName, String parentClassName, String foreignKey, String parentForeignKey, String relationalTableName, List<ColumnInfo> tableInfos, List<ColumnInfo> parentTableInfos, String moduleName) {
        initCommonTasks(className, moduleName, tableInfos);
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getEntity())) {
            taskQueue.add(new EntityTask(className, parentClassName, foreignKey, parentForeignKey, tableInfos, moduleName));
            taskQueue.add(new EntityTask(parentClassName, parentTableInfos, moduleName));
        }
        if (!StringUtil.isBlank(ConfigUtil.getConfiguration().getPath().getMapper())) {
            taskQueue.add(new MapperTask(tableName, className, parentTableName, parentClassName, foreignKey, parentForeignKey, relationalTableName, tableInfos, parentTableInfos));
        }
    }

    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }

    public AbstractTask poll() {
        return taskQueue.poll();
    }

}
