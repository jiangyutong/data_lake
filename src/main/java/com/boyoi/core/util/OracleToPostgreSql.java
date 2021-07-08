package com.boyoi.core.util;

/**
 * Oracle数据类型转换成PostgreSql数据类型
 */
public class OracleToPostgreSql {

    public String oracleToPostgreSql(String sqlType) {
        if (sqlType == null || sqlType.trim().length() == 0) return sqlType;
        sqlType = sqlType.toLowerCase();
        switch (sqlType) {
            case "varchar2":
                return "varchar";
            case "date":
                return "timestamp";
            case "clob":
                return "text";
            case "blob":
                return "bytea";
            case "number":
                return "numeric";
            default:
                System.out.println("-----------------》转化失败：未发现的类型" + sqlType);
                break;

        }
        return sqlType;
    }
}
