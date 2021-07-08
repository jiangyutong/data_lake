package com.boyoi.work.service.impl;

import com.boyoi.base.service.DataBaseService;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.*;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.enums.TableType;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.work.dao.LinkTableDao;
import com.boyoi.work.dao.NewWriteApiDao;
import com.boyoi.work.dao.TemplateTableDao;
import com.boyoi.work.entity.LinkTable;
import com.boyoi.work.entity.NewWriteApi;
import com.boyoi.work.entity.TemplateTable;
import com.boyoi.work.entity.WriteApi;
import com.boyoi.work.service.NewWriteApiService;
import com.boyoi.work.service.WriteApiService;
import com.boyoi.work.dao.WriteApiDao;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.*;

/**
 * 描述：
 * 查询接口 业务实现类
 *
 * @author Harvey Y.L.Q.
 * @date 2018/12/20 15:33
 */
@Service
public class NewWriteApiServiceImpl extends BaseServiceImpl<NewWriteApiDao> implements NewWriteApiService {

    @Resource
    private NewWriteApiDao dao;
    @Resource
    private LinkTableDao linkTableDao;
    @Resource
    private TemplateTableDao templateTableDao;
    @Resource
    private DataBaseService dataBaseService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(Map<String, Object> map) {
        //获取linkTable
        LinkTable linkTable = linkTableDao.findById(map.get("linkTableGuid").toString());
        if (null == linkTable) {
            throw new CommonException(ResultCode.TABLE_NOT_EXIST);
        }
        //获取template
        TemplateTable templateTable = templateTableDao.findById(map.get("templateTableGuid").toString());
        if (null == templateTable) {
            throw new CommonException(ResultCode.TABLE_NOT_EXIST);
        }
        //TODO: 这里要验证字段是否匹配！！！ 这里还没做
        //组装NewWriteApi实例
        NewWriteApi newWriteApi = new NewWriteApi();
        newWriteApi.setGuid(IdUtil.simpleUUID());
        newWriteApi.setTemplateGuid(map.get("templateTableGuid").toString());
        newWriteApi.setTableGuid(map.get("linkTableGuid").toString());
        // 这里注意一下，一个接口对应了一个mapper的guid
        String mapperGuid = IdUtil.simpleUUID();
        newWriteApi.setMapperGuid(mapperGuid);
        newWriteApi.setTemplateName(templateTable.getTemplateName());
        newWriteApi.setTableName(linkTable.getTableName());
        newWriteApi.setApiUrl(map.get("APIAddress").toString());
        newWriteApi.setApiName(map.get("APIName").toString());
        //TODO 这里有问题，因为linkTable中的字段只有J和D，这里是DB和JSON，还要处理一下
        if (linkTable.getType().equals("J")) {
            newWriteApi.setTableType(TableType.valueOf("JSON"));
        } else {
            newWriteApi.setTableType(TableType.valueOf("DB"));
        }

        newWriteApi.setState("1");
        String userGuid = RedisUtils.getLoginUser().getStr("guid");
        newWriteApi.setOptPerson(userGuid);
        newWriteApi.setCreateTime(new Date());
        newWriteApi.setOptTime(new Date());
        //在new_api_write中写入
        //dao.add(newWriteApi);
        //组装template_field_mapper
        HashMap<String, Object> fieldMapper = new HashMap<>();
        fieldMapper.put("local_guid", mapperGuid);
        fieldMapper.put("original_table_name", linkTable.getTableName());
        //fieldMapper.put("local_table_guid", linkTable.getGuid());
        fieldMapper.put("opt_time", new Date());
        fieldMapper.put("table_owner", userGuid);
        fieldMapper.put("table_type", linkTable.getType());
        HashMap<String, Object> fieldMapper1 = (HashMap<String, Object>) map.get("fieldMapper");
        fieldMapper.putAll(fieldMapper1);
        //在template_field_mapper中写入
        dataBaseService.insert(TableType.TEMPLATE_FIELD_MAPPER, templateTable.getTemplateName(), fieldMapper);
        return dao.add(newWriteApi);
    }

    @Override
    public int addBatch(List<NewWriteApi> list) {
        return 0;
    }

    @Override
    public int del(NewWriteApi newWriteApi) {
        return 0;
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Map<String, Object> map) {
        NewWriteApi newWriteApi = findById(map.get("guid").toString());
        if (null == newWriteApi) {
            throw new CommonException(ResultCode.TABLE_NOT_EXIST);
        }
        //获取linkTable
        LinkTable linkTable = linkTableDao.findById(map.get("linkTableGuid").toString());
        if (null == linkTable) {
            throw new CommonException(ResultCode.TABLE_NOT_EXIST);
        }
        //获取template
        TemplateTable templateTable = templateTableDao.findById(map.get("templateTableGuid").toString());
        if (null == templateTable) {
            throw new CommonException(ResultCode.TABLE_NOT_EXIST);
        }
        //删除原来的mapper映射
        dataBaseService.delByKey(TableType.TEMPLATE_FIELD_MAPPER, newWriteApi.getTemplateName(), "local_guid", newWriteApi.getMapperGuid());
        //更新api数据
        newWriteApi.setTemplateGuid(map.get("templateTableGuid").toString());
        newWriteApi.setTableGuid(map.get("linkTableGuid").toString());
        newWriteApi.setTemplateName(templateTable.getTemplateName());
        newWriteApi.setTableName(linkTable.getTableName());
        newWriteApi.setApiUrl(map.get("APIAddress").toString());
        newWriteApi.setApiName(map.get("APIName").toString());
        if (linkTable.getType().equals("J")) {
            newWriteApi.setTableType(TableType.valueOf("JSON"));
        } else {
            newWriteApi.setTableType(TableType.valueOf("DB"));
        }
        String userGuid = RedisUtils.getLoginUser().getStr("guid");
        newWriteApi.setOptPerson(userGuid);
        newWriteApi.setOptTime(new Date());
        dao.updateByNotEmpty(newWriteApi);
        //添加新的mapper字段映射
        HashMap<String, Object> fieldMapper = new HashMap<>();
        fieldMapper.put("local_guid", newWriteApi.getMapperGuid());
        fieldMapper.put("original_table_name", linkTable.getTableName());
        //fieldMapper.put("local_table_guid", linkTable.getGuid());
        fieldMapper.put("opt_time", new Date());
        fieldMapper.put("table_owner", userGuid);
        fieldMapper.put("table_type", linkTable.getType());
        HashMap<String, Object> fieldMapper1 = (HashMap<String, Object>) map.get("fieldMapper");
        fieldMapper.putAll(fieldMapper1);
        dataBaseService.insert(TableType.TEMPLATE_FIELD_MAPPER, templateTable.getTemplateName(), fieldMapper);
        return 0;
    }

    @Override
    public int updateByNotEmpty(NewWriteApi newWriteApi) {
        return 0;
    }

    @Override
    public NewWriteApi findById(Serializable id) {
        NewWriteApi byId = dao.findById(id);
        if (byId != null) {
            TableDefinition table = dataBaseService.getTable(byId.getTableType(), byId.getTableName());
            byId.setTableDefinition(table);
        }
        return byId;
    }

    @Override
    public List<NewWriteApi> findAll() {
        return null;
    }

    @Override
    public PageInfo<NewWriteApi> findByGridRequest(EasyGridRequest gridRequest) {
        //这里的目的是只获取该用户所创建的接口
        User user = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(user).getGuid();
        gridRequest.getMap2().put("createUser", guid);
        //这里是搜索的结果
        PageInfo<NewWriteApi> pageInfo = PageHelper
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
        return pageInfo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateState(NewWriteApi newWriteApi) {
        String state = newWriteApi.getState();
        newWriteApi.setState("1".equals(state) ? "0" : "1");
        newWriteApi.setOptTime(new Date());
        newWriteApi.setOptPerson(Objects.requireNonNull(RedisUtils.getThisUser()).getUserName());
        return dao.updateByNotEmpty(newWriteApi);
    }

    @Override
    public List<NewWriteApi> validList() {
        return dao.findValidList();
    }

    @Override
    public NewWriteApi findByUrl(String apiUrl, String userId) {
        return dao.findByUrl(apiUrl, userId);
    }
}
