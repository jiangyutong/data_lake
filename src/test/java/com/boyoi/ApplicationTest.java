package com.boyoi;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.boyoi.base.service.DataBaseService;
import com.boyoi.core.entity.ColumnVo;
import com.boyoi.core.entity.TableDefinition;
import com.boyoi.core.enums.ColumnType;
import com.boyoi.core.enums.TableType;
import com.boyoi.core.util.MyGenericTokenParser;
import com.boyoi.core.util.JdbcUtil;
import com.boyoi.core.util.MyParameterMappingTokenHandler;
import com.boyoi.work.entity.LinkTable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.*;

@SpringBootTest
public class ApplicationTest {
    @Resource
    private DataBaseService dataBaseService;

    @Test
    void contextLoads() {
        //判断表是否存在
        //isExistTable();
        //创建表
        //createTable();
        //删除主键
        //delPk();
        //复制表
        //copyTable();
        //获取模式下的所有表
        //getTables();
        //获取一个表
        //getTable();
        //批量插入数据
        //batchInsert();
        boundSql();
    }

    private void boundSql() {
        MyParameterMappingTokenHandler parameterMappingTokenHandler = new MyParameterMappingTokenHandler();
        MyGenericTokenParser genericTokenParser = new MyGenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String sql = genericTokenParser.parse("select * from  \"db\".\"test_db\" where name=#{ name } and opt_time>#{ optTime }");
        List<String> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        System.out.println(sql);
        System.out.println(parameterMappings);
    }

    private void batchInsert() {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("guid", IdUtil.simpleUUID());
            map.put("k", "k" + i);
            map.put("v", "v" + i);
            map.put("name", "name" + i);
            map.put("type", i % 10);
            map.put("opt_time", "2020-10-20");
            map.put("opt_person", "opt_person" + i);
            map.put("create_time", new Date());
            map.put("json", JSON.toJSONString(map));
            maps.add(map);
        }
        //人为制造一个异常
        maps.get(51).put("guid", null);
        dataBaseService.batchInsert(TableType.DB, "test_db", maps);
    }

    private void getTable() {
        TableDefinition table = dataBaseService.getTable(TableType.DB, "test_db1");
        System.out.println(table);
    }

    private void getTables() {
        List<TableDefinition> tables = dataBaseService.getTables(TableType.DB);
        System.out.println(tables.toString());
    }

    private void copyTable() {
        DataSource dataSource = JdbcUtil.getDataSource("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/atom", "postgres", "121212");
        LinkTable table = new LinkTable();
        table.setTableName("tsys_dictionary");
        table.setCopyTableName("test_db");
        dataBaseService.copyTable(dataSource, table);
    }

    void createTable() {
        List<ColumnVo> columnVos = new ArrayList<>();

        ColumnVo columnVo1 = new ColumnVo();
        columnVo1.setName("varchar");
        columnVo1.setColumnType(ColumnType.VARCHAR);
        columnVo1.setLength(28);
        columnVo1.setNullable(false);
        columnVo1.setIsPk(true);
        columnVo1.setComment("varchar注释");
        columnVos.add(columnVo1);

        ColumnVo columnVo2 = new ColumnVo();
        columnVo2.setName("text");
        columnVo2.setColumnType(ColumnType.TEXT);
        columnVo2.setLength(null);
        columnVo2.setNullable(true);
        columnVo2.setIsPk(false);
        columnVo2.setComment("text注释");
        columnVos.add(columnVo2);

        ColumnVo columnVo3 = new ColumnVo();
        columnVo3.setName("int4");
        columnVo3.setColumnType(ColumnType.INT4);
        columnVo3.setLength(null);
        columnVo3.setNullable(false);
        columnVo3.setIsPk(true);
        columnVo3.setComment("int4注释");
        columnVos.add(columnVo3);

        ColumnVo columnVo4 = new ColumnVo();
        columnVo4.setName("date");
        columnVo4.setColumnType(ColumnType.TIMESTAMP);
        columnVo4.setLength(null);
        columnVo4.setNullable(false);
        columnVo4.setIsPk(true);
        columnVo4.setComment("TIMESTAMP注释");
        columnVos.add(columnVo4);

        TableDefinition tableDefinition = new TableDefinition();
        tableDefinition.setName("test1");
        tableDefinition.setColumnVos(columnVos);
        tableDefinition.setComment("测试创建表");
        tableDefinition.setTableType(TableType.JSON);
        dataBaseService.createTable(tableDefinition);
    }

    void delPk() {
        TableDefinition tableDefinition = new TableDefinition();
        tableDefinition.setName("test");
        tableDefinition.setTableType(TableType.JSON);
        dataBaseService.delPk(tableDefinition);
    }

    void isExistTable() {
        System.out.println(dataBaseService.isExistTable("test", TableType.DB));
    }
}
