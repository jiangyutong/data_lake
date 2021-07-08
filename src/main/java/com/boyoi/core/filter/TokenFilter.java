package com.boyoi.core.filter;

import com.boyoi.base.service.RoleService;
import com.boyoi.core.entity.Result;
import com.boyoi.core.entity.TokenOffline;
import com.boyoi.core.entity.User;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.util.JacksonUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限验证过滤器
 *
 * @author ZhouJL
 * @date 2020/5/21 15:44
 */
@WebFilter
@Component
@Slf4j
public class TokenFilter implements Filter {
    /**
     * 不需要经过拦截的url
     */
    private static final Set<String> noAuthUrls = new HashSet<>();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    RoleService roleService;

    @Override
    public void init(FilterConfig filterConfig) {
        noAuthUrls.addAll(Arrays.asList(
                "/user/login",
                "/user/getUser",
                "/user/utf8ToBase64",
                "/oauth/logout",
                "/oauth/getSession",
                "/oauth/userToken",
                "/smsSend/codeSend2",
                "/smsSend/checkCode",
                "/user/check",
                "/user/upload",
                "/pushAdd/sendDirectMessage",
                "/oauth/rToken", "/index.html", "/favicon.ico", "/*.js", "/static/**", "/", "/druid/**"));// 用户不需要token验证添加
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        // 判断没有权限的URI
        String requestURI = httpServletRequest.getServletPath();

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String url : noAuthUrls) {
            if (antPathMatcher.match(url, requestURI)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        String token = httpServletRequest.getHeader("token");

        if (!Strings.isNullOrEmpty(token)) {
            Object user = redisTemplate.opsForValue().get(token);
            if (user instanceof User) {
                User userInRedis = (User) user;
                //异步保存请求历史
                // 验证权限(暂时不验证权限)
//                if (roleService.checkHasAuth(userInRedis.getRoleId(), requestURI)) {
//                    // url在用户权限中存在。通过
//                    filterChain.doFilter(servletRequest, servletResponse);
//                    return;
//                }

                // 当前用户没有权限
//                log.debug(userInRedis.getUserName() + " 无权访问：" + requestURI);
//
//                servletResponse.setContentType("application/json;charset=utf-8");
//                servletResponse.getWriter().write(JacksonUtil.toJson(Result.failure(ResultCode.PERMISSION_NO_ACCESS)));
//                servletResponse.flushBuffer();
//                return;

                filterChain.doFilter(servletRequest, servletResponse);
                return;
            } else if (user instanceof TokenOffline) {
                log.debug("token: {} 已经下线", token);

                servletResponse.setContentType("application/json;charset=utf-8");
                servletResponse.getWriter().write(JacksonUtil.toJson(Result.failure(ResultCode.USER_LOGIN_EXPIRED)));
                servletResponse.flushBuffer();
                return;
            }
        }
        //访问地址，失败，或者没有登录，重定向登录页面
        httpServletResponse.sendRedirect("/");
        // filterChain.doFilter(servletRequest, servletResponse);
       /*    //@To 没有登录跳转至登录界面
        System.out.println(Result.failure(ResultCode.USER_NOT_LOGGED_IN)+"::"+requestURI);
        // 默认未登录
        servletResponse.setContentType("application/json;charset=utf-8");
        servletResponse.getWriter().write(JacksonUtil.toJson(Result.failure(ResultCode.USER_NOT_LOGGED_IN)));
        servletResponse.flushBuffer();*/

    }

    @Override
    public void destroy() {

    }
}
