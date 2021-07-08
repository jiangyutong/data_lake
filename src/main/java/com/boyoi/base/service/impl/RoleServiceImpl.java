package com.boyoi.base.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.boyoi.base.dao.PermissionDao;
import com.boyoi.base.dao.RoleDao;
import com.boyoi.base.service.RoleService;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.CommonException;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Permission;
import com.boyoi.core.entity.Role;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 描述：
 * 角色 业务实现类
 *
 * @author ZhouJL
 * @date 2018/12/20 15:33
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleDao> implements RoleService {

    @Resource
    private RoleDao dao;
    @Resource
    private PermissionDao permissionDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Role role, List<String> premissions) {
        Role r = new Role();
        r.setRoleName(role.getRoleName());
        int count = dao.findByCheck(r);
        if (count != 0) {
            throw new CommonException(ResultCode.DATA_ALREADY_EXISTED);
        }
        role.setGuid(IdUtil.simpleUUID());
        role.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
        role.setOptTime(new Date());
        role.setCreateTime(role.getOptTime());
        List<Map<String, String>> list = new ArrayList<>();
        for (String permission : premissions) {
            Map<String, String> map = new HashMap<>();
            map.put("guid", IdUtil.simpleUUID());
            map.put("roleId", role.getGuid());
            map.put("permissionId", permission);
            list.add(map);
        }
        dao.add(role);
        if (list.size() != 0) {
            dao.addPermission(list);
        }
    }

    @Override
    public int addBatch(List<Role> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(Role role) {
        return dao.del(role);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role role, List<String> premissions) {
        Role r = new Role();
        r.setGuid(role.getGuid());
        r.setRoleName(role.getRoleName());
        int count = dao.findByCheck(r);
        if (count != 0) {
            throw new CommonException(ResultCode.DATA_ALREADY_EXISTED);
        }
        role.setOptTime(new Date());
        role.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
        List<Map<String, String>> list = new ArrayList<>();
        for (String permission : premissions) {
            Map<String, String> map = new HashMap<>();
            map.put("guid", IdUtil.simpleUUID());
            map.put("roleId", role.getGuid());
            map.put("permissionId", permission);
            list.add(map);
        }
        dao.updateByNotEmpty(role);
        dao.delPermission(role.getGuid());
        if (list.size() != 0) {
            dao.addPermission(list);
        }
        dao.updateByNotEmpty(role);
    }

    @Override
    public int updateByNotEmpty(Role role) {
        return dao.updateByNotEmpty(role);
    }

    @Override
    public Role findById(Serializable id) {
        Role role = dao.findById(id);
        role.setPermissionList(permissionDao.findByRole(role.getGuid()));
        List<Permission> list = permissionDao.findToRouter(role.getGuid());
        List<Permission> parent = new ArrayList<>();
        for (Permission p : list) {
            if (StrUtil.isEmpty(p.getParentId())) {
                parent.add(p);
            }
        }
        permissions(list, parent);
        role.setRouters(parent);
        return role;
    }

    static void permissions(List<Permission> list, List<Permission> parent) {
        for (Permission p : list) {
            if (StrUtil.isNotEmpty(p.getParentId())) {
                for (Permission pa : parent) {
                    if (p.getParentId().equals(pa.getGuid())) {
                        List<Permission> children = pa.getChildren();
                        if (children == null) {
                            children = new ArrayList<>();
                        }
                        children.add(p);
                        pa.setChildren(children);
                    }
                }

            }
        }
    }

    @Override
    public List<Role> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<Role> findByGridRequest(EasyGridRequest gridRequest) {
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
    }
}
