package com.boyoi.core.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 表类型
 *
 * @author fuwp
 */
@Getter
public enum TableType {
    /**
     * 对应数据库的表type和分组
     */
    DB("结构化", "db", "db.{}"),
    JSON("半结构化", "json", "json.{}"),
    PUBLIC("系统", "public", "public.{}"),
    TEMPLATE_FIELD_MAPPER("字段映射", "template_field_mapper", "template_field_mapper.{}"),
    TEMPLATE_DATA("数据存储", "template_data", "template_data.{}");

    TableType(String desc, String name, String group) {
        this.desc = desc;
        this.name = name;
        this.group = group;
    }

    /**
     * 说明
     */
    private final String desc;
    /**
     * 名称
     */
    private final String name;
    /**
     * 分组
     */
    private final String group;
}
