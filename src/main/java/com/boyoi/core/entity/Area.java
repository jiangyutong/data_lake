package com.boyoi.core.entity;

import com.boyoi.core.groups.AreaGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 * 区域 实体类
 *
 * @author Zhangyq
 * @date 2020-07-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Area extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 区域编号
     */
    @NotBlank(groups = {AreaGroup.Update.class, AreaGroup.Del.class})
    @Size(max = 16)
    private String areaId;

    /**
     * 区域名称
     */
    @NotBlank(groups = {AreaGroup.Add.class, AreaGroup.Update.class})
    @Size(max = 128)
    private String areaName;

    /**
     * 上级区域编号
     */
    @Size(max = 16)
    private String parentId;

    /**
     * 区域等级
     */
    @NotBlank(groups = {AreaGroup.Add.class, AreaGroup.Update.class})
    @Size(max = 16)
    private String levels;

    /**
     * 下属区域
     */
    private List<Area> childrens;


}