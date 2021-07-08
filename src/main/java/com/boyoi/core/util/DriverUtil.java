package com.boyoi.core.util;

public class DriverUtil {
    public static String driverUtil(String type) {
        switch (type) {
            case "1": {
                return "com.mysql.cj.jdbc.Driver";
            }
            case "2": {
                return "oracle.jdbc.driver.OracleDriver";
            }
            case "3": {
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            }
            case "4": {
                return "org.postgresql.Driver";
            }
        }
        return "";
    }
}
