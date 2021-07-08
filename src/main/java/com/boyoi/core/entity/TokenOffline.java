package com.boyoi.core.entity;

import java.io.Serializable;

/**
 * 只允许一个用户登录 实体对象
 * 只有一个标记作用
 *
 * @author Chenj on 2018/11/20
 */
public class TokenOffline implements Serializable {

    private boolean offline = true;

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }
}
