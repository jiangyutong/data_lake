package com.boyoi.work.entity;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.work.groups.FieldMapperInfoGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 模板映射实体
 *
 * @author LiYucheng
 * @date 2021-4-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FieldMapperInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * guid
     */
    @NotBlank(groups = {FieldMapperInfoGroup.Update.class, FieldMapperInfoGroup.Del.class})
    @Size(max = 32)
    private String guid;


    @NotBlank(groups = {FieldMapperInfoGroup.Add.class, FieldMapperInfoGroup.Update.class})
    @Size(max = 255)
    private String originalTableName;

    /**
     * 该表的拥有者
     */
    @NotBlank(groups = {FieldMapperInfoGroup.Add.class, FieldMapperInfoGroup.Update.class})
    @Size(max = 255)
    private String tableOwner;

    /**
     * 本地建表来源 D：数据库；J：Json
     */
    @NotBlank(groups = {FieldMapperInfoGroup.Add.class, FieldMapperInfoGroup.Update.class})
    @Size(max = 1)
    private String tableType;

    /**
     * 模板表字段信息
     */
    @NotBlank(groups = {FieldMapperInfoGroup.Add.class, FieldMapperInfoGroup.Update.class})
    @Size(max = 2048)
    private Map<String, String> fieldMapper;
}
