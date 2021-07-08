package com.boyoi.work.entity;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.core.entity.TableDefinition;
import com.boyoi.core.enums.TableType;
import com.boyoi.core.groups.Groups;
import com.boyoi.work.groups.NewWriteApiGroup;
import com.boyoi.work.groups.WriteApiGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * 描述：
 * 新的查询接口 实体类
 *
 * @author lyc
 * @date 2021-5-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NewWriteApi extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Guid
     */
    @NotBlank(groups = {NewWriteApiGroup.Update.class, NewWriteApiGroup.UpdateStatus.class, NewWriteApiGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 接口地址
     */
    @NotBlank(groups = {NewWriteApiGroup.Add.class, NewWriteApiGroup.Update.class})
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
    @NotBlank(groups = {NewWriteApiGroup.Add.class, NewWriteApiGroup.Update.class})
    @Size(max = 255)
    private String apiName;

    /**
     * 状态
     */
    @Size(max = 1)
    @NotBlank(groups = {NewWriteApiGroup.UpdateStatus.class})
    private String state;

    /**
     * 表名
     */
    @NotBlank(groups = {NewWriteApiGroup.Add.class, NewWriteApiGroup.Update.class})
    @Size(max = 255)
    private String tableName;

    /**
     * 表类型
     */
    @NotBlank(groups = {NewWriteApiGroup.Add.class, NewWriteApiGroup.Update.class})
    @Size(max = 128)
    private TableType tableType;

    /**
     * 模板名
     */
    @NotNull(groups = {NewWriteApiGroup.Add.class, NewWriteApiGroup.Update.class})
    @Size(max = 255)
    private String templateName;

    /**
     * 模板guid
     */
    @NotNull(groups = {NewWriteApiGroup.Add.class, NewWriteApiGroup.Update.class})
    @Size(max = 32)
    private String templateGuid;

    /**
     * 数据表guid
     */
    @NotNull(groups = {NewWriteApiGroup.Add.class, NewWriteApiGroup.Update.class})
    @Size(max = 32)
    private String tableGuid;

    /**
     * 该模板表对应数据表的字段映射guid
     */
    @NotNull(groups = {NewWriteApiGroup.Add.class, NewWriteApiGroup.Update.class})
    @Size(max = 32)
    private String mapperGuid;
    private TableDefinition tableDefinition;
}
