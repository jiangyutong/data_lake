package com.boyoi.core.generator.db;

/**
 * 数据库类型判断
 *
 * @author ZhouJL
 * @date 2019/2/14 10:07
 */
public class DriverFactory {
    private final static String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    private final static String DRIVER_POSTGRESQL = "org.postgresql.Driver";
    private final static String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
    private final static String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static String getDriver(String url) {
        if (url.contains("mysql")) {
            return DRIVER_MYSQL;
        }
        if (url.contains("postgresql")) {
            return DRIVER_POSTGRESQL;
        }
        if (url.contains("oracle")) {
            return DRIVER_ORACLE;
        }
        if (url.contains("sqlserver")) {
            return DRIVER_SQLSERVER;
        }
        return null;
    }

}
