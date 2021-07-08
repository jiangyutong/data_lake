package com.boyoi.core.entity;

import lombok.Data;

/**
 * 错误信息实体类
 *
 * @author ZhouJL
 * @date 2019/3/14 17:13
 */
@Data
public class ParameterInvalidItem {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 值
     */
    private Object value;

    public ParameterInvalidItem(String fieldName, String message, Object value) {
        this.fieldName = fieldName;
        this.message = message;
        this.value = value;
    }

    public ParameterInvalidItem() {
    }
}
