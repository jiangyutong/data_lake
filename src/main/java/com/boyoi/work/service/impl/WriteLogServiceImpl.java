package com.boyoi.work.service.impl;

import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.User;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.core.util.DateTime;
import com.boyoi.core.util.DateTimeUtil;
import com.boyoi.work.dao.QueryLogDao;
import com.boyoi.work.entity.WriteLog;
import com.boyoi.work.service.WriteLogService;
import com.boyoi.work.dao.WriteLogDao;
import com.boyoi.core.entity.EasyGridRequest;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 描述：
 * 数据写入日志 业务实现类
 *
 * @author Harvey Y.L.Q.
 * @date 2018/12/20 15:33
 */
@Service
public class WriteLogServiceImpl extends BaseServiceImpl<WriteLogDao> implements WriteLogService {

    @Resource
    private WriteLogDao dao;

    @Resource
    private QueryLogDao queryLogDao;

    @Override
    public int add(WriteLog writeLog) {
        Date date = new Date();
        writeLog.setCreateTime(date);
        writeLog.setOptTime(date);
        writeLog.setState("A");
        writeLog.setLine(0);
        return dao.add(writeLog);
    }

    @Override
    public int addBatch(List<WriteLog> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(WriteLog writeLog) {
        return dao.del(writeLog);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    public int update(WriteLog writeLog) {
        return dao.update(writeLog);
    }

    @Override
    public int updateByNotEmpty(WriteLog writeLog) {
        return dao.updateByNotEmpty(writeLog);
    }

    @Override
    public WriteLog findById(Serializable id) {
        return dao.findById(id);
    }

    @Override
    public List<WriteLog> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<WriteLog> findByGridRequest(EasyGridRequest gridRequest) {
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
    public Map<String, Object> queryData() {
        int day = 7;
        Map<String, Object> returMap = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> timeList = new ArrayList<>();
        for (int i = 6; i > 0; i--) {
            String endTime = DateTimeUtil.yyyyMMdd(DateTime.addDays(new Date(), -(day * (i - 1))));
            String startTime = DateTimeUtil.yyyyMMdd(DateTime.addDays(new Date(), -(day * i - 1)));
            Map<String, Object> mmp = new HashMap<>();
            mmp.put("startTime", startTime);
            mmp.put("endTime", endTime);
            timeList.add(mmp);
        }

        String[] writeDate = new String[timeList.size()];
        int[] writeDatasjl = new int[timeList.size()];
        //数据量统计
        for (int i = 0; i < timeList.size(); i++) {
            map = timeList.get(i);
            writeDate[i] = map.get("startTime").toString().substring(5).replaceAll("-", ".") + "-" + map.get("endTime").toString().substring(5).replaceAll("-", ".");
            Map<String, Object> cglMap = dao.queryData(map);
            if (cglMap != null) {
                if (cglMap.get("line") == "" || cglMap.get("line") == null) {
                    writeDatasjl[i] = 0;
                } else {
                    writeDatasjl[i] = Integer.valueOf(cglMap.get("line").toString());
                }
            } else {
                writeDatasjl[i] = 0;
            }

        }
        returMap.put("writeDate", writeDate);
        returMap.put("writeDatasjl", writeDatasjl);

        //查询数据量查询
        int[] queryDatasjl = new int[timeList.size()];
        for (int i = 0; i < timeList.size(); i++) {
            map = timeList.get(i);
            Map<String, Object> cglMap = queryLogDao.queryData(map);
            if (cglMap != null) {
                if (cglMap.get("line") == "" || cglMap.get("line") == null) {
                    queryDatasjl[i] = 0;
                } else {
                    queryDatasjl[i] = Integer.valueOf(cglMap.get("line").toString());
                }
            } else {
                queryDatasjl[i] = 0;
            }

        }
        returMap.put("queryDatasjl", queryDatasjl);

        map = new HashMap<>();
        Map<String, Object> numMap = dao.queryData(map);
        if (numMap != null) {
            if (numMap.get("line") == "" || numMap.get("line") == null) {
                returMap.put("writeNum", 0);
            } else {
                returMap.put("writeNum", numMap.get("line"));
            }
        } else {
            returMap.put("writeNum", 0);
        }
        return returMap;
    }
}
