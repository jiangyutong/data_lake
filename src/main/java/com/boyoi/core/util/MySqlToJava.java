package com.boyoi.core.util;

public class MySqlToJava {

    public String mySqlToJava(String sqlType) {
        if (sqlType == null || sqlType.trim().length() == 0) return sqlType;
        sqlType = sqlType.toLowerCase();
        switch (sqlType) {
            case "nvarchar":
                return "String";
            case "char":
                return "String";
            case "varchar":
                return "String";
            case "text":
                return "String";
            case "nchar":
                return "String";
            case "blob":
                return "byte[]";
            case "integer":
                return "Long";
            case "tinyint":
                return "Integer";
            case "smallint":
                return "Integer";
            case "mediumint":
                return "Integer";
            case "bit":
                return "Boolean";
            case "bigint":
                return "BigInteger";
            case "float":
                return "Fload";
            case "double":
                return "Double";
            case "decimal":
                return "BigDecimal";
            case "boolean":
                return "Boolean";
            case "date":
                return "Date";
            case "datetime":
                return "Date";
            case "year":
                return "Date";
            case "time":
                return "Time";
            case "timestamp":
                return "Timestamp";
            case "numeric":
                return "BigDecimal";
            case "real":
                return "BigDecimal";
            case "money":
                return "Double";
            case "smallmoney":
                return "Double";
            case "image":
                return "byte[]";
            default:
                System.out.println("-----------------》转化失败：未发现的类型" + sqlType);
                break;
        }
        return sqlType;
    }
}
