package com.ys.hr.enums;

import lombok.Getter;

/**
 * @description: Employees Status Enum
 * @author: xz_Frank
 * @date: 2025/11/27
 */
@Getter
public enum EmployeeStatus {
    ACTIVE("1", "Active"),
    RESIGNED("2", "Resigned"),
    OFFBOARDING("3", "Offboarding");
    EmployeeStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private final String code;

    private final String description;

    public boolean equalsCode(String code) {
        return this.code.equals(code);
    }
}
