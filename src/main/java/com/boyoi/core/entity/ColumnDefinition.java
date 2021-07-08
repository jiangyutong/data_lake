package com.boyoi.core.entity;

import lombok.Data;

/**
 * @author fuwp
 */
@Data
public class ColumnDefinition {
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段类型
     */
    private String ddlType;
    /**
     * 可为空
     */
    private Boolean nullable;
    /**
     * 是否主键
     */
    private Boolean isPk;
    /**
     * 注释
     */
    private String comment;
}
