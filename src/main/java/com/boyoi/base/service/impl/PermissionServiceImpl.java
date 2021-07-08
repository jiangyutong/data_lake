package com.boyoi.base.service.impl;

import cn.hutool.core.util.IdUtil;
import com.boyoi.base.dao.PermissionDao;
import com.boyoi.base.service.PermissionService;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Permission;
import com.boyoi.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 描述：
 * 权限 业务实现类
 *
 * @author ZhouJL
 * @date 2018/12/20 15:33
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionDao> implements PermissionService {

    @Resource
    private PermissionDao dao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Permission permission, List<Map<String, String>> childrens) {
        permission.setGuid(IdUtil.simpleUUID());
        permission.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
        permission.setOptTime(new Date());
        permission.setCreateTime(permission.getOptTime());
        permission.setType("0");

        List<Permission> children = new ArrayList<>();
        for (Map<String, String> map : childrens) {
            Permission p = new Permission();
            p.setGuid(IdUtil.simpleUUID());
            p.setType("1");
            p.setCreateTime(permission.getOptTime());
            p.setOptTime(permission.getOptTime());
            p.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
            p.setParentId(permission.getGuid());
            p.setKey(map.get("childrenKey"));
            p.setName(map.get("childrenName"));
            p.setSort(Integer.parseInt(map.get("childrenSort")));
            children.add(p);
        }
        dao.add(permission);
        if (children.size() != 0) {
            dao.addBatch(children);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addBatch(List<Permission> list) {
        return dao.addBatch(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int del(Permission permission) {
        Permission p = new Permission();
        p.setParentId(permission.getGuid());
        p.setType("1");
        dao.del(p);
        return dao.del(permission);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Permission permission, List<Map<String, String>> childrens) {
        permission.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
        permission.setOptTime(new Date());
        permission.setCreateTime(permission.getOptTime());
        permission.setType("0");
        dao.update(permission);
        List<Permission> children = new ArrayList<>();
        for (Map<String, String> map : childrens) {
            Permission p = new Permission();
            p.setGuid(IdUtil.simpleUUID());
            p.setType("1");
            p.setCreateTime(permission.getOptTime());
            p.setOptTime(permission.getOptTime());
            p.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
            p.setParentId(permission.getGuid());
            p.setKey(map.get("childrenKey"));
            p.setName(map.get("childrenName"));
            p.setSort(Integer.parseInt(String.valueOf(map.get("childrenSort"))));
            children.add(p);
        }
        Permission p = new Permission();
        p.setType("1");
        p.setParentId(permission.getGuid());
        dao.del(p);
        if (children.size() != 0) {
            dao.addBatch(children);
        }
    }

    @Override
    public int updateByNotEmpty(Permission permission) {
        return dao.updateByNotEmpty(permission);
    }

    @Override
    public Permission findById(Serializable id) {
        return dao.findById(id);
    }

    @Override
    public List<Permission> findAll() {
        return dao.findAll();
    }

    @Override
    public List<Map<String, Object>> findByGridRequest(EasyGridRequest gridRequest) {
        List<Permission> list = dao.findByGridRequest(gridRequest);
        List<Map<String, Object>> parent = new ArrayList<>();
        for (Permission p : list) {
            if (p.getParentId() == null || "".equals(p.getParentId())) {
                Map<String, Object> map = new HashMap<>(12);
                map.put("guid", p.getGuid());
                map.put("parentId", p.getParentId());
                map.put("name", p.getName());
                map.put("key", p.getKey());
                map.put("sort", p.getSort());
                map.put("icon", p.getIcon());
                map.put("type", p.getType());
                map.put("instructions", p.getInstructions());
                map.put("optTime", p.getOptTime());
                map.put("createTime", p.getCreateTime());
                map.put("optTimeStr", p.getOptTimeStr());
                map.put("optPerson", p.getOptPerson());
                parent.add(map);
            }
        }
        for (Permission p : list) {
            if (p.getParentId() != null && !"".equals(p.getParentId())) {
                for (Map<String, Object> pa : parent) {
                    if (p.getParentId().equals(pa.get("guid"))) {
                        List<Map<String, Object>> children = (List<Map<String, Object>>) pa.get("children");
                        if (children == null) {
                            children = new ArrayList<>();
                        }
                        Map<String, Object> map = new HashMap<>(12);
                        map.put("guid", p.getGuid());
                        map.put("parentId", p.getParentId());
                        map.put("name", p.getName());
                        map.put("key", p.getKey());
                        map.put("sort", p.getSort());
                        map.put("icon", p.getIcon());
                        map.put("type", p.getType());
                        map.put("instructions", p.getInstructions());
                        map.put("optTime", p.getOptTime());
                        map.put("createTime", p.getCreateTime());
                        map.put("optTimeStr", p.getOptTimeStr());
                        map.put("optPerson", p.getOptPerson());
                        children.add(map);
                        pa.put("children", children);
                    }
                }

            }
        }
        return parent;
    }

    @Override
    public List<Permission> findByRole(String roleId) {
        return dao.findByRole(roleId);
    }

    @Override
    public List<Permission> allParent() {
        return dao.allParent();
    }

//    @Override
//    public Map<String, Object> findPerByUser() {
//        String userId = WebUtils.getLoginUser().getGuid();
//        List<UserMenuOther> userMenuList = userMenuDao.findMenuByUser(userId);
////        List<Permission> permissionList = dao.findPerByUser(userId);
//        Map<String, Object> resMap = new HashMap<>();
//        List<Permission> permissionList1 = dao.findByRoleId(WebUtils.getLoginUser().getRoleId());
//        List<Permission> permissionList = new ArrayList<>();
//        for(Permission permission:permissionList1){
//            if(permission.getType().equals("0")){
//                if(permission.getParentId()!=null&&!permission.getParentId().equals("")){
//                    permissionList.add(permission);
//                }
//            }
//        }
//        Iterator<Permission> iterator = permissionList.iterator();
//        while (iterator.hasNext()){
//            Permission permission = iterator.next();
//            for (UserMenuOther userMenu: userMenuList){
//                if (permission.getGuid().equals(userMenu.getPermissionId())){
//                    iterator.remove();
//                    continue;
//                }
//            }
//        }
//        resMap.put("details", permissionList);
//        return resMap;
//    }
}
