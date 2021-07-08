package com.boyoi.work.entity;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.work.groups.TemplateTableGroup;
import com.boyoi.work.groups.WriteApiGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 描述：
 * 模板表实体
 *
 * @author LiYucheng
 * @date 2021-4-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateTable extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Guid
     */
    @NotBlank(groups = {TemplateTableGroup.Update.class, TemplateTableGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 模板表名
     */
    @Getter
    @NotBlank(groups = {TemplateTableGroup.Add.class, TemplateTableGroup.Update.class})
    @Size(max = 255)
    private String templateName;


    /**
     * 模板表字段信息
     */
    @NotBlank(groups = {TemplateTableGroup.Add.class, TemplateTableGroup.Update.class})
    @Size(max = 2048)
    private String templateColumn;

    /**
     * null
     */
    @NotBlank(groups = {TemplateTableGroup.Add.class, TemplateTableGroup.Update.class})
    @Size(max = 22)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Size(max = 1)
    @NotBlank(groups = {TemplateTableGroup.Update.class})
    private char isDel;

    /**
     * null
     */
    private String optPerson;
    private String templateDescribe;
}
