package com.boyoi.core.enums;

import lombok.Getter;

/**
 * 表类型
 *
 * @author fuwp
 */
@Getter
public enum ColumnType {
    /**
     * 对应数据库的类型
     */
    VARCHAR("varchar", "变长字符串", "varchar({})"),
    CHAR("bpchar", "定长字符串", "char({})"),
    TEXT("text", "文档", "text"),
    INT2("int2", "微整型", "int4"),
    INT4("int4", "短整型", "int4"),
    INT8("int8", "长整型", "int8"),
    FLOAT4("float4", "单精度浮点", "float4"),
    FLOAT8("float8", "双精度浮点", "float8"),
    BOOL("bool", "布尔", "bool"),
    JSON("json", "json", "json"),
    TIMESTAMP("timestamp", "日期时间", "timestamp(6)");

    ColumnType(String code, String desc, String ddl) {
        this.code = code;
        this.desc = desc;
        this.ddl = ddl;
    }

    /**
     * 代码
     */
    private final String code;
    /**
     * 说明
     */
    private final String desc;
    /**
     * 分组
     */
    private final String ddl;
}
