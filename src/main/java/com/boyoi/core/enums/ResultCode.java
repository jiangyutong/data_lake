package com.boyoi.core.enums;


/**
 * 返回值枚举
 *
 * @author ZhouJL
 * @date 2018/12/26 11:26
 */
public enum ResultCode {
    /**
     * 系统异常
     */
    FAILURE(-1, "服务器异常，请重试"),

    /* 成功状态码 */
    SUCCESS(0, "成功"),

    /* 参数错误：10000-19999 */
    PARAM_ERROR(10000, "参数错误"),
    PARAM_IS_BLANK(10001, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10002, "参数类型错误"),
    PARAM_NOT_COMPLETE(10003, "参数缺失"),
    PARAM_CHECK_ERROR(10004, "参数校验失败"),

    /* 用户错误：20000-29999*/
    USER_NOT_LOGGED_IN(20000, "用户未登录"),
    USER_PASSWORD_ERROR(20001, "密码错误"),
    USER_ACCOUNT_FORBIDDEN(20002, "账号已被禁用"),
    USER_NOT_EXIST(20003, "账号不存在"),
    USER_LOGIN_ERROR(20004, "登录失败"),
    USER_LOGIN_EXPIRED(20005, "登录过期，请重新登录"),
    USER_LOGIN_FAILURE_MAX(20008, "连续输入错误超过5次，锁定5分钟"),
    USER_LOGIN_CHECK_FAILURE(20009, "服务端校验失败"),

    /* 业务错误：30000-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30000, "业务出错"),
    ORDER_HAS_BEEN_BEGIN(30001, "订单已经开始加工"),
    HAVE_SUBCATEGORY(30002, "存在子分类，不能删除"),
    TELEPHONE_HAVE(30003, "电话号码已使用"),
    SPECIFIED_SMS_DATA_NOT_CHECK(30004, "发送太快了，请等一分钟在发送哦！"),
    SPECIFIED_SMS_DATA_NOT_ERROR(30005, "网络错误，请等会在发送哦！"),
    SPECIFIED_SMS_DATA_NOT_SEND(30006, "请发送验证码！"),
    SPECIFIED_SMS_ERROR(30007, "验证码错误！"),
    ID_CARD_HAVE(30008, "身份证号码已使用"),
    TRANS_COSTS_TEMPLATE_NAME_EXISTS(30009, "模板名已存在"),
    USER_LOGIN_AUTH_FAILURE(30010, "实名认证校验失败"),
    CAROUSEL_SORT_EXISTS(30023, "排序值重复"),
    DEPT_DEL_PARENT(30024, "该部门有下级部门，无法删除！"),
    DEPT_DEL_USER(30026, "该部门有用户，无法删除！"),
    OCR_ERROR(30025, "自动识别失败，请自行填写！"),
    CUSTOM_NEWS_NOT_FULL(30027, "分销商银行卡为空,请完善!"),
    CUSTOM_BALANCE_NOT_ENOUGH(30028, "余额不足，不能提现"),


    /* 系统错误：40000-49999 */
    SYSTEM_INNER_ERROR(40000, "系统繁忙，请稍后重试"),

    /* 数据错误：50000-599999 */
    RESULE_DATA_NONE(50000, "数据未找到"),
    DATA_IS_WRONG(50001, "数据有误"),
    DATA_ALREADY_EXISTED(50002, "数据已存在"),
    DATA_IS_MAX(50003, "文件过大"),
    DATA_FILE_BLANK(50004, "文件未找到"),
    DATA_FILE_NULL(50005, "文件内容为空"),
    DATA_FOLDER_NULL(50006, "文件目录为空"),
    DATA_IS_ALREADY_EXISTED(50007, "数据已被使用，无法删除。"),
    DATA_IS_DELETED(50008, "商品已经删除，无法添加"),
    DATA_LINK_FAILURE(50009, "数据库无法链接"),

    /* 接口错误：60000-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_NOT_FOUNT(60004, "未找到接口"),
    INTERFACE_TYPE_ERROR(60005, "接口请求类型错误"),

    /* 权限错误：70000-79999 */
    PERMISSION_NO_ACCESS(70000, "无访问权限"),
    PERMISSION_REVIEW_AUTHORITY(70001, "审核权限不足"),
    /*数据库异常：80000-89999*/
    CONNECTION_ERROR(80000, "获取连接失败"),
    COPY_TABLE_NOT_EXIST(80001, "拷贝数据表不存在"),
    TABLE_NOT_EXIST(80002, "数据表不存在"),
    TABLE_EXIST(80003, "数据表已存在"),
    URL_EXIST(80004, "该接口地址已存在"),
    JSON_NOT_PK(80005, "json类型字段不能作为主键"),
    TYPE_CONVERSION_ERR(80006, "不支持的字段类型转换"),
    API_URL_NULL(80007, "接口地址不存在"),
    API_DEACTIVATE(80008, "接口已停用"),
    DATA_LIST_NULL(80009, "数据集合不能为空"),
    WRITE_TASK_ERR(80010, "写入任务失败"),
    WRITE_DB_ERR(80011, "写入数据库失败"),
    QUERY_DB_ERR(80012, "查询数据库失败"),
    RESULT_NOT_EXIST(80013, "查询缓存结果已被清除"),
    QUERY_LOG_NOT_EXIST(80014, "无查询结果"),
    CONNECTION_DRIVER_ERROR(80015, "获取驱动程序错误，查看链接地址与类型是否一致"),
    TABLE_NUM_ERROR(80016, "表存在数据,无法修改表结构"),
    TEMPLATE_MAPPER_NOT_EXIST(80017, "模板映射表不存在"),
    TEMPLATE_DATA_TABLE_NOT_EXIST(800178, "模板映射表不存在");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
