package com.boyoi.work.service.impl;

import cn.hutool.core.date.DateUtil;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.User;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.core.util.DateTime;
import com.boyoi.core.util.DateTimeUtil;
import com.boyoi.work.dao.WriteLogDao;
import com.boyoi.work.entity.QueryLog;
import com.boyoi.work.service.QueryLogService;
import com.boyoi.work.dao.QueryLogDao;
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
public class QueryLogServiceImpl extends BaseServiceImpl<QueryLogDao> implements QueryLogService {

    @Resource
    private QueryLogDao dao;

    @Resource
    private WriteLogDao writeLogDao;

    @Override
    public int add(QueryLog queryLog) {
        Date date = new Date();
        queryLog.setCreateTime(date);
        queryLog.setOptTime(date);
        queryLog.setState("A");
        queryLog.setLine(0);
        return dao.add(queryLog);
    }

    @Override
    public int addBatch(List<QueryLog> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(QueryLog queryLog) {
        return dao.del(queryLog);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    public int update(QueryLog queryLog) {
        return dao.update(queryLog);
    }

    @Override
    public int updateByNotEmpty(QueryLog queryLog) {
        return dao.updateByNotEmpty(queryLog);
    }

    @Override
    public QueryLog findById(Serializable id) {
        return dao.findById(id);
    }

    @Override
    public List<QueryLog> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<QueryLog> findByGridRequest(EasyGridRequest gridRequest) {
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
        String[] queryDate = new String[timeList.size()];

        //写入数据量查询
        int[] queryDatajlcgl = new int[timeList.size()];
        int[] queryDatajlsbl = new int[timeList.size()];
        for (int i = 0; i < timeList.size(); i++) {
            map = timeList.get(i);
            queryDate[i] = map.get("startTime").toString().substring(5).replaceAll("-", ".") + "-" + map.get("endTime").toString().substring(5).replaceAll("-", ".");

            map.put("state", "('B')");
            Map<String, Object> cglMap = dao.queryDatajl(map);
            if (cglMap != null) {
                if (cglMap.get("jll") == "" || cglMap.get("jll") == null) {
                    queryDatajlcgl[i] = 0;
                } else {
                    queryDatajlcgl[i] = Integer.valueOf(cglMap.get("jll").toString());
                }
            } else {
                queryDatajlcgl[i] = 0;
            }

            map.put("state", "('C','D')");
            Map<String, Object> jlMap = dao.queryDatajl(map);
            if (cglMap != null) {
                if (jlMap.get("jll") == "" || jlMap.get("jll") == null) {
                    queryDatajlsbl[i] = 0;
                } else {
                    queryDatajlsbl[i] = Integer.valueOf(jlMap.get("jll").toString());
                }
            } else {
                queryDatajlsbl[i] = 0;
            }

        }
        returMap.put("queryDate", queryDate);
        returMap.put("queryDatajlcgl", queryDatajlcgl);
        returMap.put("queryDatajlsbl", queryDatajlsbl);

        //写入记录量查询
        int[] writeDatajlcgl = new int[timeList.size()];
        int[] writeDatajlsbl = new int[timeList.size()];
        for (int i = 0; i < timeList.size(); i++) {
            map = timeList.get(i);
            map.put("state", "('B')");
            Map<String, Object> cglMap = writeLogDao.queryDatajl(map);
            if (cglMap != null) {
                if (cglMap.get("jll") == "" || cglMap.get("jll") == null) {
                    writeDatajlcgl[i] = 0;
                } else {
                    writeDatajlcgl[i] = Integer.valueOf(cglMap.get("jll").toString());
                }
            } else {
                writeDatajlcgl[i] = 0;
            }

            map.put("state", "('C','D')");
            Map<String, Object> sblMap = writeLogDao.queryDatajl(map);
            if (cglMap != null) {
                if (sblMap.get("jll") == "" || sblMap.get("jll") == null) {
                    writeDatajlsbl[i] = 0;
                } else {
                    writeDatajlsbl[i] = Integer.valueOf(sblMap.get("jll").toString());
                }
            } else {
                writeDatajlsbl[i] = 0;
            }
        }
        returMap.put("writeDatajlcgl", writeDatajlcgl);
        returMap.put("writeDatajlsbl", writeDatajlsbl);

        map = new HashMap<>();
        Map<String, Object> numMap = dao.queryData(map);
        if (numMap != null) {
            if (numMap.get("line") == "" || numMap.get("line") == null) {
                returMap.put("queryNum", 0);
            } else {
                returMap.put("queryNum", numMap.get("line"));
            }
        } else {
            returMap.put("queryNum", 0);
        }
        return returMap;
    }
}
