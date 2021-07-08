package com.boyoi.base.dao;

import com.boyoi.core.entity.*;
import com.boyoi.core.enums.TableType;
import com.boyoi.work.entity.QueryApi;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fuwp
 */
@Mapper
public interface DataBaseDao {
    /**
     * 查询表是否存在
     *
     * @param tableName 表名
     * @return s
     */
    String isExistTable(@Param("type") String type, @Param("tableName") String tableName);

    /**
     * 创建表
     *
     * @param tableDefinition table
     */
    void createTable(TableDefinition tableDefinition);

    /**
     * 增加新的字段
     *
     * @param tableDefinition 新增加的字段信息
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
     * del主键
     *
     * @param tableDefinition table
     */
    void delPk(TableDefinition tableDefinition);

    /**
     * 获取模式下的所有表
     *
     * @param name 模式名
     * @return list
     */
    List<TableDefinition> getTables(String name);

    /**
     * 获取一个表
     *
     * @param tableType 表类型
     * @param name      表名
     * @return TableDefinition
     */
    TableDefinition getTable(@Param("tableType") TableType tableType, @Param("name") String name);

    /**
     * 获取列定义对象
     *
     * @param tableType 表类型
     * @param name      表名
     * @return list
     */
    List<ColumnDefinition> getColumns(@Param("tableType") TableType tableType, @Param("name") String name);

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
     * @param tableDefinition 表对象
     * @param insertList      list
     */
    void batchInsert(@Param("tableDefinition") TableDefinition tableDefinition, @Param("list") List<List<Object>> insertList);

    /**
     * 单条插入
     *
     * @param tableDefinition 表对象
     * @param row             参数集合
     */
    void insert(@Param("tableDefinition") TableDefinition tableDefinition, @Param("row") List<Object> row);

    /**
     * 根据某一个key的值去更新数据
     *
     * @param tableType
     * @param name
     * @param key
     * @param value
     * @Param map
     */
    void update(@Param("tableType") TableType tableType, @Param("name") String name, @Param("key") String key, @Param("value") Object value, @Param("map") Map<String, Object> map);


    /**
     * 根据某个字段去删除数据
     *
     * @param tableType
     * @param name
     * @param key
     * @param value
     */
    void delByKey(@Param("tableType") TableType tableType, @Param("name") String name, @Param("key") String key, @Param("value") String value);

    /**
     * 分页查询表信息
     *
     * @param gridRequest 条件
     * @return list
     */
    List<TableDefinition> findByGridRequest(EasyGridRequest gridRequest);

    /**
     * 根据值去表中获取某一条数据
     *
     * @param tableType
     * @param tableName
     * @param key1
     * @param value1
     * @return
     */
    Map<String, Object> findByGuid(@Param("tableType") TableType tableType, @Param("tableName") String tableName, @Param("key1") String key1, @Param("value1") String value1);

    List<Map<String, Object>> showData(EasyGridRequest gridRequest);

    /**
     * 查询表是否有数据
     *
     * @param tableType
     * @param name
     * @return
     */
    List<Map<String, Object>> getNum(@Param("tableType") TableType tableType, @Param("name") String name);

    /**
     * 查询某个表中的特定数据，其中gridRequest是该表的查询条件
     *
     * @param tableType
     * @param name
     * @param gridRequest
     * @return
     */
    List<Map<String, Object>> findDataFromTemplate(@Param("tableType") TableType tableType, @Param("name") String name, @Param("gridRequest") EasyGridRequest gridRequest);
}
