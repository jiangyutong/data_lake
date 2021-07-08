package com.boyoi.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
import com.boyoi.base.dao.DataBaseDao;
import com.boyoi.base.service.DataBaseService;
import com.boyoi.core.entity.*;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.enums.TableType;
import com.boyoi.core.util.ConverterUtil;
import com.boyoi.work.entity.LinkTable;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author fuwp
 */
@Service
@Slf4j
public class DataBaseServiceImpl implements DataBaseService {
    @Resource
    private DataBaseDao dataBaseDao;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean isExistTable(String tableName, TableType tableType) {
        String type = tableType.getName();
        String existTable = dataBaseDao.isExistTable(type, tableName);
        return !StrUtil.isBlank(existTable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createTable(TableDefinition tableDefinition) {
        //判断表是否存在
        boolean existTable = isExistTable(tableDefinition.getName(), tableDefinition.getTableType());
        if (existTable) {
            throw new CommonException(ResultCode.TABLE_EXIST);
        }
        //创建表
        dataBaseDao.createTable(tableDefinition);
        //添加注释
        dataBaseDao.comment(tableDefinition);
        //添加主键
        if (tableDefinition.getPks().size() > 0) {
            dataBaseDao.addPk(tableDefinition);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addField(TableDefinition tableDefinition) {
        boolean existTable = isExistTable(tableDefinition.getName(), tableDefinition.getTableType());
        if (!existTable) {
            throw new CommonException(ResultCode.TABLE_NOT_EXIST);
        }
        dataBaseDao.addField(tableDefinition);
        dataBaseDao.comment(tableDefinition);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void comment(TableDefinition tableDefinition) {
        dataBaseDao.comment(tableDefinition);
    }

    @Override
    public void addPk(TableDefinition tableDefinition) {
        dataBaseDao.addPk(tableDefinition);
    }

    @Override
    public void delPk(TableDefinition tableDefinition) {
        dataBaseDao.delPk(tableDefinition);
    }

    @Override
    public TableDefinition copyTable(DataSource dataSource, LinkTable linkTable) {
        //获取表的原始数据
        Table table = MetaUtil.getTableMeta(dataSource, linkTable.getTableName());
        //判断表是否存在
        if (CollectionUtil.isEmpty(table.getColumns())) {
            throw new CommonException(ResultCode.COPY_TABLE_NOT_EXIST);
        }
        //进行类型转换
        TableDefinition tableDefinition = ConverterUtil.table2tableDefinition(table, linkTable.getCopyTableName(), TableType.DB);
        //创建表
        createTable(tableDefinition);
        return tableDefinition;
    }

    @Override
    public List<TableDefinition> getTables(TableType tableType) {
        //获取表的基本信息列表
        List<TableDefinition> tables = dataBaseDao.getTables(tableType.getName());
        tables.forEach(tableDefinition -> {
            //获取列信息
            List<ColumnVo> columns = getColumns(tableDefinition);
            //设置列信息
            tableDefinition.setColumnVos(columns);
        });
        return tables;
    }

    @Override
    public TableDefinition getTable(TableType tableType, String name) {
        //获取表的基本信息
        TableDefinition tableDefinition = dataBaseDao.getTable(tableType, name);
        if (tableDefinition != null) {
            //获取列信息
            List<ColumnVo> columns = getColumns(tableDefinition);
            //设置列信息
            tableDefinition.setColumnVos(columns);
        }
        return tableDefinition;
    }

    @Override
    public List<ColumnVo> getColumns(TableDefinition tableDefinition) {
        //要返回的集合
        List<ColumnVo> list = new ArrayList<>();
        //获取列的定义信息
        List<ColumnDefinition> columns = dataBaseDao.getColumns(tableDefinition.getTableType(), tableDefinition.getName());
        //获取主键列名称
        List<String> pks = getPks(tableDefinition);
        columns.forEach(columnDefinition -> {
            //转换为vo对象
            ColumnVo columnVo = ConverterUtil.columnDefinition2columnVo(pks, columnDefinition);
            //添加到集合
            list.add(columnVo);
        });
        return list;
    }

    @Override
    public List<String> getPks(TableDefinition tableDefinition) {
        return dataBaseDao.getPks(tableDefinition);
    }

    /**
     * 向数据库里插入一条数据
     *
     * @param tableType
     * @param name
     * @param map
     */
    @Transactional(noRollbackFor = Exception.class)
    @Override
    public void insert(TableType tableType, String name, Map<String, Object> map) {
        TableDefinition tableDefinition = getTable(tableType, name);
        if (tableDefinition == null) {
            throw new CommonException(ResultCode.TEMPLATE_MAPPER_NOT_EXIST);
        }
        List<Object> list = new ArrayList<>();
        List<ColumnVo> columnVos = tableDefinition.getColumnVos();
        columnVos.forEach(columnVo -> {
            list.add(map.get(columnVo.getName()));
        });
        dataBaseDao.insert(tableDefinition, list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByKey(TableType tableType, String name, Map<String, Object> map, String key) {
//        TableDefinition tableDefinition = getTable(tableType, name);
//        if(tableDefinition == null){
//            throw new CommonException(ResultCode.TEMPLATE_MAPPER_NOT_EXIST);
//        }
//        List<Object> list = new ArrayList<>();
//        List<ColumnVo> columnVos = tableDefinition.getColumnVos();
//        columnVos.forEach(columnVo -> {
//            list.add(map.get(columnVo.getName()));
//        });
        String value = map.get(key).toString();
        dataBaseDao.update(tableType, name, key, value, map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByKey(TableType tableType, String name, String key, String value) {
        dataBaseDao.delByKey(tableType, name, key, value);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(TableType tableType, String name, List<Map<String, Object>> list) {
        //获取表信息
        TableDefinition tableDefinition = getTable(tableType, name);
        if (tableDefinition == null) {
            throw new CommonException(ResultCode.TABLE_NOT_EXIST);
        }
        //插入前预处理
        List<List<Object>> lists = batchInsertBefore(tableDefinition, list);
        //分多个批次处理
        int size = 50;
        List<List<List<Object>>> split = CollUtil.split(lists, size);
        for (int i = 0; i < split.size(); i++) {
            List<List<Object>> oneSplit = split.get(i);
            //try {
            dataBaseDao.batchInsert(tableDefinition, oneSplit);
            /*} catch (Exception e) {
                for (int j = 0; j < oneSplit.size(); j++) {
                    List<Object> row = oneSplit.get(j);
                    try {
                        dataBaseDao.insert(tableDefinition, row);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        //根据下标获取异常数据
                        Map<String, Object> errData = list.get(size * i + j);
                        //添加到要返回的集合
                    }
                }
            }*/
        }
    }

    @Override
    public PageInfo<TableDefinition> findByGridRequest(EasyGridRequest gridRequest) {
        String sort = gridRequest.getSort();
        if (StrUtil.isBlank(sort) || "tablealins.opt_time".equalsIgnoreCase(sort) || "opt_time".equalsIgnoreCase(sort)) {
            gridRequest.setSort("relname");
        }
        PageInfo<TableDefinition> page = PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dataBaseDao.findByGridRequest(gridRequest));
        List<TableDefinition> list = page.getList();
        list.forEach(tableDefinition -> {
            //获取列信息
            List<ColumnVo> columns = getColumns(tableDefinition);
            //设置列信息
            tableDefinition.setColumnVos(columns);
        });
        return page;
    }

    @Override
    public List<Map<String, Object>> query(IBoundSql iBoundSql, Map<String, Object> parameter) {
        String sqlText = iBoundSql.getSqlText();
        List<String> parameterList = iBoundSql.getParameterList();
        Object[] args = new Object[parameterList.size()];
        for (int i = 0; i < parameterList.size(); i++) {
            String key = parameterList.get(i);
            args[i] = parameter.get(key);
        }
        log.info("sql:{}", sqlText);
        log.info("args:{}", Arrays.toString(args));
        return jdbcTemplate.queryForList(sqlText, args);
    }

    @Override
    public PageInfo<Map<String, Object>> showData(EasyGridRequest gridRequest) {
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //数据
                .doSelectPageInfo(() -> dataBaseDao.showData(gridRequest));
    }

    @Override
    public List<Map<String, Object>> getNum(TableType tableType, String name) {
        List<Map<String, Object>> list = dataBaseDao.getNum(tableType, name);
        return list;
    }

    /**
     * 批量插入前置处理
     *
     * @param tableDefinition 表对象
     * @param list            list
     * @return list
     */
    private List<List<Object>> batchInsertBefore(TableDefinition tableDefinition, List<Map<String, Object>> list) {
        //处理后的数据集合
        List<List<Object>> insertList = new ArrayList<>();
        //遍历数据集合
        list.forEach(data -> {
            //一行数据,顺序为columnVos的顺序
            List<Object> rowList = new ArrayList<>();
            //遍历列集合
            tableDefinition.getColumnVos().forEach(columnVo -> {
                //根据列名取出数据value
                Object obj = data.get(columnVo.getName());
                //value转换类型
                obj = ConverterUtil.dataTypeConverter(columnVo.getColumnType(), obj);
                rowList.add(obj);
            });
            insertList.add(rowList);
        });
        return insertList;
    }
}
