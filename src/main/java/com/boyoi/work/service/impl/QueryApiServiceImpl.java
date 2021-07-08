package com.boyoi.work.service.impl;

import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.*;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.core.util.ConverterUtil;
import com.boyoi.work.entity.QueryApi;
import com.boyoi.work.entity.WriteApi;
import com.boyoi.work.service.QueryApiService;
import com.boyoi.work.dao.QueryApiDao;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
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
public class QueryApiServiceImpl extends BaseServiceImpl<QueryApiDao> implements QueryApiService {

    @Resource
    private QueryApiDao dao;

    @Override
    public int add(QueryApi queryApi) {
        String apiUrl = queryApi.getApiUrl();
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        QueryApi byUrl = dao.findByUrl(apiUrl, guid);
        if (byUrl != null) {
            throw new CommonException(ResultCode.URL_EXIST);
        }
        queryApi.setGuid(IdUtil.simpleUUID());
        queryApi.setOptPerson(thisUser.getUserName());
        queryApi.setCreateUser(thisUser.getGuid());
        queryApi.setOptTime(new Date());
        queryApi.setCreateTime(new Date());
        queryApi.setState("1");
        return dao.add(queryApi);
    }

    @Override
    public int addBatch(List<QueryApi> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(QueryApi queryApi) {
        return dao.del(queryApi);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    public int update(QueryApi queryApi) {
        String apiUrl = queryApi.getApiUrl();
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        QueryApi byUrl = dao.findByUrl(apiUrl, guid);
        if (byUrl != null && !queryApi.getGuid().equals(byUrl.getGuid())) {
            throw new CommonException(ResultCode.URL_EXIST);
        }
        queryApi.setOptPerson(thisUser.getUserName());
        queryApi.setOptTime(new Date());
        return dao.updateByNotEmpty(queryApi);
    }

    @Override
    public int updateByNotEmpty(QueryApi queryApi) {
        return dao.updateByNotEmpty(queryApi);
    }

    @Override
    public QueryApi findById(Serializable id) {
        QueryApi byId = dao.findById(id);
        if (byId != null) {
            IBoundSql iBoundSql = ConverterUtil.sqlText2boundSql(byId.getSqlText());
            byId.setIBoundSql(iBoundSql);
        }
        return byId;
    }

    @Override
    public List<QueryApi> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<QueryApi> findByGridRequest(EasyGridRequest gridRequest) {
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        gridRequest.getMap2().put("createUser", guid);
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
    }

    @Override
    public int updateState(QueryApi queryApi) {
        String state = queryApi.getState();
        queryApi.setState("1".equals(state) ? "0" : "1");
        queryApi.setOptTime(new Date());
        queryApi.setOptPerson(Objects.requireNonNull(RedisUtils.getThisUser()).getUserName());
        return dao.updateByNotEmpty(queryApi);
    }

    @Override
    public List<QueryApi> validList() {
        User thisUser = RedisUtils.getThisUser();
        String guid = Objects.requireNonNull(thisUser).getGuid();
        QueryApi queryApi = new QueryApi();
        queryApi.setState("1");
        queryApi.setCreateUser(guid);
        List<QueryApi> byDomain = dao.findByDomain(queryApi);
        byDomain.forEach(by -> {
            IBoundSql iBoundSql = ConverterUtil.sqlText2boundSql(by.getSqlText());
            by.setIBoundSql(iBoundSql);
        });
        return byDomain;
    }

    @Override
    public QueryApi findByUrl(String apiUrl, String userId) {
        QueryApi byUrl = dao.findByUrl(apiUrl, userId);
        if (byUrl != null) {
            IBoundSql iBoundSql = ConverterUtil.sqlText2boundSql(byUrl.getSqlText());
            byUrl.setIBoundSql(iBoundSql);
        }
        return byUrl;
    }
}
