package com.boyoi.work.service;

import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.service.BaseService;
import com.boyoi.work.entity.FieldMapperInfo;
import com.boyoi.work.entity.LinkTable;
import com.boyoi.work.entity.TemplateTable;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 模板表表信息 业务接口层
 *
 * @author LiYucheng
 * @date 2021-4-21
 */
public interface TemplateTableService extends BaseService {

    int add(Map<String, Object> map);

    int addBatch(List<TemplateTable> list);

    int del(TemplateTable templateTable);

    PageInfo<Map<String, Object>> getFieldMapperInfo(Serializable id, EasyGridRequest easyGridRequest);

    int delBatch(List<Serializable> ids);

    int update(Map<String, Object> map);

    Map<String, Object> findDetail(Serializable id);

    int updateByNotEmpty(TemplateTable templateTable);

    TemplateTable findById(Serializable id);

    List<TemplateTable> findAll();

    PageInfo<TemplateTable> findByGridRequest(EasyGridRequest gridRequest);

    Map<String, String> getmapper(Map<String, String> map);

    PageInfo<Map<String, Object>> getTemplateData(EasyGridRequest gridRequest, String templateGuid);
}
