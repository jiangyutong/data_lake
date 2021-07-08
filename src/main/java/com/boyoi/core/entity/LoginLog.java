package com.boyoi.core.entity;

import com.boyoi.core.groups.LoginLogGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 描述：
 * 系统访问记录 实体类
 *
 * @author ZhouJL
 * @date 2020-04-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginLog extends BaseEntity {

    private static final long serialVersionUID = 2870004642786282725L;

    /**
     * guid
     */
    @NotBlank(groups = {LoginLogGroup.Update.class, LoginLogGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 账号名称
     */
    @Size(max = 32)
    private String loginName;

    /**
     * 登录IP
     */
    @Size(max = 50)
    private String ipaddr;

    /**
     * 登录地点
     */
    @Size(max = 255)
    private String loginLocation;

    /**
     * 登录浏览器
     */
    @Size(max = 50)
    private String browser;

    /**
     * 登录操作系统
     */
    @Size(max = 50)
    private String os;

    /**
     * 登录状态  0：成功 1：失败
     */
    @Size(max = 1)
    private String status;

    /**
     * 提示消息
     */
    @Size(max = 255)
    private String msg;

    /**
     * 登录时间
     */
    @Size(max = 22)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;


}
