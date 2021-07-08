package com.boyoi.core.entity;


import com.boyoi.core.enums.ResultCode;

/**
 * 通用异常处理类
 *
 * @author ZhouJL
 * @date 2018/12/26 14:40
 */
public class CommonException extends RuntimeException {

    private ResultCode resultCode;
    /**
     * 存放还回异常的data
     */

    private Object data;

    public CommonException(ResultCode resultCode) {
        super("错误dl：" + resultCode.getCode() + "，错误信息：" + resultCode.getMessage());
        this.resultCode = resultCode;
    }


    public ResultCode getResultCode() {
        return resultCode;
    }

    public Object getData() {
        return data;
    }

    public CommonException setData(Object data) {
        this.data = data;
        return this;
    }
}
