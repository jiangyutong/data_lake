package com.boyoi.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.JdbcType;
import cn.hutool.db.meta.Table;
import com.alibaba.fastjson.JSON;
import com.boyoi.core.entity.ColumnDefinition;
import com.boyoi.core.entity.ColumnVo;
import com.boyoi.core.entity.IBoundSql;
import com.boyoi.core.entity.TableDefinition;
import com.boyoi.core.enums.ColumnType;
import com.boyoi.core.enums.TableType;

import java.util.*;

/**
 * 转发器
 *
 * @author fuwp
 */
public class ConverterUtil {
    /**
     * sql语句转为IBoundSql
     *
     * @param sqlText sql
     * @return IBoundSql
     */
    public static IBoundSql sqlText2boundSql(String sqlText) {
        MyParameterMappingTokenHandler parameterMappingTokenHandler = new MyParameterMappingTokenHandler();
        MyGenericTokenParser genericTokenParser = new MyGenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String sql = genericTokenParser.parse(sqlText);
        List<String> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new IBoundSql(sql, parameterMappings);
    }


    /**
     * Table转为TableDefinition
     *
     * @param table   table
     * @param newName 新名称
     * @return TableDefinition
     */
    public static TableDefinition table2tableDefinition(Table table, String newName, TableType tableType) {
        TableDefinition tableDefinition = new TableDefinition();
        tableDefinition.setName(StrUtil.isNotBlank(newName) ? newName : table.getTableName());
        tableDefinition.setComment(table.getComment());
        Set<String> pkNames = table.getPkNames();
        tableDefinition.setTableType(tableType);
        List<ColumnVo> columnVos = new ArrayList<>();
        table.getColumns().forEach(column -> {
            ColumnVo columnVo = column2columnVo(pkNames, column);
            columnVos.add(columnVo);
        });
        tableDefinition.setColumnVos(columnVos);
        return tableDefinition;
    }

    /**
     * columnVo转为ColumnDefinition
     *
     * @param columnVo columnVo
     * @return ColumnDefinition
     */
    public static ColumnDefinition columnVo2columnDefinition(ColumnVo columnVo) {
        ColumnDefinition columnDefinition = new ColumnDefinition();
        columnDefinition.setName(StrUtil.trim(columnVo.getName()));
        columnDefinition.setDdlType(columnType2ddl(columnVo));
        columnDefinition.setNullable(columnVo.getNullable());
        columnDefinition.setIsPk(columnVo.getIsPk());
        columnDefinition.setComment(columnVo.getComment());
        return columnDefinition;
    }

    /**
     * column转columnVo
     *
     * @param pkNames 主键名集合
     * @param column  column
     * @return ColumnVo
     */
    public static ColumnVo column2columnVo(Collection<String> pkNames, Column column) {
        ColumnVo columnVo = new ColumnVo();
        columnVo.setName(column.getName());
        columnVo.setComment(column.getComment());
        columnVo.setLength(column.getSize());
        columnVo.setIsPk(pkNames.contains(column.getName()));
        columnVo.setNullable(column.isNullable());
        //长度大于1G只能为TEXT
        columnVo.setColumnType(column.getTypeEnum() == null || column.getSize() > 10485760 ? ColumnType.TEXT : parseTypeFormSqlType(column.getTypeEnum()));
        return columnVo;
    }

    /**
     * columnDefinition转columnVo
     *
     * @param pkNames          主键名集合
     * @param columnDefinition columnDefinition
     * @return ColumnVo
     */
    public static ColumnVo columnDefinition2columnVo(Collection<String> pkNames, ColumnDefinition columnDefinition) {
        ColumnVo columnVo = new ColumnVo();
        columnVo.setName(columnDefinition.getName());
        columnVo.setComment(columnDefinition.getComment());
        columnVo.setIsPk(pkNames.contains(columnDefinition.getName()));
        columnVo.setNullable(columnDefinition.getNullable());
        ddl2columnType(columnDefinition.getDdlType(), columnVo);
        return columnVo;
    }

    /**
     * 转换为建表时的类型字符串
     *
     * @param columnVo 列
     * @return String
     */
    public static String columnType2ddl(ColumnVo columnVo) {
        ColumnType columnType = columnVo.getColumnType();
        Integer length = columnVo.getLength();
        if (columnType == ColumnType.VARCHAR || columnType == ColumnType.CHAR) {
            if (length == null || length <= 0) {
                length = 255;
            }
            return StrUtil.format(columnType.getDdl(), length);
        } else {
            return columnType.getDdl();
        }
    }

    /**
     * 数据库类型转ColumnType
     *
     * @param ddl      String
     * @param columnVo columnVo
     */
    public static void ddl2columnType(String ddl, ColumnVo columnVo) {
        String lengthStr = StrUtil.subBetween(ddl, "(", ")");
        String type = StrUtil.subBefore(ddl, "(", false);
        if (StrUtil.isNotBlank(lengthStr)) {
            columnVo.setLength(Integer.valueOf(lengthStr));
        }
        LinkedHashMap<String, ColumnType> enumMap = EnumUtil.getEnumMap(ColumnType.class);
        for (Map.Entry<String, ColumnType> entries : enumMap.entrySet()) {
            ColumnType value = entries.getValue();
            if (value.getCode().equals(type)) {
                columnVo.setColumnType(value);
                break;
            }
        }

    }


    /**
     * 类型对应关系
     *
     * @param sqlType sqlType
     * @return ColumnType
     */
    public static ColumnType parseTypeFormSqlType(JdbcType sqlType) {
        switch (sqlType) {
            case VARCHAR:
                return ColumnType.VARCHAR;
            case CHAR:
                return ColumnType.CHAR;
            case SMALLINT:
                return ColumnType.INT2;
            case INTEGER:
                return ColumnType.INT4;
            case BIGINT:
                return ColumnType.INT8;
            case FLOAT:
                return ColumnType.FLOAT4;
            case DOUBLE:
                return ColumnType.FLOAT8;
            case BOOLEAN:
                return ColumnType.BOOL;
            case TIMESTAMP:
                return ColumnType.TIMESTAMP;
            default:
                return ColumnType.TEXT;
        }
    }

    /**
     * 数据类型转换
     *
     * @param columnType columnType
     * @param value      值
     * @return o
     */
    public static Object dataTypeConverter(ColumnType columnType, Object value) {
        if (value == null) {
            return null;
        }
        String vStr = String.valueOf(value);
        switch (columnType) {
            case INT4:
                return Integer.valueOf(vStr);
            case INT8:
                return Long.valueOf(vStr);
            case FLOAT4:
                return Float.valueOf(vStr);
            case FLOAT8:
                return Double.valueOf(vStr);
            case BOOL:
                return BooleanUtil.toBoolean(vStr);
            case TIMESTAMP:
                return DateUtil.parse(vStr);
            case JSON:
                return JSON.toJSONString(value);
            default:
                return vStr;
        }
    }
}
