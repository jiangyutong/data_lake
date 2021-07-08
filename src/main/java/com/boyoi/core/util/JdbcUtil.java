package com.boyoi.core.util;

import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.meta.MetaUtil;
import com.boyoi.core.entity.*;
import com.boyoi.core.entity.CommonException;
import com.boyoi.core.enums.ResultCode;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;


import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @author fuwp
 */
public class JdbcUtil {
    /**
     * 获取数据库连接
     *
     * @return Connection
     */
    public static Connection getConn() {
        DataSource dataSource = getDataSource();
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CommonException(ResultCode.CONNECTION_ERROR);
        }
    }

    /**
     * JDBC链接池
     *
     * @param driver   driver
     * @param url      url
     * @param username username
     * @param password password
     * @return Connection
     */
    public static Connection getConn(String driver, String url, String username, String password) {
        Connection connection;
        try {
            DataSource dataSource = getDataSource(driver, url, username, password);
            connection = dataSource.getConnection();
            return connection;
        } catch (DbRuntimeException e) {
            e.printStackTrace();
            throw new CommonException(ResultCode.CONNECTION_DRIVER_ERROR);
        } catch (CommunicationsException e) {
            e.printStackTrace();
            throw new CommonException(ResultCode.CONNECTION_ERROR);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new CommonException(ResultCode.CONNECTION_ERROR);
        }
    }

    /**
     * 获取默认数据源
     *
     * @return DataSource
     */
    public static DataSource getDataSource() {
        return (DataSource) SpringUtil.getBean("dataSource");
    }

    /**
     * 获取指定数据源
     *
     * @param driver   driver
     * @param url      url
     * @param username username
     * @param password password
     * @return DataSource
     */
    public static DataSource getDataSource(String driver, String url, String username, String password) {
        return new SimpleDataSource(url, username, password, driver);
    }

    /**
     * 查询表名
     *
     * @param dataSource
     * @return
     */
    public List<Table> queryTableName(DataSource dataSource) {
        try {
            List<Table> tables = new ArrayList<>();
            List<String> list = MetaUtil.getTables(dataSource);
            for (int i = 0; i < list.size(); i++) {
                Table table = new Table();
                table.setTableName(list.get(i));
                tables.add(table);
            }
            return tables;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 修改已删除链接关联的表名
     *
     * @param tableName
     * @throws SQLException
     */
    public void updateTableName(String tableName, String type) throws SQLException {
        Connection connection = getConn();
        Statement stmt_create = connection.createStatement();
        try {
            String sqql = "alter table \"" + type + "\".\"" + tableName + "\" drop constraint " + tableName + "_pkey";
            stmt_create.executeUpdate(sqql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "drop table \"" + type + "\".\"" + tableName + "\" ";
        stmt_create.executeUpdate(sql);
        stmt_create.close();
        connection.close();
    }

    /**
     * 查询表是否有数据
     *
     * @return
     */
    public int QueryNum(String tableName, String type) {
        int i = 0;
        try {
            Connection connection = getConn();
            Statement stmt_create = connection.createStatement();
            String sql = "select * from \"" + type + "\".\"" + tableName + "\" limit 10 ";
            ResultSet resultSet = stmt_create.executeQuery(sql);
            while (resultSet.next()) {
                i++;
            }
            stmt_create.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

}
