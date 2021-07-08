package com.boyoi.base.service;

import com.boyoi.core.entity.*;
import com.boyoi.core.enums.TableType;
import com.boyoi.work.entity.LinkTable;
import com.github.pagehelper.PageInfo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author fuwp
 */
public interface DataBaseService {

    /**
     * 查询表是否存在
     *
     * @param tableType 数据表类型
     * @param tableName 表名
     * @return boolean
     */
    boolean isExistTable(String tableName, TableType tableType);

    /**
     * 创建表
     *
     * @param tableDefinition table
     */
    void createTable(TableDefinition tableDefinition);

    /**
     * 给已有表增加新的字段
     *
     * @param tableDefinition
     */
    void addField(TableDefinition tableDefinition);

    /**
     * 为表和列添加注释
     *
     * @param tableDefinition table
     */
    void comment(TableDefinition tableDefinition);

    /**
     * 添加主键
     *
     * @param tableDefinition table
     */
    void addPk(TableDefinition tableDefinition);

    /**
     * 删除主键
     *
     * @param tableDefinition table
     */
    void delPk(TableDefinition tableDefinition);

    /**
     * 复制表
     *
     * @param dataSource 数据源
     * @param linkTable  已复制到本地表信息
     * @return table
     */
    TableDefinition copyTable(DataSource dataSource, LinkTable linkTable);

    /**
     * 获取模式下的所有表
     *
     * @param tableType tableType
     * @return list
     */
    List<TableDefinition> getTables(TableType tableType);

    /**
     * 获取一个表
     *
     * @param tableType 表类型
     * @param name      表名
     * @return TableDefinition
     */
    TableDefinition getTable(TableType tableType, String name);

    /**
     * 获取列定义对象
     *
     * @param tableDefinition tableDefinition
     * @return list
     */
    List<ColumnVo> getColumns(TableDefinition tableDefinition);

    /**
     * 获取主键名称
     *
     * @param tableDefinition tableDefinition
     * @return list
     */
    List<String> getPks(TableDefinition tableDefinition);

    /**
     * 批量插入数据
     *
     * @param tableType 表类型
     * @param name      表名
     * @param list      数据
     */
    void batchInsert(TableType tableType, String name, List<Map<String, Object>> list);

    /**
     * 插入一条数据
     *
     * @param tableType
     * @param name
     * @param map
     */
    void insert(TableType tableType, String name, Map<String, Object> map);

    /**
     * 根据key字段去更新这一张表
     *
     * @param tableType
     * @param name
     * @param map
     * @param key
     */
    void updateByKey(TableType tableType, String name, Map<String, Object> map, String key);

    /**
     * 根据某一个字段的值去删除数据
     *
     * @param tableType
     * @param name
     * @param key
     * @param value
     */
    void delByKey(TableType tableType, String name, String key, String value);


    /**
     * 分页查询表信息
     *
     * @param gridRequest 条件
     * @return list
     */
    PageInfo<TableDefinition> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 查询
     *
     * @param iBoundSql 预编译语句和参数名
     * @param parameter 参数
     * @return list
     */
    List<Map<String, Object>> query(IBoundSql iBoundSql, Map<String, Object> parameter);

    PageInfo<Map<String, Object>> showData(EasyGridRequest gridRequest);

    /**
     * 查询是否有数据量
     */
    List<Map<String, Object>> getNum(TableType tableType, String name);
}
