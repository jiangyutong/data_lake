package com.boyoi.base.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.boyoi.base.dao.DeptDao;
import com.boyoi.base.dao.PermissionDao;
import com.boyoi.base.dao.RoleDao;
import com.boyoi.base.dao.UserDao;
import com.boyoi.base.service.LoginLogService;
import com.boyoi.base.service.UserService;
import com.boyoi.base.tools.redis.util.RedisUtils;
import com.boyoi.core.entity.*;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.service.impl.BaseServiceImpl;
import com.boyoi.core.util.AddressUtils;
import com.boyoi.core.util.IpUtils;
import com.boyoi.core.util.ServletUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 * 用户 业务实现类
 *
 * @author ZhouJL
 * @date 2018/12/20 15:33
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<UserDao> implements UserService {

    @Resource
    private UserDao dao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private PermissionDao permissionDao;

    @Resource
    private DeptDao deptDao;

    @Resource
    private LoginLogService loginLogService;

    /**
     * token过期时间
     */
    @Value("${token.expires}")
    private Long TOKEN_EXPIRES_IN;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(User user, List<String> roleIds, List<String> otherCheckPermissions, List<String> supplierIds) {
        User checkUser = new User();
        checkUser.setTelephone(user.getTelephone());
        if (dao.findByCheck(checkUser) > 0) {
            throw new CommonException(ResultCode.DATA_ALREADY_EXISTED);
        }
        user.setGuid(IdUtil.simpleUUID());
        user.setPassword(SecureUtil.sha1("abc123456"));
        user.setPhoto("/default.jpg");
        user.setRemark("这个人很懒，什么都没有留下~");
        user.setIsEnable(0);
        user.setIsDeleted(0);
        user.setCreateTime(new Date());
        user.setOptTime(user.getCreateTime());
        user.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
        user.setDeptId(null);
//        if (user.getType() == 0) {
//            user.setSupplierId(null);
//        } else {
//        }

        List<Map<String, String>> role = new ArrayList<>();
        for (String id : roleIds) {
            Map<String, String> map = new HashMap<>(3);
            map.put("guid", IdUtil.simpleUUID());
            map.put("userId", user.getGuid());
            map.put("roleId", id);
            role.add(map);
        }
        List<Map<String, String>> permission = new ArrayList<>();

        for (String id : otherCheckPermissions) {
            Map<String, String> map = new HashMap<>(3);
            map.put("guid", IdUtil.simpleUUID());
            map.put("userId", user.getGuid());
            map.put("permissionId", id);
            permission.add(map);
        }

//        List<Map<String, String>> supplier = new ArrayList<>();
//
//        for (String id : supplierIds) {
//            Map<String, String> map = new HashMap<>(3);
//            map.put("guid", IdUtil.simpleUUID());
//            map.put("userId", user.getGuid());
//            map.put("supplierId", id);
//            supplier.add(map);
//        }

        dao.add(user);
        if (role.size() > 0) {
            dao.addRoleToUser(role);
        }
        if (permission.size() > 0) {
            dao.addPermissionToUser(permission);
        }
//        if (user.getType() == 0 && supplier.size() > 0) {
//            dao.addSupplierToUser(supplier);
//        }

    }

    @Override
    public int addBatch(List<User> list) {
        return dao.addBatch(list);
    }

    @Override
    public int del(User user) {
        return dao.del(user);
    }

    @Override
    public int delBatch(List<Serializable> ids) {
        return dao.delBatch(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user, List<String> roleIds, List<String> otherCheckPermissions, List<String> supplierIds) {
        User checkUser = new User();
        checkUser.setTelephone(user.getTelephone());
        checkUser.setGuid(user.getGuid());
        if (dao.findByCheck(checkUser) > 0) {
            throw new CommonException(ResultCode.DATA_ALREADY_EXISTED);
        }
        user.setOptTime(new Date());
        user.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
        user.setDeptId(user.getDeptId());
//        if (user.getType() == 0) {
//            user.setSupplierId(null);
//        } else {
//        }
        List<Map<String, String>> role = new ArrayList<>();
        for (String id : roleIds) {
            Map<String, String> map = new HashMap<>(3);
            map.put("guid", IdUtil.simpleUUID());
            map.put("userId", user.getGuid());
            map.put("roleId", id);
            role.add(map);
        }
        List<Map<String, String>> permission = new ArrayList<>();

        for (String id : otherCheckPermissions) {
            Map<String, String> map = new HashMap<>(3);
            map.put("guid", IdUtil.simpleUUID());
            map.put("userId", user.getGuid());
            map.put("permissionId", id);
            permission.add(map);
        }

        dao.updateByNotEmpty(user);
        dao.delRoleToUser(user.getGuid());
        dao.delPermissionToUser(user.getGuid());

//        List<Map<String, String>> supplier = new ArrayList<>();
//
//        for (String id : supplierIds) {
//            Map<String, String> map = new HashMap<>(3);
//            map.put("guid", IdUtil.simpleUUID());
//            map.put("userId", user.getGuid());
//            map.put("supplierId", id);
//            supplier.add(map);
//        }
//        dao.delSupplierToUser(user.getGuid());

        if (role.size() > 0) {
            dao.addRoleToUser(role);
        }
        if (permission.size() > 0) {
            dao.addPermissionToUser(permission);
        }
//        if (user.getType() == 0 && supplier.size() > 0) {
//            dao.addSupplierToUser(supplier);
//        }
    }

    @Override
    public int updateByNotEmpty(User user) {
        user.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
        user.setOptTime(new Date());
        return dao.updateByNotEmpty(user);
    }

    @Override
    public int resetPassword(User user) {
        user.setPassword(SecureUtil.sha1("abc123456"));
        user.setOptPerson(RedisUtils.getLoginUser().get("userName").toString());
        user.setOptTime(new Date());
        return dao.updateByNotEmpty(user);
    }

    @Override
    public User findById(Serializable id) {
        User user = dao.findById(id);
        List<Role> roles = roleDao.findByUser(user.getGuid());
        List<Permission> all = new ArrayList<>();
        for (Role role : roles) {
            role.setPermissionList(permissionDao.findByRole(role.getGuid()));
            List<Permission> list = permissionDao.findToRouter(role.getGuid());
            all.addAll(permissionDao.findToRouter(role.getGuid()));
            List<Permission> parent = new ArrayList<>();
            for (Permission p : list) {
                if (StrUtil.isEmpty(p.getParentId())) {
                    parent.add(p);
                }
            }
            RoleServiceImpl.permissions(list, parent);
            role.setRouters(parent);
        }
        user.setRoles(roles);
        user.setPermissions(permissionDao.findByUser(user.getGuid()));
        user.setDept(deptDao.findById(user.getDeptId()));
        all.addAll(user.getPermissions());
        Set<Permission> personSet = new TreeSet<>(Comparator.comparing(Permission::getGuid));
        personSet.addAll(all);
        all = new ArrayList<>(personSet);
        List<Permission> parent = new ArrayList<>();
        for (Permission p : all) {
            if (StrUtil.isEmpty(p.getParentId())) {
                parent.add(p);
            }
        }
        RoleServiceImpl.permissions(all, parent);
        user.setRouters(parent);
        user.setPassword(null);
        return user;
    }

    @Override
    public List<User> findAll() {
        return dao.findAll();
    }

    @Override
    public PageInfo<User> findByGridRequest(EasyGridRequest gridRequest) {
        gridRequest.getMap2().put("type", "0");
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
    }

    @Override
    public PageInfo<User> findByGridRequest2(EasyGridRequest gridRequest) {
        gridRequest.getMap2().put("type", "0");
        gridRequest.getMap2().put("isEnable", "0");
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
    }

    @Override
    public PageInfo<User> findByGridRequest3(EasyGridRequest gridRequest) {
        gridRequest.getMap2().put("type", "1");
        return PageHelper
                //分页
                .startPage(gridRequest.getPage(), gridRequest.getRows())
                //排序
                .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
                //数据
                .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
    }

    @Override
    public Token login(User user) {
        HttpServletRequest request = ServletUtils.getRequest();
        String ip = IpUtils.getHostIp();
        LoginLog loginLog = new LoginLog();
        loginLog.setGuid(IdUtil.simpleUUID());

        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象

        loginLog.setIpaddr(ip);
        loginLog.setLoginLocation(AddressUtils.getRealAddressByIp(ip));
        loginLog.setBrowser(browser);
        loginLog.setOs(os);
        loginLog.setMsg("登录成功");
        loginLog.setStatus("0");
        loginLog.setLoginTime(new Date());
        //异步保存登录日志

        User u = dao.login(user.getTelephone());

        if (u == null) {
            loginLog.setMsg(ResultCode.USER_NOT_EXIST.getMessage());
            loginLog.setStatus("1");
            loginLog.setLoginName(user.getTelephone());
            loginLogService.add(loginLog);
            throw new CommonException(ResultCode.USER_NOT_EXIST);
        }
        loginLog.setLoginName(u.getUserName());
        if (1 == u.getIsEnable()) {
            loginLog.setMsg(ResultCode.USER_ACCOUNT_FORBIDDEN.getMessage());
            loginLog.setStatus("1");
            loginLogService.add(loginLog);
            throw new CommonException(ResultCode.USER_ACCOUNT_FORBIDDEN);
        }
        if (!u.getPassword().equals(SecureUtil.sha1(user.getPassword()))) {
            loginLog.setMsg(ResultCode.USER_PASSWORD_ERROR.getMessage());
            loginLog.setStatus("1");
            loginLogService.add(loginLog);
            throw new CommonException(ResultCode.USER_PASSWORD_ERROR);
        }
        u.setRoles(roleDao.findByUser(u.getGuid()));
        u.setDept(deptDao.findById(u.getDeptId()));
        u.setAllPermissions(permissionDao.findByUserId(u.getGuid()));
        u.setRoutersToPage(makeRouter(u.getAllPermissions()));
        u.setPassword(null);
        loginLogService.add(loginLog);
        return makeToken(u);
    }

    @Override
    public int checkPassword(String password) {
        User user = dao.findById(RedisUtils.getLoginUser().get("guid").toString());
        if (SecureUtil.sha1(password).equals(user.getPassword())) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void updatePassword(String oldPassword, String password) {
        User user = dao.findById(RedisUtils.getLoginUser().get("guid").toString());
        if (!SecureUtil.sha1(oldPassword).equals(user.getPassword())) {
            throw new CommonException(ResultCode.USER_PASSWORD_ERROR);
        }
        user = new User();
        user.setGuid(RedisUtils.getLoginUser().get("guid").toString());
        user.setPassword(SecureUtil.sha1(password));
        dao.updateByNotEmpty(user);
    }

    @Override
    public void updateInfo(User user) {
        user.setGuid(RedisUtils.getLoginUser().get("guid").toString());
        dao.updateByNotEmpty(user);
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String token = request.getHeader("token");
        Object o = redisTemplate.opsForValue().get(token);
        String s = JSONUtil.toJsonStr(o);
        User u = JSONUtil.toBean(s, User.class);
        assert u != null;
        u.setUserName(user.getUserName());
        u.setTelephone(user.getTelephone());
        u.setGender(user.getGender());
        u.setRemark(user.getRemark());
        u.setPhoto(user.getPhoto());
        redisTemplate.opsForValue().set(token, user, TOKEN_EXPIRES_IN, TimeUnit.SECONDS);
    }

    private Token makeToken(User user) {
        Token token = new Token();
        // token
        token.setToken(UUID.randomUUID().toString());
        redisTemplate.opsForValue().set(token.getToken(), user, TOKEN_EXPIRES_IN, TimeUnit.SECONDS);

        log.info("用户名:{}, 生成token成功! IP:{}", user.getUserName(), IpUtils.getHostIp());
        return token;
    }

    /**
     * 组装路由
     *
     * @param list 权限集合
     * @return 路由列表
     */
    public static List<Map<String, Object>> makeRouter(List<Permission> list) {
        List<Map<String, Object>> routers = new ArrayList<>();
        //所有二级菜单
        List<Map<String, Object>> other = new ArrayList<>();
        //所有隐藏路由
        List<Map<String, Object>> hidden = new ArrayList<>();
        for (Permission p : list) {
            // 显示的菜单
            if ("0".equals(p.getType())) {
                Map<String, Object> map = new HashMap<>(7);
                //一级菜单
                if (p.getParentId() == null || "".equals(p.getParentId())) {
                    map.put("guid", p.getGuid());
                    map.put("key", p.getKey());
                    map.put("title", p.getName());
                    map.put("icon", p.getIcon());
//                    if (!"首页".equals(p.getName()) && !"牧场分布".equals(p.getName())) {
//                        map.put("component", "RouteView");
//                    }
                    for (Permission p_c : list) {
                        if (p.getGuid().equals(p_c.getParentId())) {
                            map.put("component", "RouteView");
                            break;
                        }
                    }
                    routers.add(map);
                } else { //二级
                    map.put("guid", p.getGuid());
                    map.put("parentId", p.getParentId());
                    map.put("key", p.getKey());
                    map.put("component", p.getKey());
                    map.put("title", p.getName());
                    other.add(map);
                }
            } else { //隐藏的路由页面 例如详情添加之类
                Map<String, Object> map = new HashMap<>(7);
                map.put("guid", p.getGuid());
                map.put("key", p.getKey());
                map.put("component", p.getKey());
                map.put("title", p.getName());
                map.put("parentId", p.getParentId());
                hidden.add(map);
            }
        }
        for (Map<String, Object> map : hidden) {
            for (Map<String, Object> map2 : routers) {
                if (map.get("parentId").equals(map2.get("guid"))) {
                    List<Map<String, Object>> children;
                    if (map2.get("children") == null) {
                        children = new ArrayList<>();
                        map.put("key", "");
                    } else {
                        children = (List<Map<String, Object>>) map2.get("children");
                    }
                    children.add(map);
                    map2.put("children", children);
                    map2.put("hideChildrenInMenu", true);
                    break;
                }
            }
            for (Map<String, Object> map2 : other) {
                if (map.get("parentId").equals(map2.get("guid"))) {
                    List<Map<String, Object>> children;
                    if (map2.get("children") == null) {
                        children = new ArrayList<>();
                        map.put("key", "");
                    } else {
                        children = (List<Map<String, Object>>) map2.get("children");
                    }
                    children.add(map);
                    map2.put("children", children);
                    map2.put("hideChildrenInMenu", true);
                    break;
                }
            }
        }
        for (Map<String, Object> map : other) {
            for (Map<String, Object> map2 : routers) {
                if (map.get("parentId").equals(map2.get("guid"))) {
                    List<Map<String, Object>> children;
                    if (map2.get("children") == null) {
                        children = new ArrayList<>();
                    } else {
                        children = (List<Map<String, Object>>) map2.get("children");
                    }
                    children.add(map);
                    map2.put("children", children);
                    break;
                }
            }
        }
        return routers;
    }
}
