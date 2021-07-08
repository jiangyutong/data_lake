package com.boyoi.work.service.impl;

import com.alibaba.fastjson.JSON;
import com.boyoi.base.service.DataBaseService;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.ColumnVo;
import com.boyoi.core.entity.CommonException;
import com.boyoi.core.entity.TableDefinition;
import com.boyoi.core.enums.ColumnType;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.enums.TableType;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.core.util.JdbcUtil;
import com.boyoi.work.entity.LinkTable;
import com.boyoi.work.service.LinkTableService;
import com.boyoi.work.dao.LinkTableDao;
import com.boyoi.core.entity.EasyGridRequest;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 描述：
 * 已复制到本地表信息 业务实现类
 *
 * @author Harvey Y.L.Q.
 * @date 2018/12/20 15:33
 */
@Service
public class LinkTableServiceImpl extends BaseServiceImpl<LinkTableDao> implements LinkTableService {

    @Resource
    private LinkTableDao dao;

    @Resource
    private DataBaseService dataBaseService;

    JdbcUtil jdbcUtil = new JdbcUtil();

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int add(Map<String, Object> map) {
        System.out.println(map.toString());
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("childrens");

        //这里是在数据库中创建这一张表
        List<ColumnVo> columnVos = new ArrayList<>();
        for (Map<String, Object> mmp : list) {
            System.out.println(mmp.toString());

            ColumnVo columnVo1 = new ColumnVo();
            columnVo1.setName(mmp.get("fieldName").toString());
            columnVo1.setColumnType(ColumnType.valueOf(mmp.get("fieldType").toString()));
            if (mmp.get("fieldLength") == "" || mmp.get("fieldLength") == null) {
                columnVo1.setLength(0);
            } else {
                columnVo1.setLength(Integer.valueOf(mmp.get("fieldLength").toString()));
            }

            columnVo1.setNullable(Boolean.valueOf(mmp.get("fieldIsnull").toString()));
            columnVo1.setIsPk(Boolean.valueOf(mmp.get("fieldPrimaryKey").toString()));
            if (mmp.get("fieldNotes") == "" || mmp.get("fieldNotes") == null) {
                columnVo1.setComment("");
            } else {
                columnVo1.setComment(mmp.get("fieldNotes").toString());
            }
            columnVos.add(columnVo1);

        }
        TableDefinition tableDefinition = new TableDefinition();
        tableDefinition.setName(map.get("tableName").toString());
        tableDefinition.setColumnVos(columnVos);
        tableDefinition.setComment(map.get("notes").toString());
        tableDefinition.setTableType(TableType.JSON);
        dataBaseService.createTable(tableDefinition);

        //这里是把表的信息添加到数据库linkTable表中
        LinkTable linkTable = new LinkTable();
        linkTable.setGuid(IdUtil.simpleUUID());
        linkTable.setTableName(map.get("tableName").toString());
        linkTable.setCopyTableName(map.get("tableName").toString());
        linkTable.setTableComment(map.get("notes").toString());
        linkTable.setTableColumn(JSON.toJSONString(map.get("childrens")));
        linkTable.setLinkId("无");
        linkTable.setType("J");
        linkTable.setCreateTime(new Date());
        linkTable.setOptTime(new Date());
        linkTable.setOptPerson(RedisUtils.getLoginUser().getStr("guid"));
        return dao.add(linkTable);
    }

    @Override
    public int addBatch(List<LinkTable> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(LinkTable linkTable) {
        try {
            LinkTable linkTable1 = dao.findById(linkTable.getGuid());
            String type = "";
            if (linkTable1.getType().equals("J")) {
                type = "json";
            } else if (linkTable1.getType().equals("D")) {
                type = "db";
            }
            jdbcUtil.updateTableName(linkTable1.getCopyTableName(), type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dao.del(linkTable);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(Map<String, Object> map) {

        LinkTable linkTable = dao.findById(map.get("guid").toString());

        String type = "";
        if (linkTable.getType().equals("J")) {
            type = "json";
        } else if (linkTable.getType().equals("D")) {
            type = "db";
        }
        int k = jdbcUtil.QueryNum(linkTable.getCopyTableName(), type);
        if (k > 0) {
            throw new CommonException(ResultCode.TABLE_NUM_ERROR);
        }
        try {
            jdbcUtil.updateTableName(linkTable.getCopyTableName(), type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("childrens");
        List<ColumnVo> columnVos = new ArrayList<>();
        for (Map<String, Object> mmp : list) {
            System.out.println(mmp.toString());

            ColumnVo columnVo1 = new ColumnVo();
            columnVo1.setName(mmp.get("fieldName").toString());
            columnVo1.setColumnType(ColumnType.valueOf(mmp.get("fieldType").toString()));
            if (mmp.get("fieldLength") == "" || mmp.get("fieldLength") == null) {
                columnVo1.setLength(0);
            } else {
                columnVo1.setLength(Integer.valueOf(mmp.get("fieldLength").toString()));
            }

            columnVo1.setNullable(Boolean.valueOf(mmp.get("fieldIsnull").toString()));
            columnVo1.setIsPk(Boolean.valueOf(mmp.get("fieldPrimaryKey").toString()));
            if (mmp.get("fieldNotes") == "" || mmp.get("fieldNotes") == null) {
                columnVo1.setComment("");
            } else {
                columnVo1.setComment(mmp.get("fieldNotes").toString());
            }
            columnVos.add(columnVo1);
        }
        TableDefinition tableDefinition = new TableDefinition();
        tableDefinition.setName(map.get("tableName").toString());
        tableDefinition.setColumnVos(columnVos);
        tableDefinition.setComment(map.get("notes").toString());
        if (linkTable.getType().equals("J")) {
            tableDefinition.setTableType(TableType.JSON);
        } else if (linkTable.getType().equals("D")) {
            tableDefinition.setTableType(TableType.DB);
        }
        dataBaseService.createTable(tableDefinition);

        LinkTable linkTable1 = new LinkTable();
        if (linkTable.getType().equals("J")) {
            linkTable1.setTableName(map.get("tableName").toString());
        }
        linkTable1.setCopyTableName(map.get("tableName").toString());
        linkTable1.setTableComment(map.get("notes").toString());
        linkTable1.setTableColumn(JSON.toJSONString(map.get("childrens")));
        linkTable1.setOptTime(new Date());
        linkTable1.setOptPerson(RedisUtils.getLoginUser().getStr("guid"));
        linkTable1.setGuid(map.get("guid").toString());
        return dao.updateByNotEmpty(linkTable1);
    }

    @Override
    public int updateByNotEmpty(LinkTable linkTable) {
        return dao.updateByNotEmpty(linkTable);
    }

    @Override
    public LinkTable findById(Serializable id) {
        return dao.findById(id);
    }

    @Override
    public List<LinkTable> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<LinkTable> findByGridRequest(EasyGridRequest gridRequest) {
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
    }

    @Override
    public Map<String, Object> findDetail(Serializable id) {
        Map<String, Object> mmp = new HashMap<>();
        LinkTable linkTable = dao.findById(id);
        TableDefinition tableDefinition = new TableDefinition();
        if (linkTable.getType().equals("D")) {
            tableDefinition = dataBaseService.getTable(TableType.DB, linkTable.getCopyTableName());
        } else if (linkTable.getType().equals("J")) {
            tableDefinition = dataBaseService.getTable(TableType.JSON, linkTable.getCopyTableName());
        }
        mmp.put("tableDefinition", tableDefinition);
        return mmp;
    }
}
