package com.boyoi.core.generator.entity;


import com.boyoi.core.generator.utils.StringUtil;

import java.io.Serializable;

/**
 * 表对象
 *
 * @author ZhouJL
 * @date 2019/2/14 10:07
 */
public class ColumnInfo implements Serializable {
    /**
     * 实体类属性名
     */
    private String columnName;

    /**
     * 列名
     */
    private String classPropertyName;

    /**
     * 类型代码
     */
    private int type;

    /**
     * 中文名称
     */
    private String propertyName;

    /**
     * 是否为主键
     */
    private boolean isPrimaryKey;

    /**
     * 是否必填
     */
    private boolean isNull;

    /**
     * 最大长度
     */
    private int size;

    /**
     * 小数点后的位数
     */
    private int scale;

    public ColumnInfo() {

    }

    public ColumnInfo(String columnName, int type, boolean isPrimaryKey, int isNull, int size, int scale) {
        this.columnName = StringUtil.columnName2PropertyName(columnName);
        this.classPropertyName = columnName;
        this.type = type;
        this.isPrimaryKey = isPrimaryKey;
        if (isNull == 0) {
            this.isNull = true;
        } else {
            this.isNull = false;
        }
        this.size = size;
        this.scale = scale;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getClassPropertyName() {
        return classPropertyName;
    }

    public void setClassPropertyName(String classPropertyName) {
        this.classPropertyName = classPropertyName;
    }
}
