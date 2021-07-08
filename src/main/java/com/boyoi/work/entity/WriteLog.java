package com.boyoi.work.entity;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.work.groups.WriteLogGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 描述：
 * 数据写入日志 实体类
 *
 * @author fuwp
 * @date 2020-10-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WriteLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(groups = {WriteLogGroup.Update.class, WriteLogGroup.DownData.class, WriteLogGroup.Del.class})
    @Size(max = 32)
    private String guid;
    /**
     * 创建人id
     */
    @NotBlank(groups = {WriteLogGroup.Add.class, WriteLogGroup.Update.class})
    @Size(max = 32)
    private String createUser;

    /**
     * 写入url
     */
    @NotBlank(groups = {WriteLogGroup.Add.class, WriteLogGroup.Update.class})
    @Size(max = 255)
    private String apiUrl;

    /**
     * 写入id
     */
    @Size(max = 32)
    private String apiId;

    /**
     * 写入文件路径
     */
    @Size(max = 255)
    private String dataPath;

    /**
     * 状态 A：未写入 B写入成功 C业务异常 D数据异常
     */
    @NotBlank(groups = {WriteLogGroup.Add.class, WriteLogGroup.Update.class})
    @Size(max = 1)
    private String state;

    /**
     * 异常信息
     */
    private String err;
    /**
     * 数据行数
     */
    private Integer line;
    private String apiName;
}
