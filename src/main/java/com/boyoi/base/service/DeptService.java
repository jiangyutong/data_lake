package com.boyoi.base.service;

import cn.hutool.core.lang.tree.Tree;
import com.boyoi.core.entity.Dept;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.service.BaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 部门 业务接口层
 *
 * @author ZhouJL
 * @date 2020-05-09
 */
public interface DeptService extends BaseService {

    /**
     * 保存一个实体
     *
     * @param dept 实体
     * @return 受影响的个数
     */
    int add(Dept dept);

    /**
     * 批量保存
     *
     * @param list 实体List集合
     * @return 受影响的个数
     */
    int addBatch(List<Dept> list);

    /**
     * 删除一个实体
     *
     * @param dept 实体内容
     * @return 受影响的个数
     */
    int del(Dept dept);

    /**
     * 批量删除实体
     *
     * @param ids id的List集合
     * @return 受影响的个数
     */
    int delBatch(List<Serializable> ids);

    /**
     * 更新一个实体
     *
     * @param dept 实体
     * @return 受影响的个数
     */
    int update(Dept dept);

    /**
     * 更新一个实体,条件更新,不为 null 和 空字符
     *
     * @param dept 实体
     * @return 受影响的个数
     */
    int updateByNotEmpty(Dept dept);

    /**
     * 通过ID查找实体
     *
     * @param id id
     * @return 实体
     */
    Dept findById(Serializable id);

    /**
     * 查找所有实体
     *
     * @return 实体集合
     */
    List<Dept> findAll();

    /**
     * 通过EasyGridRequest条件查找
     *
     * @param gridRequest EasyGridRequest请求
     * @return 符合条件的数据集合
     */
    List<Tree<String>> findByGridRequest(EasyGridRequest gridRequest);

    List<Map<String, Object>> findAllTest(Map<String, String> map);
}
