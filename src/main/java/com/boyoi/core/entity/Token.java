package com.boyoi.core.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录态
 *
 * @author ZhouJL
 * @date 2020/5/20 13:44
 */
@Data
public class Token implements Serializable {

    private static final long serialVersionUID = -8714643469648581682L;

    /**
     * token
     */
    private String token;
}
