package com.boyoi.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author fuwp
 */
@Data
@AllArgsConstructor
public class IBoundSql {
    /**
     * 解析过后的sql
     */
    private String sqlText;
    /**
     * 参数集合
     */
    private List<String> parameterList;
}
