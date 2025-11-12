package com.ys.hr.domain;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ABAFileRequest {
    @Size(max = 26, message = "Account name cannot exceed 26 characters")
    private String accoutName;

    @Size(max = 9, message = "Account number cannot exceed 9 digits")
    private String accoutNumber;

    @Size(min = 0, max = 6, message = "APCA number must be 6 digits")
    private String apca;

    @NotBlank(message = "Bank code cannot be blank")
    private String bankCode; // Text format such as "CBA", "ANZ"

    @NotBlank(message = "BSB cannot be blank")
    @Pattern(regexp = "\\d{3}-\\d{3}", message = "BSB format should be XXX-XXX")
    private String bsb;

    @NotBlank(message = "Date cannot be blank")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date format should be YYYY-MM-DD")
    private String date;

    @NotBlank(message = "Default text cannot be blank")
    @Size(max = 18, message = "Default text cannot exceed 18 characters")
    private String defaultText;

    @NotNull(message = "Employee list cannot be null")
    @Size(min = 1, message = "At least one employee record is required")
    private List<EmployeeRecord> list;

    @Data
    public static class EmployeeRecord {
        @NotBlank(message = "Employee full name cannot be blank")
        private String fullName;

        @NotBlank(message = "Bank account number cannot be blank")
        @Size(max = 9, message = "Bank account number cannot exceed 9 digits")
        private String bankNumber;

        @NotNull(message = "Payment amount cannot be null")
        @DecimalMin(value = "0.01", message = "Payment amount must be greater than 0")
        private BigDecimal afterTaxSalary;

        // Optional field
        private String bsb; // Employee-specific BSB (optional)
    }
}
