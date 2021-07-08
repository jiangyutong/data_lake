package com.boyoi.base.service.impl;

import cn.hutool.core.util.IdUtil;
import com.boyoi.base.dao.OptLogDao;
import com.boyoi.base.service.OptLogService;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.OptLog;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.core.util.AddressUtils;
import com.boyoi.core.util.DateTime;
import com.boyoi.core.util.DateTimeUtil;
import com.boyoi.core.util.IpUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 操作日志 业务实现类
 *
 * @author ZhouJL
 * @date 2018/12/20 15:33
 */
@Service
public class OptLogServiceImpl extends BaseServiceImpl<OptLogDao> implements OptLogService {

    @Resource
    private OptLogDao dao;

    @Autowired
    private HttpServletRequest request;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(OptLog optLog) {
        optLog.setGuid(IdUtil.simpleUUID());
        optLog.setDeptName("运维部门");
        String ip = IpUtils.getIpAddr(request);
        optLog.setOperIp(ip);
        optLog.setOperLocation(AddressUtils.getRealAddressByIp(ip));
        optLog.setOperTime(new Date());
        return dao.add(optLog);
    }

    @Override
    public int addBatch(List<OptLog> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(OptLog optLog) {
        return dao.del(optLog);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    public int update(OptLog optLog) {
        return dao.update(optLog);
    }

    @Override
    public int updateByNotEmpty(OptLog optLog) {
        return dao.updateByNotEmpty(optLog);
    }

    @Override
    public OptLog findById(Serializable id) {
        return dao.findById(id);
    }

    @Override
    public List<OptLog> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<OptLog> findByGridRequest(EasyGridRequest gridRequest) {
        String sort = "tablealins.opt_time";
        if (sort.equals(gridRequest.getSort())) {
            gridRequest.setSort("tablealins.oper_time");
        }
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
    }

    @Override
    public Map<String, Object> queryData() {
        String startTime = "";
        String[] optLogDate = new String[7];
        int[] optLogData = new int[7];
        int l = 0;
        for (int i = 6; i >= 0; i--) {
            startTime = DateTimeUtil.yyyyMMdd(DateTime.addDays(new Date(), -i));
            Map<String, Object> queryMap = dao.queryData(startTime, startTime);
            optLogDate[l] = startTime;
            optLogData[l] = Integer.valueOf(queryMap.get("sl").toString());
            l++;
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("optLogDate", optLogDate);
        returnMap.put("optLogData", optLogData);
        return returnMap;
    }
}
