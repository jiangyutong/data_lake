package com.boyoi.base.tools.log.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：
 * 操作日志 实体类
 *
 * @author ZhouJL
 * @date 2020-04-28
 */
@Data
public class OptLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Guid
     */
    private String guid;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    private int businessType;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 请求地址
     */
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    private int operatorType;

    /**
     * 操作人员
     */
    private String operName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 请求URL
     */
    private String operUrl;

    /**
     * 主机地址
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 操作状态（0正常 1异常）
     */
    private int status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private Date operTime;


}
