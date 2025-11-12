package com.ys.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
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
public class HrEmployees extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** EMPLOYEE ID */
    @TableId(value = "employee_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long employeeId;
    /** EMPLOYEE FULL NAME */
    @NotNull(message = "Full name cannot be empty")
    @Size(max = 100, message = "Full name length cannot exceed 100 characters")
    @Excel(name="Full Name")
    private String fullName;
    /** HIRE DATE */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Hire Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hireDate;
    /** JOB TITLE */
    @Excel(name = "Position")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long position;
    /** LOGIN ACCOUNT NAME */
    private String username;
    /** LOGIN ACCOUNT NAME */
    private String firstName;
    /** LOGIN ACCOUNT NAME */
    private String lastName;
    /** Gender */
    private String gender;
    /** EMAIL (EMAIL ADDRESS) */
    private String email;
    /** AVATAR URL */
    private String avatarUrl;
    /** ACCOUNT STATUS */
    private String status;
    /** DATE OF BIRTH */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Date Of Birth", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateOfBirth;
    /** Phone Code */
    @Excel(name = "Phone Code")
    @NotNull(message = "Phone Code cannot be empty")
    @Size(max = 20, message = "Phone Code length cannot exceed 20 characters")
    private String phoneCode;
    /** Phone Number */
    @Excel(name = "Phone Number")
    @NotNull(message = "Phone Number cannot be empty")
    private String contactPhone;
    /** EMERGENCY CONTACT NUMBER */
    @Excel(name = "Emergency Contact Name")
    private String emergencyContactName;
    @Excel(name = "Emergency Contact Number")
    private String emergencyContact;
    /** REGION */
    private String country;
    private String address1;
    private String address2;
    private String city;
    private String province;
    private String postCode;
    private String streetAddress;
    private String taxFileNumber;
    private String worksAt;
    /** WORK TYPE */
    @Excel(name = "Work type")
    private String employmentType;
    /** PAYROLL NUMBER */
    @Excel(name = "PAYROLL NUMBER")
    private String payrollId;
    /** SALARY RATE */
    @Excel(name = "SALARY RATE")
    private String payRate;
    /** WORK PERIOD */
    @Excel(name = "WORK PERIOD")
    private String workPeriod;
    /** HOURS PER PERIOD */
    @Excel(name = "HOURS PER PERIOD")
    private String hoursPerPeriod;
    private String perPeriodHours;
    private String salaryPayPeriod;
    private String payFrequency;
    /** DAYS PER PERIOD */
    @Excel(name = "DAYS PER PERIOD")
    private String daysPerPeriod;
    /** PRESSURE PROFILE */
    @Excel(name = "PRESSURE PROFILE")
    private String stressProfile;
    /** Account Level */
    @Excel(name = "Account Level")
    private String accessLevel;

    /** Enterprise Number */
    @Excel(name = "Enterprise Number")
    private String enterpriseId;

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

    private String bsb;

    /** Social Security Cost */
    @Excel(name = "Social Security Cost")
    private Double socialSecurityCost;

    /** Commercial Insurance Cost */
    @Excel(name = "Commercial Insurance Cost")
    private Double comInsuIdCost;

    /** Housing Fund Cost */
    @Excel(name = "Housing Fund Cost")
    private Double accuFundIdCost;

    /** Mobile Phone Number */
    @Excel(name = "Google Unique Identifier")
    private String google;

    private String leaveEntitlements;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    // Employee ID
    private String jobnumber;

    /*Resignation Date*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resignationDate;

    /*resignation Attachment*/
    private String resignationAttachment;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date probationStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date probationEndTime;

    private Long probationSalary;

    private String probationStatus;

    private String swiftOrBsb;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private Long present;

    @TableField(exist = false)
    private Long Late;

    @TableField(exist = false)
    private Long absentDays;

    @TableField(exist = false)
    private String attendanceRate;

    @TableField(exist = false)
    private LocalDate DataTime;

    @TableField(exist = false)
    private String PresentStatus;

    @TableField(exist = false)
    private String LeaveStatus;

    @TableField(exist = false)
    private String HolidayStatus;

    @TableField(exist = false)
    private String statusName;

    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long candidateId;

    @TableField(exist = false)
    private Long deptId;

    @TableField(exist = false)
    private String schedulingId;

    @TableField(exist = false)
    private String noDept;

    @TableField(exist = false)
    private Long noDeptId;

    @TableField(exist = false)
    private Long[] employeeIds;

    @TableField(exist = false)
    private String password;

    @TableField(exist = false)
    private String positionName;

    @TableField(exist = false)
    private Long[] topEmployeeIds;

    @TableField(exist = false)
    private Long leaderId;

    @TableField(exist = false)
    private String userType;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("employeeId", getEmployeeId())
                .append("fullName", getFullName())
                .append("email", getEmail())
                .append("avatarUrl", getAvatarUrl())
                .append("status", getStatus())
                .append("dateOfBirth", getDateOfBirth())
                .append("contactPhone", getContactPhone())
                .append("emergencyContact", getEmergencyContact())
                .append("position", getPosition())
                .append("hireDate", getHireDate())
                .append("payrollId", getPayrollId())
                .append("payRate", getPayRate())
                .append("workPeriod", getWorkPeriod())
                .append("hoursPerPeriod", getHoursPerPeriod())
                .append("daysPerPeriod", getDaysPerPeriod())
                .append("stressProfile", getStressProfile())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
