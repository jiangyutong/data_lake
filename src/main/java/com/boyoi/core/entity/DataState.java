package com.boyoi.core.entity;

import cn.hutool.core.util.StrUtil;
import com.boyoi.core.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fuwp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataState {
    private Integer code;
    private String message;

    public DataState(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public DataState(ResultCode resultCode, String errMsg) {
        this.code = resultCode.getCode();
        if (StrUtil.isNotBlank(errMsg)) {
            int i = errMsg.lastIndexOf("ERROR:");
            if (i > -1) {
                errMsg = errMsg.substring(i);
            }
            this.message = resultCode.getMessage() + StrUtil.COMMA + errMsg;
        } else {
            this.message = resultCode.getMessage();
        }
    }
}
