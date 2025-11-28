package com.ys.hr.enums;

import lombok.Getter;

/**
 * @description: Fund Type Enum
 * @author: xz_Frank
 * @date: 2025/11/27
 */
@Getter
public enum FundType {
    APRA("1"),
    SMSF("2");
    FundType(String code) {
        this.code = code;
    }

    private final String code;

    public boolean equalsCode(String code) {
        return this.code.equals(code);
    }
}
