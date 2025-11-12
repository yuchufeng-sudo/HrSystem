package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * EMPLOYEE SALARY entity hr_payroll
 *
 * @author ys
 * @date 2025-05-30
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrPayroll extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** PAYROLL NUMBER */
    @TableId(value = "payroll_id", type = IdType.ASSIGN_UUID)
    private String payrollId;
    /** USER ID */
    @Excel(name = "USER  ID")
    private Long userId;
    /** USER Name */
    @Excel(name = "USER Name")
    private String nickName;
    /** Working Days */
    @Excel(name = "Working Days")
    private Integer mouthDays;

    /** Attendance Time */
    @Excel(name = "Attendance Time")
    private Double presentHours;

    /** Paid Holiday */
    @Excel(name = "Paid Holiday")
    private Double holidaysHours;

    /** Leave Application Time */
    @Excel(name = "Leave Application Time")
    private Double leaveHours;

    /** Late Time */
    @Excel(name = "Late Time")
    private Double lateHours;

    /** Early Leave Time */
    @Excel(name = "Early Leave Time")
    private Double earlyHours;

    /** Overtime */
    @Excel(name = "Overtime")
    private Double overHours;

    /** Absent Time */
    @Excel(name = "Absent Time")
    private Double absentHours;

    /** Working Hours This Month */
    @Excel(name = "Working Hours This Month")
    private Double workHours;

    /** Month (Salary Month) */
    @JsonFormat(pattern = "yyyy-MM")
    @Excel(name = "Month (Salary Month)", width = 30, dateFormat = "yyyy-MM")
    private Date payrollData;

    /** Basic Salary */
    @Excel(name = "Basic Salary")
    private Long baseSalary;

    /** Overtime Multiplier */
    @Excel(name = "Overtime Multiplier")
    private Double overTimeMultiple;

    /** Social Security Cost */
    @Excel(name = "Social Security Cost")
    private Double socialSecurityCost;

    /** Commercial Insurance Cost */
    @Excel(name = "Commercial Insurance Cost")
    private Double comInsuIdCost;

    /** Housing Fund Cost */
    @Excel(name = "Housing Fund Cost")
    private Double accuFundIdCost;

    /** Additional Costs */
    @Excel(name = "Additional Costs")
    private Double additionalCosts;

    /** Abnormal Time Multiplier (Late, Early Leave) */
    @Excel(name = "Abnormal Time Multiplier (Late, Early Leave)")
    private Double abnormalTimeMultiple;

    /** After-Tax Salary */
    @Excel(name = "After-Tax Salary")
    private Double afterTaxSalary;

    /** Payroll Status */
    @Excel(name = "Payroll Status")
    private Character payrollStatus;

    /** Before-Tax Salary */
    @Excel(name = "Before-Tax Salary")
    private Double beforeTaxSalary;

    /** Overtime Salary */
    @Excel(name = "Overtime Salary")
    private Double overTimeCost;

    /** Violation Salary */
    @Excel(name = "Violation Salary")
    private Double abnormalTimeCost;

    /** Attendance Salary */
    @Excel(name = "Attendance Salary")
    private Double presentCost;

    private String empTimeId;

    @TableField(exist = false)
    private String fullName;

    @TableField(exist = false)
    private String searchTime;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private String flag;

    private String enterpriseId;

    private String payrollNum;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Payday", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hireData;


    @TableField(exist = false)
    private Long employeeId;
    @TableField(exist = false)
    private String bsb;
    @TableField(exist = false)
    private String jobnumber;
    @TableField(exist = false)
    private String bankNumber;



}
