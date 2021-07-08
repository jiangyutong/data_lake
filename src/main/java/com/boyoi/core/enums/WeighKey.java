package com.boyoi.core.enums;

/**
 * 记重方式
 *
 * @author ShuQ
 */
public enum WeighKey {
    /* 成功状态码 */
    A("A", "整车称重"),
    B("B", "逐头称重");

    private String code;

    private String message;

    WeighKey(String code, String message) {
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
