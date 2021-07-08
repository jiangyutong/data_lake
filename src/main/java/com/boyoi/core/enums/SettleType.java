package com.boyoi.core.enums;

/**
 * 结算方式
 *
 * @author ShuQ
 */
public enum SettleType {
    /* 成功状态码 */
    A("A", "预付款扣除");

    private String code;

    private String message;

    SettleType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
