package com.boyoi.core.util;

/**
 * sqlserver数据类型转换成PostgreSql数据类型
 */
public class SqlServerToPostgreSql {

    public String sqlServerToPostgreSql(String sqlType) {
        if (sqlType == null || sqlType.trim().length() == 0) return sqlType;
        sqlType = sqlType.toLowerCase();
        switch (sqlType) {
            case "binary":
                return "bytea";
            case "bit":
                return "bit(1)";
            case "datetime":
                return "timestamp";
            case "decimal":
                return "numeric";
            case "float":
                return "numeric";
            case "image":
                return "bytea";
            case "money":
                return "numeric";
            case "nchar":
                return "varchar";
            case "ntext":
                return "text";
            case "nvarchar":
                return "varchar";
            case "smalldatetime":
                return "timestamp";
            case "smallmoney":
                return "numeric";
            case "timestamp":
                return "bigint";
            case "tinyint":
                return "smallint";
            case "varbinary":
                return "bytea";
            default:
                System.out.println("-----------------》转化失败：未发现的类型" + sqlType);
                break;

        }
        return sqlType;
    }

}
