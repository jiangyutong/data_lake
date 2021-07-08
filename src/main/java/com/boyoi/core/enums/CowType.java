package com.boyoi.core.enums;

/**
 * 记重方式
 *
 * @author ShuQ
 */
public enum CowType {
    /* 成功状态码 */
    MALE_YAK("0", "公牦牛"),
    FEMALE_YAK("1", "母牦牛"),
    CAS_MALE_YAK("2", "公犏牦牛"),
    CAS_FEMALE_YAK("3", "母犏牦牛"),
    OTHER("4", "其他");

    private String code;

    private String message;

    CowType(String code, String message) {
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
