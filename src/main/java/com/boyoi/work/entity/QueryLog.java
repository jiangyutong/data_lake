package com.boyoi.work.entity;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.work.groups.QueryLogGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 描述：
 * 数据写入日志 实体类
 *
 * @author fuwp
 * @date 2020-10-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    @NotBlank(groups = {QueryLogGroup.Update.class, QueryLogGroup.Del.class, QueryLogGroup.DownData.class})
    @Size(max = 32)
    private String guid;

    /**
     * 操作人
     */
    @NotBlank(groups = {QueryLogGroup.Add.class, QueryLogGroup.Update.class})
    @Size(max = 32)
    private String optPerson;

    /**
     * 创建人id
     */
    @NotBlank(groups = {QueryLogGroup.Add.class, QueryLogGroup.Update.class})
    @Size(max = 32)
    private String createUser;

    /**
     * 查询url
     */
    @NotBlank(groups = {QueryLogGroup.Add.class, QueryLogGroup.Update.class})
    @Size(max = 255)
    private String apiUrl;

    /**
     * 查询id
     */
    @Size(max = 32)
    private String apiId;

    /**
     * 参数
     */
    @NotBlank(groups = {QueryLogGroup.Add.class, QueryLogGroup.Update.class})
    private String parameter;

    /**
     * 状态 A：未查询 B正在查询 C查询完成 D查询异常
     */
    @NotBlank(groups = {QueryLogGroup.Add.class, QueryLogGroup.Update.class})
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
