package com.boyoi.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 描述：
 * 文件 实体类
 *
 * @Author ZhouJL
 * @Date 2019-04-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class File extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * guid
     */
    private String guid;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 操作人
     */
    private String optPerson;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 操作人
     */
    private String useType;

    /**
     * 顺序
     */
    private Integer fileSort;


}
