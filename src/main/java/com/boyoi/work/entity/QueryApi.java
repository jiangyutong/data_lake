package com.boyoi.work.entity;

import com.boyoi.core.entity.BaseEntity;
import com.boyoi.core.entity.IBoundSql;
import com.boyoi.work.groups.QueryApiGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.Map;

/**
 * 描述：
 * 查询接口 实体类
 *
 * @author fuwp
 * @date 2020-10-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryApi extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Guid
     */
    @NotBlank(groups = {QueryApiGroup.Update.class, QueryApiGroup.UpdateStatus.class, QueryApiGroup.Del.class})
    @Size(max = 32)
    private String guid;

    /**
     * 接口名称
     */
    @NotBlank(groups = {QueryApiGroup.Add.class, QueryApiGroup.Update.class})
    @Size(max = 255)
    private String apiUrl;

    /**
     * sql语句
     */
    @NotBlank(groups = {QueryApiGroup.Add.class, QueryApiGroup.Update.class})
    private String sqlText;

    /**
     * 接口名称
     */
    @NotBlank(groups = {QueryApiGroup.Add.class, QueryApiGroup.Update.class})
    @Size(max = 255)
    private String apiName;

    /**
     * 是否启用
     */
    @NotBlank(groups = {QueryApiGroup.UpdateStatus.class})
    @Size(max = 1)
    private String state;
    private String createUser;
    private IBoundSql iBoundSql;
    private Map<String, Object> parameter;
}
