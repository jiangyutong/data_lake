package com.boyoi.work.service.impl;


import com.alibaba.fastjson.JSON;
import com.boyoi.base.service.DataBaseService;
import com.boyoi.base.tools.redis.util.RedisUtils;

import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Table;
import com.boyoi.core.entity.TableDefinition;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.core.util.DriverUtil;
import com.boyoi.core.util.JdbcUtil;
import com.boyoi.work.dao.LinkTableDao;
import com.boyoi.work.entity.Link;
import com.boyoi.work.entity.LinkTable;
import com.boyoi.work.service.LinkService;
import com.boyoi.work.dao.LinkDao;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.util.*;

/**
 * 描述：
 * 数据链接 业务实现类
 *
 * @author Harvey Y.L.Q.
 * @date 2018/12/20 15:33
 */
@Service
public class LinkServiceImpl extends BaseServiceImpl<LinkDao> implements LinkService {

    @Resource
    private LinkDao dao;

    @Resource
    private LinkTableDao linkTableDao;

    @Resource
    private DataBaseService dataBaseService;

    JdbcUtil jdbcUtil = new JdbcUtil();

    DriverUtil driverUtil = new DriverUtil();

    @Override
    public int add(Link link) {
        link.setDrive(driverUtil.driverUtil(link.getType()));
        Connection connection = jdbcUtil.getConn(link.getDrive(), link.getUrl(), link.getUsername(), link.getPassword());
        if (connection == null) {
            return 2;
        }
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        link.setOptPerson(RedisUtils.getLoginUser().getStr("guid"));
        link.setGuid(IdUtil.simpleUUID());
        link.setCreateTime(new Date());
        link.setOptTime(new Date());
        link.setIsDeleted("A");
        return dao.add(link);
    }

    @Override
    public int addBatch(List<Link> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(Link link) {
        return dao.del(link);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    public int update(Link link) {
        return dao.update(link);
    }

    @Override
    public int updateByNotEmpty(Link link) {
        link.setDrive(driverUtil.driverUtil(link.getType()));
        Connection connection = jdbcUtil.getConn(link.getDrive(), link.getUrl(), link.getUsername(), link.getPassword());
        if (connection == null) {
            return 2;
        }
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        link.setOptTime(new Date());
        link.setOptPerson(RedisUtils.getLoginUser().getStr("guid"));
        return dao.updateByNotEmpty(link);
    }

    @Override
    public Link findById(Serializable id) {
        return dao.findById(id);
    }

    @Override
    public List<Link> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<Link> findByGridRequest(EasyGridRequest gridRequest) {
        Map<String, String> mmp = gridRequest.getMap2();
        mmp.put("optPerson", RedisUtils.getLoginUser().getStr("guid"));
        gridRequest.setMap2(mmp);
        gridRequest.setSort("tableAlins.create_time");
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int copyTable(Map<String, Object> map) {
        Link link = dao.findById(map.get("guid").toString());
        DataSource dataSource = JdbcUtil.getDataSource(link.getDrive(), link.getUrl(), link.getUsername(), link.getPassword());
        String tableName = link.getLinkName() + "_" + map.get("tableName").toString();
        LinkTable linkTable = new LinkTable();
        linkTable.setGuid(IdUtil.simpleUUID());
        linkTable.setLinkId(link.getGuid());
        linkTable.setTableName(map.get("tableName").toString());
        linkTable.setCopyTableName(tableName);
        linkTable.setType("D");
        linkTable.setCreateTime(new Date());
        linkTable.setOptTime(new Date());
        linkTable.setOptPerson(RedisUtils.getLoginUser().getStr("guid"));
        TableDefinition tableDefinition = dataBaseService.copyTable(dataSource, linkTable);
        linkTable.setTableComment(tableDefinition.getComment());
        linkTable.setTableColumn(JSON.toJSONString(tableDefinition.getColumnVos()));
        return linkTableDao.add(linkTable);
    }

    @Override
    public List<Link> findBydum(Link link) {
        return dao.findBydum(link);
    }

    @Override
    public Map<String, Object> getNotCopyTable(Map<String, Object> map) {
        Link link = dao.findById(map.get("guid").toString());
        Map<String, Object> retMap = new HashMap<>();
        DataSource dataSource = JdbcUtil.getDataSource(link.getDrive(), link.getUrl(), link.getUsername(), link.getPassword());
        List<Table> tableList = jdbcUtil.queryTableName(dataSource);
        LinkTable linkTable = new LinkTable();
        linkTable.setLinkId(link.getGuid());
        List<LinkTable> linkTables = linkTableDao.findByDomain(linkTable);
        List<Table> copyTbleList = new ArrayList<>();

        for (int i = 0; i < tableList.size(); i++) {
            Table table = tableList.get(i);
            for (LinkTable linkTable1 : linkTables) {
                if (table.getTableName().equals(linkTable1.getTableName())) {
                    copyTbleList.add(table);
                    tableList.remove(i);
                    i--;
                }
            }
        }


        retMap.put("notCopyTable", tableList);
        retMap.put("copyTbleList", copyTbleList);

        return retMap;
    }
}
