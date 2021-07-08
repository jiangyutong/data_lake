package com.boyoi.core.entity;

import cn.hutool.core.util.StrUtil;

import com.boyoi.core.enums.ColumnType;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.enums.TableType;
import com.boyoi.core.util.ConverterUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fuwp
 */
@Data
public class TableDefinition {
    /**
     * 表名
     */
    private String name;
    /**
     * 注释
     */
    private String comment;
    /**
     * 表类型
     */
    private TableType tableType;
    /**
     * 列集合
     */
    private List<ColumnVo> columnVos;
    /**
     * ddl列集合
     */
    private List<ColumnDefinition> columnDefinitions;
    /**
     * 主键集合
     */
    private List<ColumnDefinition> pks;
    /**
     * 结构化所属数据库id
     */
    private String link;

    public void setName(String name) {
        this.name = StrUtil.trim(name);
    }

    public void setColumnVos(List<ColumnVo> columnVos) {
        List<ColumnDefinition> columnDefinitions = new ArrayList<>();
        List<ColumnDefinition> pks = new ArrayList<>();
        //遍历列集合
        columnVos.forEach(columnVo -> {
            //json类型不能作为主键
            if (columnVo.getColumnType() == ColumnType.JSON && columnVo.getIsPk()) {
                throw new CommonException(ResultCode.JSON_NOT_PK);
            }
            ColumnDefinition columnDefinition = ConverterUtil.columnVo2columnDefinition(columnVo);
            //创建表用到的列信息
            columnDefinitions.add(columnDefinition);
            //维护主键集合
            if (columnVo.getIsPk()) {
                pks.add(columnDefinition);
            }
        });
        this.columnVos = columnVos;
        this.columnDefinitions = columnDefinitions;
        this.pks = pks;
    }
}
