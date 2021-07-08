package com.boyoi.core.entity;

import com.boyoi.core.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回值统一格式
 *
 * @author ZhouJL
 * @date 2018/12/26 13:48
 */
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private Integer code;

    private String msg;

    private Object data;

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result failure() {
        Result result = new Result();
        result.setResultCode(ResultCode.SYSTEM_INNER_ERROR);
        return result;
    }

    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return result;
    }

    public static Result failure(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }


    private void setResultCode(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getMessage();
    }

}
