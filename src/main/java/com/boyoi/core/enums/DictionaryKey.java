package com.boyoi.core.enums;

/**
 * 数据词典
 *
 * @author ShuQ
 */
public enum DictionaryKey {
    /* 成功状态码 */
    PURCHASE_BALANCE_DEPOSIT("purchase_balance_deposit", "采购预存款余额"),
    SETTLE_PRICE_INCREASE("settle_price_increase", "结算价格加价");

    private String code;

    private String message;

    DictionaryKey(String code, String message) {
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
