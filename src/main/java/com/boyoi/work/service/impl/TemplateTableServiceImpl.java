package com.boyoi.work.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.boyoi.base.dao.DataBaseDao;
import com.boyoi.base.service.DataBaseService;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.*;
import com.boyoi.core.enums.ColumnType;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.enums.TableType;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.core.util.JdbcUtil;
import com.boyoi.work.dao.LinkTableDao;
import com.boyoi.work.dao.TemplateTableDao;
import com.boyoi.work.entity.FieldMapperInfo;
import com.boyoi.work.entity.LinkTable;
import com.boyoi.work.entity.TemplateTable;
import com.boyoi.work.service.TemplateTableService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

@Service
public class TemplateTableServiceImpl extends BaseServiceImpl<TemplateTableDao> implements TemplateTableService {
    @Resource
    private TemplateTableDao dao;

    @Resource
    private DataBaseDao dataBaseDao;

    @Resource
    private DataBaseService dataBaseService;

    JdbcUtil jdbcUtil = new JdbcUtil();

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int add(Map<String, Object> map) {
        //创建映射表
        createTable(map, "mapper");
        //创建数据表
        createTable(map, "data");
        //这里是把表的信息添加到数据库linkTable表中
        return addTemplateTableInfo(map);
    }

    /**
     * 这里的服务不会调用，是自动生成的
     *
     * @param list
     * @return
     */
    @Override
    public int addBatch(List<TemplateTable> list) {
        return dao.addBatch(list);
    }

    /**
     * 这里是通过模板的guid，得到该模板的所有字段映射信息
     *
     * @param id
     * @return List<FieldMapperInfo>
     */
    @Override
    public PageInfo<Map<String, Object>> getFieldMapperInfo(Serializable id, EasyGridRequest gridRequest) {
        TemplateTable templateTable = dao.findById(id);
        String templateName = templateTable.getTemplateName();
        TableDefinition tableDefinition = dataBaseService.getTable(TableType.TEMPLATE_FIELD_MAPPER, templateName);
        List<ColumnVo> columnVos = tableDefinition.getColumnVos();
        List<Map<String, String>> fieldMapper = new ArrayList<>();
        List<String> fieldNames = new ArrayList<>();
        for (ColumnVo columnVo : columnVos) {
            String fieldName = columnVo.getName();
            fieldNames.add(fieldName);
        }
        System.out.println(gridRequest.toString());
        return PageHelper.startPage(gridRequest.getPage(), gridRequest.getRows())
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                .doSelectPageInfo(() -> dao.getAllFiledMapper(templateName, fieldNames, gridRequest));
    }

    /**
     * 这里考虑只修改是否被删除这个字段，并不真正的删除
     *
     * @param templateTable
     * @return
     */
    @Override
    public int del(TemplateTable templateTable) {
        //判定模板中是否有数据
        return dao.del(templateTable);
    }

    /**
     * 这里的服务不会调用，是自动生成的
     *
     * @param ids
     * @return
     */
    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    //根据map中的信息在数据库中创建表，当tableType是"mapper"时在template_field_mapper中创建，当是"data"时在template_data中创建
    private void createTable(Map<String, Object> map, String tableType) {
        System.out.println(map.toString());
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("childrens");

        //这里是在数据库中创建这一张表
        List<ColumnVo> columnVos = new ArrayList<>();
        //这里需要加入一些信息，比如这个字段映射是哪张原始表的，表的拥有者是谁，表示半结构化还是非结构化的等
        if ("mapper".equals(tableType))
            addBasicColumn(columnVos);
        for (Map<String, Object> mmp : list) {
            if ("mapper".equals(tableType)) {
                //这里是创建字段映射表，因此数据类型都是string
                ColumnVo columnVo1 = new ColumnVo();
                columnVo1.setName(mmp.get("fieldName").toString());
                columnVo1.setColumnType(ColumnType.valueOf("VARCHAR"));
                columnVo1.setLength(255);
                columnVo1.setNullable(Boolean.TRUE);
                columnVo1.setIsPk(Boolean.FALSE);
                if (mmp.get("fieldNotes") == "" || mmp.get("fieldNotes") == null) {
                    columnVo1.setComment("");
                } else {
                    columnVo1.setComment(mmp.get("fieldNotes").toString());
                }
                columnVos.add(columnVo1);
            } else {
                //这里是创建数据表，因此数据类型要匹配
//                System.out.println(mmp.toString());
                ColumnVo columnVo1 = new ColumnVo();
                columnVo1.setName(mmp.get("fieldName").toString());
                columnVo1.setColumnType(ColumnType.valueOf(mmp.get("fieldType").toString()));
                if (mmp.get("fieldLength") == "" || mmp.get("fieldLength") == null) {
                    columnVo1.setLength(0);
                } else {
                    columnVo1.setLength(Integer.valueOf(mmp.get("fieldLength").toString()));
                }
//                columnVo1.setNullable(Boolean.valueOf(mmp.get("fieldIsnull").toString()));
                //这里必须设置为true，因为你不知道别人数据库是否有这条数据
                columnVo1.setNullable(Boolean.TRUE);
                columnVo1.setIsPk(Boolean.valueOf(mmp.get("fieldPrimaryKey").toString()));
                if (mmp.get("fieldNotes") == "" || mmp.get("fieldNotes") == null) {
                    columnVo1.setComment("");
                } else {
                    columnVo1.setComment(mmp.get("fieldNotes").toString());
                }
                columnVos.add(columnVo1);
            }
        }
        TableDefinition tableDefinition = new TableDefinition();
        tableDefinition.setName(map.get("templateName").toString());
        tableDefinition.setColumnVos(columnVos);
        tableDefinition.setComment(map.get("notes").toString());
        if ("mapper".equals(tableType))
            tableDefinition.setTableType(TableType.TEMPLATE_FIELD_MAPPER);
        else
            tableDefinition.setTableType(TableType.TEMPLATE_DATA);
        dataBaseService.createTable(tableDefinition);
    }

    //添加字段映射表的基础信息
    private void addBasicColumn(List<ColumnVo> columnVos) {
        ColumnVo guid = new ColumnVo();
        ColumnVo tableGuid = new ColumnVo();
        ColumnVo tableOptTime = new ColumnVo();
        ColumnVo tableOptPerson = new ColumnVo();
        guid.setName("local_guid");
        guid.setLength(32);
        guid.setColumnType(ColumnType.valueOf("VARCHAR"));
        guid.setNullable(false);
        guid.setIsPk(true);
        guid.setComment("主键");
        tableGuid.setName("local_table_guid");
        tableGuid.setLength(32);
        tableGuid.setColumnType(ColumnType.valueOf("VARCHAR"));
        tableGuid.setNullable(false);
        tableGuid.setIsPk(false);
        tableGuid.setComment("数据表对应的guid");
        tableOptTime.setName("local_opt_time");
        tableOptTime.setColumnType(ColumnType.valueOf("TIMESTAMP"));
        tableOptTime.setNullable(false);
        tableOptTime.setIsPk(false);
        tableOptTime.setComment("操作时间");
        tableOptPerson.setName("local_opt_person");
        tableOptPerson.setColumnType(ColumnType.valueOf("VARCHAR"));
        tableOptPerson.setNullable(false);
        tableOptPerson.setIsPk(false);
        tableOptPerson.setComment("操作人");
        columnVos.add(guid);
        columnVos.add(tableGuid);
        columnVos.add(tableOptTime);
        columnVos.add(tableOptPerson);
    }

    private int addTemplateTableInfo(Map<String, Object> map) {
        TemplateTable templateTable = new TemplateTable();
        templateTable.setGuid(IdUtil.simpleUUID());
        templateTable.setTemplateName(map.get("templateName").toString());
        templateTable.setTemplateColumn(JSON.toJSONString(map.get("childrens")));
        templateTable.setTemplateDescribe(map.get("notes").toString());
        templateTable.setCreateTime(new Date());
        templateTable.setOptTime(new Date());
        templateTable.setOptPerson(RedisUtils.getLoginUser().getStr("guid"));
        templateTable.setIsDel('A');
        return dao.add(templateTable);
    }

    /**
     * 这里暂时还没有写
     *
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(Map<String, Object> map) {
        //确保该模板的名字没有改变
        TemplateTable templateTable = dao.findById((String) map.get("guid"));
        String templateName = templateTable.getTemplateName();
        if (!templateName.equals(map.get("templateName"))) {
            throw new CommonException(ResultCode.TABLE_NOT_EXIST);
        }
//        //修改模板的注释
        templateTable.setTemplateDescribe((String) map.get("notes"));
//        //修改操作人
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        templateTable.setOptPerson(guid);
//        //增加模板字段
        List<Map<String, Object>> newColumns = (List<Map<String, Object>>) map.get("childrens");
        JSONArray oldColumns = JSONArray.parseArray(templateTable.getTemplateColumn());
        int size = oldColumns.size();
        System.out.println(oldColumns.toString());
        System.out.println(newColumns.toString());
        for (Map<String, Object> newColumn : newColumns) {
            oldColumns.add(newColumn);
        }
        for (int i = 0; i < size; i++) {
            oldColumns.remove(0);
        }

        System.out.println(oldColumns.toString());
        templateTable.setTemplateColumn(JSONArray.toJSONString(oldColumns));
//        //这里是直接在数据库加字段了
        addFiled2Template(map, "mapper", size);
        addFiled2Template(map, "data", size);
//        //修改时间
        templateTable.setOptTime(new Date());
        return dao.updateByNotEmpty(templateTable);
    }

    private void addFiled2Template(Map<String, Object> map, String tableType, int size) {
//        System.out.println(map.toString());
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("childrens");
        if ("mapper".equals(tableType)) {
            for (int i = 0; i < size; i++) {
                list.remove(0);
            }
        }

        //这里是在数据库中新增的字段信息
        List<ColumnVo> columnVos = new ArrayList<>();
        //这里需要加入一些信息，比如这个字段映射是哪张原始表的，表的拥有者是谁，表示半结构化还是非结构化的等
        if (!dataBaseService.isExistTable(map.get("templateName").toString(), TableType.TEMPLATE_FIELD_MAPPER)) {
            throw new CommonException(ResultCode.TEMPLATE_MAPPER_NOT_EXIST);
        }
        if (!dataBaseService.isExistTable(map.get("templateName").toString(), TableType.TEMPLATE_DATA)) {
            throw new CommonException(ResultCode.TEMPLATE_DATA_TABLE_NOT_EXIST);
        }
        for (Map<String, Object> mmp : list) {
            if ("mapper".equals(tableType)) {
                //这里是创建字段映射表，因此数据类型都是string
                ColumnVo columnVo1 = new ColumnVo();
                columnVo1.setName(mmp.get("fieldName").toString());
                columnVo1.setColumnType(ColumnType.valueOf("VARCHAR"));
                columnVo1.setLength(255);
                columnVo1.setNullable(Boolean.TRUE);
                columnVo1.setIsPk(Boolean.FALSE);
                if (mmp.get("fieldNotes") == "" || mmp.get("fieldNotes") == null) {
                    columnVo1.setComment("");
                } else {
                    columnVo1.setComment(mmp.get("fieldNotes").toString());
                }
                columnVos.add(columnVo1);
            } else {
                //这里是创建数据表，因此数据类型要匹配
//                System.out.println(mmp.toString());
                ColumnVo columnVo1 = new ColumnVo();
                columnVo1.setName(mmp.get("fieldName").toString());
                columnVo1.setColumnType(ColumnType.valueOf(mmp.get("fieldType").toString()));
                if (mmp.get("fieldLength") == "" || mmp.get("fieldLength") == null) {
                    columnVo1.setLength(0);
                } else {
                    columnVo1.setLength(Integer.valueOf(mmp.get("fieldLength").toString()));
                }
//                columnVo1.setNullable(Boolean.valueOf(mmp.get("fieldIsnull").toString()));
                //这里必须设置为true，因为你不知道别人数据库是否有这条数据
                columnVo1.setNullable(Boolean.TRUE);
                columnVo1.setIsPk(Boolean.valueOf(mmp.get("fieldPrimaryKey").toString()));
                if (mmp.get("fieldNotes") == "" || mmp.get("fieldNotes") == null) {
                    columnVo1.setComment("");
                } else {
                    columnVo1.setComment(mmp.get("fieldNotes").toString());
                }
                columnVos.add(columnVo1);
            }
        }
        TableDefinition tableDefinition = new TableDefinition();
        tableDefinition.setName(map.get("templateName").toString());
        tableDefinition.setColumnVos(columnVos);
        tableDefinition.setComment(map.get("notes").toString());
        if ("mapper".equals(tableType))
            tableDefinition.setTableType(TableType.TEMPLATE_FIELD_MAPPER);
        else
            tableDefinition.setTableType(TableType.TEMPLATE_DATA);
        dataBaseService.addField(tableDefinition);
    }

    /**
     * 找到guid的模板表具体信息
     *
     * @param id
     * @return
     */
    @Override
    public TemplateTable findById(Serializable id) {
        return dao.findById(id);
    }

    /**
     * 修改guid 模板表的信息，前提是guid不为空
     *
     * @param templateTable
     * @return
     */
    @Override
    public int updateByNotEmpty(TemplateTable templateTable) {
        return dao.updateByNotEmpty(templateTable);
    }

    @Override
    public PageInfo<Map<String, Object>> getTemplateData(EasyGridRequest gridRequest, String templateGuid) {
        //获取templateName
        TemplateTable templateTable = dao.findById(templateGuid);
        if (templateTable == null) {
            throw new CommonException(ResultCode.RESULE_DATA_NONE);
        }
        PageInfo<Map<String, Object>> pageInfo;
        //通过EasyGridRequest去查询其个数
        if (gridRequest.getSort().equals("tablealins.opt_time")) {
            pageInfo = PageHelper
                    //分页
                    .startPage(gridRequest.getPage(), gridRequest.getRows())
                    //排序
                    //因为这里没有时间，因此不需要排序
                    //                    .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                    //数据
                    .doSelectPageInfo(() -> dataBaseDao.findDataFromTemplate(TableType.TEMPLATE_DATA, templateTable.getTemplateName(), gridRequest));
        } else {
            pageInfo = PageHelper
                    //分页
                    .startPage(gridRequest.getPage(), gridRequest.getRows())
                    //排序
                    //因为这里不一定有时间，因此不需要排序
                    .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                    //数据
                    .doSelectPageInfo(() -> dataBaseDao.findDataFromTemplate(TableType.TEMPLATE_DATA, templateTable.getTemplateName(), gridRequest));
        }

        return pageInfo;
    }

    /**
     * 获取所有的模板表
     *
     * @return
     */
    @Override
    public List<TemplateTable> findAll() {
        return dao.findAll();
    }

    /**
     * 分页查询
     *
     * @param gridRequest
     * @return
     */
    @Override
    public PageInfo<TemplateTable> findByGridRequest(EasyGridRequest gridRequest) {
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
    }

    @Override
    public Map<String, String> getmapper(Map<String, String> map) {
        Map<String, String> mmp = new HashMap<>();
        String templateName = map.get("templateName");
        String original_table = map.get("original_table");
        List<String> fieldNames = dao.findByName(templateName);
        System.out.println(fieldNames.toString());
        List<Map<String, String>> values = dao.findByFields(fieldNames, templateName, original_table);
        System.out.println(values.toString());
        mmp = values.get(0);

        return mmp;
    }

    /**
     * 获得这张表的guid模板表的字段信息
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findDetail(Serializable id) {
        Map<String, Object> mmp = new HashMap<>();
        TemplateTable templateTable = dao.findById(id);
        TableDefinition tableDefinition = dataBaseService.getTable(TableType.TEMPLATE_DATA, templateTable.getTemplateName());
        mmp.put("tableDefinition", tableDefinition);
        return mmp;
    }
}
