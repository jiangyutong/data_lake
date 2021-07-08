package com.boyoi.work.entity;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.work.groups.LinkTableGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 描述：
 * 已复制到本地表信息 实体类
 *
 * @author Xiaopeng
 * @date 2020-09-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LinkTable extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Guid
     */
    @NotBlank(groups = {LinkTableGroup.Update.class, LinkTableGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 源表名
     */
    @NotBlank(groups = {LinkTableGroup.Add.class, LinkTableGroup.Update.class})
    @Size(max = 255)
    private String tableName;

    /**
     * 复制到本地表名称
     */
    @NotBlank(groups = {LinkTableGroup.Add.class, LinkTableGroup.Update.class})
    @Size(max = 255)
    private String copyTableName;

    /**
     * 链接表ID
     */
    @NotBlank(groups = {LinkTableGroup.Add.class, LinkTableGroup.Update.class})
    @Size(max = 32)
    private String linkId;

    /**
     * 本地建表来源 D：数据库；F：文件
     */
    @NotBlank(groups = {LinkTableGroup.Add.class, LinkTableGroup.Update.class})
    @Size(max = 1)
    private String type;

    /**
     * 表字段信息
     */
    @NotBlank(groups = {LinkTableGroup.Add.class, LinkTableGroup.Update.class})
    @Size(max = 2048)
    private String tableColumn;

    /**
     * null
     */
    @NotBlank(groups = {LinkTableGroup.Add.class, LinkTableGroup.Update.class})
    @Size(max = 22)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * null
     */
    private String optPerson;
    private String tableComment;
}
