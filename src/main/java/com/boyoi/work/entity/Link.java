package com.boyoi.work.entity;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.work.groups.LinkGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 描述：
 * 数据链接 实体类
 *
 * @author Harvey ylq
 * @date 2020-09-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Link extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * guid
     */
    @NotBlank(groups = {LinkGroup.Update.class, LinkGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 链接地址
     */
    @NotBlank(groups = {LinkGroup.Add.class, LinkGroup.Update.class})
    @Size(max = 255)
    private String url;

    /**
     * 用户名
     */
    @NotBlank(groups = {LinkGroup.Add.class, LinkGroup.Update.class})
    @Size(max = 255)
    private String username;

    /**
     * 密码
     */
    @NotBlank(groups = {LinkGroup.Add.class, LinkGroup.Update.class})
    @Size(max = 100)
    private String password;

    /**
     * 类型
     */
    @NotBlank(groups = {LinkGroup.Add.class, LinkGroup.Update.class})
    @Size(max = 1)
    private String type;

    /**
     * 驱动
     */
    @NotBlank(groups = {LinkGroup.Update.class})
    @Size(max = 255)
    private String drive;

    /**
     * null
     */
    @Size(max = 32)
    private String optPerson;

    /**
     * null
     */
    @Size(max = 22)
    private Date optTime;

    /**
     * null
     */
    @Size(max = 22)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;

    /**
     * 链接别名
     */
    @NotBlank(groups = {LinkGroup.Add.class, LinkGroup.Update.class})
    @Size(max = 255)
    private String linkName;

    /**
     * 是否删除 A:未删除  B:已删除
     */
    private String isDeleted;
}
