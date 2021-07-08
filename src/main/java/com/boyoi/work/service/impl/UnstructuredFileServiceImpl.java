package com.boyoi.work.service.impl;

import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.work.entity.UnstructuredFile;
import com.boyoi.work.service.UnstructuredFileService;
import com.boyoi.work.dao.UnstructuredFileDao;
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
 * 文件上传存储信息表 业务实现类
 *
 * @author Harvey Y.L.Q.
 * @date 2018/12/20 15:33
 */
@Service
public class UnstructuredFileServiceImpl extends BaseServiceImpl<UnstructuredFileDao> implements UnstructuredFileService {

    @Resource
    private UnstructuredFileDao dao;

    @Override
    public int add(UnstructuredFile unstructuredFile) {
        unstructuredFile.setGuid(IdUtil.simpleUUID());
        unstructuredFile.setCreateTime(new Date());
        unstructuredFile.setOptTime(new Date());
        unstructuredFile.setOptPerson(RedisUtils.getLoginUser().getStr("guid"));
        unstructuredFile.setOptTimeStr(RedisUtils.getLoginUser().getStr("userName"));
        unstructuredFile.setIsDeleted("A");
        return dao.add(unstructuredFile);
    }

    @Override
    public int addBatch(List<UnstructuredFile> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(UnstructuredFile unstructuredFile) {
        return dao.del(unstructuredFile);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    public int update(UnstructuredFile unstructuredFile) {
        return dao.update(unstructuredFile);
    }

    @Override
    public int updateByNotEmpty(UnstructuredFile unstructuredFile) {
        return dao.updateByNotEmpty(unstructuredFile);
    }

    @Override
    public UnstructuredFile findById(Serializable id) {
        return dao.findById(id);
    }

    @Override
    public List<UnstructuredFile> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<UnstructuredFile> findByGridRequest(EasyGridRequest gridRequest) {
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
        Map<String, Object> mmp = new HashMap<>();
        List<Map<String, Object>> reterList = new ArrayList<>();
        List<Map<String, Object>> list = dao.queryData();
        int zsl = 0;
        for (Map<String, Object> map : list) {
            mmp = new HashMap<>();
            mmp.put("value", map.get("sl"));
            if (map.get("ftype").equals("T")) {
                mmp.put("name", "图片");
            } else if (map.get("ftype").equals("Y")) {
                mmp.put("name", "音频");
            } else if (map.get("ftype").equals("S")) {
                mmp.put("name", "视频");
            } else if (map.get("ftype").equals("W")) {
                mmp.put("name", "文档");
            } else if (map.get("ftype").equals("Q")) {
                mmp.put("name", "其它");
            }
            zsl += Integer.valueOf(map.get("sl").toString());
            reterList.add(mmp);
        }
        mmp = new HashMap<>();
        mmp.put("fileNum", zsl);
        mmp.put("fileList", reterList);
        return mmp;
    }
}
