package com.boyoi.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.IdUtil;
import com.boyoi.base.dao.DeptDao;
import com.boyoi.base.dao.UserDao;
import com.boyoi.base.service.DeptService;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.CommonException;
import com.boyoi.core.entity.Dept;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.User;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.service.impl.BaseServiceImpl;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 描述：
 * 部门 业务实现类
 *
 * @author ZhouJL
 * @date 2018/12/20 15:33
 */
@Service
public class DeptServiceImpl extends BaseServiceImpl<DeptDao> implements DeptService {

    @Resource
    private DeptDao dao;

    @Resource
    private UserDao userDao;

    @Override
    public int add(Dept dept) {
        int count = dao.findByCheck(dept);
        if (count != 0) {
            throw new CommonException(ResultCode.DATA_ALREADY_EXISTED);
        }
        dept.setGuid(IdUtil.simpleUUID());
        dept.setCreateTime(new Date());
        dept.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
        dept.setOptTime(dept.getCreateTime());
        return dao.add(dept);
    }

    @Override
    public int addBatch(List<Dept> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(Dept dept) {
        Dept d = new Dept();
        d.setParentId(dept.getGuid());
        int count = dao.findByCheck(d);
        if (count > 0) {
            throw new CommonException(ResultCode.DEPT_DEL_PARENT);
        }
        User user = new User();
        user.setDeptId(dept.getGuid());
        count = userDao.findByCheck(user);
        if (count > 0) {
            throw new CommonException(ResultCode.DEPT_DEL_USER);
        }
        return dao.del(dept);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    public int update(Dept dept) {
        return dao.update(dept);
    }

    @Override
    public int updateByNotEmpty(Dept dept) {
        dept.setOptTime(new Date());
        dept.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
        return dao.updateByNotEmpty(dept);
    }

    @Override
    public Dept findById(Serializable id) {
        return dao.findById(id);
    }

    @Override
    public List<Dept> findAll() {
        return dao.findAll();
    }

    @Override
    public List<Tree<String>> findByGridRequest(EasyGridRequest gridRequest) {
        List<Dept> all = dao.findByGridRequest(gridRequest);
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();
        for (Dept dept : all) {
            TreeNode<String> treeNode = new TreeNode<>(dept.getGuid(), dept.getParentId() == null ? "" : dept.getParentId(), dept.getDeptName(), null);
            Map<String, Object> extra = new HashMap<>(4);
            extra.put("optTime", dept.getOptTime());
            extra.put("optPerson", dept.getOptPerson());
            extra.put("optTimeStr", dept.getOptTimeStr());
            extra.put("createTime", DateUtil.format(dept.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            treeNode.setExtra(extra);
            nodeList.add(treeNode);
        }
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("guid");
        treeNodeConfig.setParentIdKey("parentId");
        treeNodeConfig.setNameKey("deptName");
        //转换器
        return TreeUtil.build(nodeList, "", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getWeight());
                    tree.setName(treeNode.getName());
                    tree.putExtra("title", treeNode.getName());
                    tree.putExtra("key", treeNode.getId());
                    tree.putExtra("value", treeNode.getId());
                    tree.putExtra("optTime", treeNode.getExtra().get("optTime"));
                    tree.putExtra("optPerson", treeNode.getExtra().get("optPerson"));
                    tree.putExtra("optTimeStr", treeNode.getExtra().get("optTimeStr"));
                    tree.putExtra("createTime", treeNode.getExtra().get("createTime"));
                });
    }

    @Override
    public List<Map<String, Object>> findAllTest(Map<String, String> map) {
        List<Map<String, Object>> reslut = new ArrayList<>();
        reslut = dao.findAllTest(map);
        try {
            reslut = dao.findAllTest(map);
        } catch (BadSqlGrammarException exception) {
            System.out.println("sys" + exception.getMessage());
            if (exception.getMessage().contains("不存在")) {
                reslut = dao.findAllTest(map);
            } else {
                throw new CommonException(ResultCode.SYSTEM_INNER_ERROR);
            }
        }
        return reslut;
    }

}
