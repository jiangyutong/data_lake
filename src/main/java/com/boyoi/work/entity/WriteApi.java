package com.boyoi.work.entity;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.core.entity.TableDefinition;
import com.boyoi.core.enums.TableType;
import com.boyoi.core.groups.Groups;
import com.boyoi.work.groups.WriteApiGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * 描述：
 * 查询接口 实体类
 *
 * @author fuwp
 * @date 2020-10-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WriteApi extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Guid
     */
    @NotBlank(groups = {WriteApiGroup.Update.class, WriteApiGroup.UpdateStatus.class, WriteApiGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 接口地址
     */
    @NotBlank(groups = {WriteApiGroup.Add.class, WriteApiGroup.Update.class})
    @Size(max = 255)
    private String apiUrl;

    /**
     * 操作人
     */
    @Size(max = 32)
    private String optPerson;

    /**
     * 接口名称
     */
    @NotBlank(groups = {WriteApiGroup.Add.class, WriteApiGroup.Update.class})
    @Size(max = 255)
    private String apiName;

    /**
     * 状态
     */
    @Size(max = 1)
    @NotBlank(groups = {WriteApiGroup.UpdateStatus.class})
    private String state;

    /**
     * 表名
     */
    @NotBlank(groups = {WriteApiGroup.Add.class, WriteApiGroup.Update.class})
    @Size(max = 255)
    private String tableName;

    /**
     * 表类型
     */
    @NotNull(groups = {WriteApiGroup.Add.class, WriteApiGroup.Update.class})
    @Size(max = 10)
    private TableType tableType;
    private String createUser;
    private TableDefinition tableDefinition;
    private String tableState;
}
