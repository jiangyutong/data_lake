package com.boyoi.core.entity;

/**
 * 表字段信息：
 * 区域 实体类
 *
 * @author xiaopeng
 * @date 2020-10-14
 */
public class TableAttribute {

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段类型
     */
    private String typeName;

    /**
     * 字段长度
     */
    private int columnSize;

    /**
     * 是否为空 0不为空
     */
    private int nullable;

    /**
     * 是否为主键
     */
    private String primaryKeys;

    /**
     * 字段注释
     */
    private String remarks;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public int getNullable() {
        return nullable;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    public String getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(String primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
