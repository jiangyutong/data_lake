package com.boyoi.core.generator.db;

import com.boyoi.core.generator.entity.ColumnInfo;
import com.boyoi.core.generator.utils.ConfigUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库连接类
 *
 * @author ZhouJL
 * @date 2019/2/14 10:06
 */
public class ConnectionUtil {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    /**
     * 初始化数据库连接
     *
     * @return 连接是否成功
     */
    public boolean initConnection() {
        try {
            Class.forName(DriverFactory.getDriver(ConfigUtil.getConfiguration().getDb().getUrl()));
            String url = ConfigUtil.getConfiguration().getDb().getUrl();
            String username = ConfigUtil.getConfiguration().getDb().getUsername();
            String password = ConfigUtil.getConfiguration().getDb().getPassword();
            connection = DriverManager.getConnection(url, username, password);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取表结构数据
     *
     * @param tableName 表名
     * @return 包含表结构数据的列表
     */
    public List<ColumnInfo> getMetaData(String tableName) throws SQLException {
        String url = ConfigUtil.getConfiguration().getDb().getUrl();
        String database = url.split("/")[3];
        ResultSet tempResultSet = connection.getMetaData().getPrimaryKeys(database, null, tableName);
        String primaryKey = null;
        if (tempResultSet.next()) {
            primaryKey = tempResultSet.getObject(4).toString();
        }
        List<ColumnInfo> columnInfos = new ArrayList<>();
        statement = connection.createStatement();
        String sql = "SELECT * FROM " + tableName;
        resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            ColumnInfo info;
            int isNull = metaData.isNullable(i);
            if (metaData.getColumnName(i).equals(primaryKey)) {
                info = new ColumnInfo(metaData.getColumnName(i), metaData.getColumnType(i), true, isNull, metaData.getPrecision(i), metaData.getScale(i));
            } else {
                info = new ColumnInfo(metaData.getColumnName(i), metaData.getColumnType(i), false, isNull, metaData.getPrecision(i), metaData.getScale(i));
            }
            columnInfos.add(info);
        }
        resultSet = statement.executeQuery("select\n" +
                "des.description as Comment\n" +
                "from\n" +
                "information_schema.columns col left join pg_description des on\n" +
                "col.table_name::regclass = des.objoid\n" +
                "and col.ordinal_position = des.objsubid\n" +
                "where\n" +
                "table_schema = 'public'\n" +
                "and table_name = '" + tableName + "'");
        List<String> comments = new ArrayList<>();
        while (resultSet.next()) {
            comments.add(resultSet.getString("Comment"));
        }
        for (int i = 0; i < columnInfos.size(); i++) {
            columnInfos.get(i).setPropertyName(comments.get(i));
        }
        statement.close();
        resultSet.close();
        return columnInfos;
    }

    public void close() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
