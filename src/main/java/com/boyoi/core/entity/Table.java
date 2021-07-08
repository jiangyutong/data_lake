package com.boyoi.core.entity;

/**
 * 表信息：
 * 区域 实体类
 *
 * @author xiaopeng
 * @date 2020-10-14
 */
public class Table {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String notes;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
