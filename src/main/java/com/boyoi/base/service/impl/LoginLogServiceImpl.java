package com.boyoi.base.service.impl;

import cn.hutool.core.util.IdUtil;
import com.boyoi.base.dao.LoginLogDao;
import com.boyoi.base.service.LoginLogService;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.LoginLog;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 系统访问记录 业务实现类
 *
 * @author ZhouJL
 * @date 2018/12/20 15:33
 */
@Service
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLogDao> implements LoginLogService {

    @Resource
    private LoginLogDao dao;

    @Override
    @Async
    public int add(LoginLog loginLog) {
        loginLog.setGuid(IdUtil.simpleUUID());
        return dao.add(loginLog);
    }

    @Override
    public int addBatch(List<LoginLog> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(LoginLog loginLog) {
        return dao.del(loginLog);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    public int update(LoginLog loginLog) {
        return dao.update(loginLog);
    }

    @Override
    public int updateByNotEmpty(LoginLog loginLog) {
        return dao.updateByNotEmpty(loginLog);
    }

    @Override
    public LoginLog findById(Serializable id) {
        return dao.findById(id);
    }

    @Override
    public List<LoginLog> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<LoginLog> findByGridRequest(EasyGridRequest gridRequest) {
        String sort = "tablealins.opt_time";
        if (sort.equals(gridRequest.getSort())) {
            gridRequest.setSort("tablealins.login_time");
        }
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
    }
}
