package com.boyoi.core.entity;

import com.boyoi.core.enums.ColumnType;
import lombok.Data;

/**
 * @author fuwp
 */
@Data
public class ColumnVo {
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段类型
     */
    private ColumnType columnType;
    /**
     * 长度
     */
    private Integer length;
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
