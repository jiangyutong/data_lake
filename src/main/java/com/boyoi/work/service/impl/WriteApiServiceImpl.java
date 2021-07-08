package com.boyoi.work.service.impl;

import com.boyoi.base.service.DataBaseService;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.CommonException;
import com.boyoi.core.entity.TableDefinition;
import com.boyoi.core.entity.User;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.work.entity.WriteApi;
import com.boyoi.work.service.WriteApiService;
import com.boyoi.work.dao.WriteApiDao;
import com.boyoi.core.entity.EasyGridRequest;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 描述：
 * 查询接口 业务实现类
 *
 * @author Harvey Y.L.Q.
 * @date 2018/12/20 15:33
 */
@Service
public class WriteApiServiceImpl extends BaseServiceImpl<WriteApiDao> implements WriteApiService {

    @Resource
    private WriteApiDao dao;
    @Resource
    private DataBaseService dataBaseService;

    @Override
    public int add(WriteApi writeApi) {
        String apiUrl = writeApi.getApiUrl();
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        WriteApi byUrl = dao.findByUrl(apiUrl, guid);
        if (byUrl != null) {
            throw new CommonException(ResultCode.URL_EXIST);
        }
        boolean existTable = dataBaseService.isExistTable(writeApi.getTableName(), writeApi.getTableType());
        if (!existTable) {
            throw new CommonException(ResultCode.TABLE_NOT_EXIST);
        }
        writeApi.setGuid(IdUtil.simpleUUID());
        writeApi.setOptPerson(thisUser.getUserName());
        writeApi.setOptTime(new Date());
        writeApi.setCreateTime(new Date());
        writeApi.setState("1");
        writeApi.setCreateUser(guid);
        return dao.add(writeApi);
    }

    @Override
    public int addBatch(List<WriteApi> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(WriteApi writeApi) {
        return dao.del(writeApi);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    public int update(WriteApi writeApi) {
        String apiUrl = writeApi.getApiUrl();
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        WriteApi byUrl = dao.findByUrl(apiUrl, guid);
        if (byUrl != null && !writeApi.getGuid().equals(byUrl.getGuid())) {
            throw new CommonException(ResultCode.URL_EXIST);
        }
        boolean existTable = dataBaseService.isExistTable(writeApi.getTableName(), writeApi.getTableType());
        if (!existTable) {
            throw new CommonException(ResultCode.TABLE_NOT_EXIST);
        }
        writeApi.setOptPerson(thisUser.getUserName());
        writeApi.setOptTime(new Date());
        return dao.updateByNotEmpty(writeApi);
    }

    @Override
    public int updateByNotEmpty(WriteApi writeApi) {
        return dao.updateByNotEmpty(writeApi);
    }

    @Override
    public WriteApi findById(Serializable id) {
        WriteApi byId = dao.findById(id);
        if (byId != null) {
            TableDefinition table = dataBaseService.getTable(byId.getTableType(), byId.getTableName());
            byId.setTableDefinition(table);
        }
        return byId;
    }

    @Override
    public List<WriteApi> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<WriteApi> findByGridRequest(EasyGridRequest gridRequest) {
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        gridRequest.getMap2().put("createUser", guid);
        PageInfo<WriteApi> pageInfo = PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
        pageInfo.getList().forEach(api -> {
            boolean existTable = dataBaseService.isExistTable(api.getTableName(), api.getTableType());
            api.setTableState(existTable ? "1" : "0");
        });
        return pageInfo;
    }

    @Override
    public int updateState(WriteApi writeApi) {
        String state = writeApi.getState();
        writeApi.setState("1".equals(state) ? "0" : "1");
        writeApi.setOptTime(new Date());
        writeApi.setOptPerson(Objects.requireNonNull(RedisUtils.getThisUser()).getUserName());
        return dao.updateByNotEmpty(writeApi);
    }

    @Override
    public List<WriteApi> validList() {
        List<WriteApi> writeApis = new ArrayList<>();
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        WriteApi writeApi = new WriteApi();
        writeApi.setState("1");
        writeApi.setCreateUser(guid);
        List<WriteApi> byDomain = dao.findByDomain(writeApi);
        byDomain.forEach(by -> {
            boolean existTable = dataBaseService.isExistTable(by.getTableName(), by.getTableType());
            if (existTable) {
                TableDefinition table = dataBaseService.getTable(by.getTableType(), by.getTableName());
                by.setTableDefinition(table);
                writeApis.add(by);
            }
        });
        return writeApis;
    }

    @Override
    public WriteApi findByUrl(String apiUrl, String userId) {
        return dao.findByUrl(apiUrl, userId);
    }
}
