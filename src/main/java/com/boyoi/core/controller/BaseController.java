package com.boyoi.core.controller;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Controller层的基类
 *
 * @author ZhouJL
 * @date 2020/04/22 11:04
 */
public abstract class BaseController<T extends BaseEntity, S extends BaseService> {

    //自动注入request,继承本类的子类就可以直接使用
    @Autowired
    protected HttpServletRequest request;

    //自动注入session,继承本类的子类就可以直接使用
    @Autowired
    protected HttpSession session;


}
