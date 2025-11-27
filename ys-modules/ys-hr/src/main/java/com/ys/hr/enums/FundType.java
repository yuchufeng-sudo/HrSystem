package com.ys.hr.enums;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/11/27
 */
public enum FundType {
    APRA("1"),
    SMSF("2");
    FundType(String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }

    public boolean equalsCode(String code) {
        return this.code.equals(code);
    }
}
