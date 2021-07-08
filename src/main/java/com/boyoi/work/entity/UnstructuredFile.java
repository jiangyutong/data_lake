package com.boyoi.work.entity;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.work.groups.UnstructuredFileGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 描述：
 * 文件上传存储信息表 实体类
 *
 * @author Xiaopeng
 * @date 2020-10-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UnstructuredFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据的id
     */
    @NotBlank(groups = {UnstructuredFileGroup.Update.class, UnstructuredFileGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 文件的大小
     */
    @NotBlank(groups = {UnstructuredFileGroup.Add.class, UnstructuredFileGroup.Update.class})
    @Size(max = 16)
    private int fsize;

    /**
     * 存储的位置
     */
    @NotBlank(groups = {UnstructuredFileGroup.Add.class, UnstructuredFileGroup.Update.class})
    @Size(max = 1024)
    private String faddress;

    /**
     * 数据所属类型：T:图片；Y：音频；S：视频；W:文档
     */
    @NotBlank(groups = {UnstructuredFileGroup.Add.class, UnstructuredFileGroup.Update.class})
    @Size(max = 255)
    private String ftype;

    /**
     * 发送时间
     */
    @Size(max = 22)
    private Date time;

    /**
     * 搜索些关键词(1,2,3)
     */
    @NotBlank(groups = {UnstructuredFileGroup.Add.class, UnstructuredFileGroup.Update.class})
    @Size(max = 255)
    private String keywords;

    /**
     * 最近修改人
     */
    @NotBlank(groups = {UnstructuredFileGroup.Add.class, UnstructuredFileGroup.Update.class})
    @Size(max = 255)
    private String optPerson;

    /**
     * 最近修改时间
     */
    @NotBlank(groups = {UnstructuredFileGroup.Add.class, UnstructuredFileGroup.Update.class})
    @Size(max = 22)
    private Date optTime;

    /**
     * 创建时间
     */
    @NotBlank(groups = {UnstructuredFileGroup.Add.class, UnstructuredFileGroup.Update.class})
    @Size(max = 22)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * null
     */
    @Size(max = 2147483647)
    private String optTimeStr;

    /**
     * 文件的名字
     */
    @NotBlank(groups = {UnstructuredFileGroup.Add.class, UnstructuredFileGroup.Update.class})
    @Size(max = 255)
    private String fname;

    /**
     * 是否删除 A:未删除  B:已删除
     */
    private String isDeleted;

}
