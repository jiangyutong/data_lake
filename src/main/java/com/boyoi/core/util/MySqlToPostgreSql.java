package com.boyoi.core.util;

/**
 * MySql数据类型转换成PostgreSql数据类型
 */
public class MySqlToPostgreSql {

    public String mySqlToPostgreSql(String sqlType) {
        if (sqlType == null || sqlType.trim().length() == 0) return sqlType;
        sqlType = sqlType.toLowerCase();
        switch (sqlType) {
            case "bit":
                return "bit(1)";
            case "tinyint":
                return "smallint";
            case "float":
                return "real";
            case "double":
                return "numeric";
            case "decimal":
                return "numeric";
            case "mediumtext":
                return "varchar";
            case "datetime":
                return "timestamp";
            case "longblob":
                return "bytea";
            case "longtext":
                return "text";
            case "varchar(43)":
                return "CIDR";
            case "varchar(17)":
                return "macaddr";
            case "varchar(36)":
                return "uuid";
            case "point":
                return "point";
            case "linestring":
                return "line";
            case "polygon":
                return "box";
            default:
                System.out.println("-----------------》转化失败：未发现的类型" + sqlType);
                break;

        }
        return sqlType;
    }
}
