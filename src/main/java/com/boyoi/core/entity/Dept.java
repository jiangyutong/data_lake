package com.boyoi.core.entity;

import com.boyoi.core.groups.DeptGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 描述：
 * 部门 实体类
 *
 * @author ZhouJL
 * @date 2020-05-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Dept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Guid
     */
    @NotBlank(groups = {DeptGroup.Update.class, DeptGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 部门名称
     */
    @NotBlank(groups = {DeptGroup.Add.class, DeptGroup.Update.class})
    @Size(max = 32)
    private String deptName;

    /**
     * 上级部门ID
     */
    @Size(max = 32)
    private String parentId;

    /**
     * 子级部门
     */
    private List<Dept> children;

}
