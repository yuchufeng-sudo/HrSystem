package com.ys.hr.domain.excel;

import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * EMPLOYEE Object hr_employees
 *
 * @author ys
 * @date 2025-05-21
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrEmployeesExcel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name="Full Name")
    private String fullName;

    @Excel(name = "Hire Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hireDate;

    @Excel(name = "Position")
    private String position;

    @Excel(name = "Email")
    private String email;

    @Excel(name = "Phone Code")
    private String phoneCode;

    @Excel(name = "Phone Number")
    private String contactPhone;

    @Excel(name = "Emergency Contact")
    private String emergencyContact;

    @Excel(name = "Date Of Birth", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Excel(name = "Work Type", readConverterExp = "1=Full-time,2=Part-time,3=Internship")
    private String employmentType;

    @Excel(name = "Account Level")
    private String accountLevel;

    @Excel(name = "Account Status", readConverterExp = "1=Active Now,2=Resigned")
    private String status;

    /** Basic Salary */
    @Excel(name = "Basic Salary")
    private Long baseSalary;

    @Excel(name = "Account Holder Name")
    private String bankName;

    /** Bank Account Number */
    @Excel(name = "Bank Account Number")
    private String bankNumber;

    /** Bank of Deposit */
    @Excel(name = "Bank of Deposit")
    private String openingBank;

    /** Social Security Cost */
    @Excel(name = "Social Security Cost")
    private Double socialSecurityCost;

    /** Commercial Insurance Cost */
    @Excel(name = "Commercial Insurance Cost")
    private Double comInsuIdCost;

    /** Housing Fund Cost */
    @Excel(name = "Housing Fund Cost")
    private Double accuFundIdCost;
}
