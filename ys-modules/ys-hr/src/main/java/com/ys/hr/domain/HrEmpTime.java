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
 * EMPLOYEE TIME entity hr_emp_time
 *
 * @author ys
 * @date 2025-06-03
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrEmpTime extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** EMPLOYEE Working Hours Confirmation Form */
    @TableId(value = "emp_time_id", type = IdType.ASSIGN_UUID)
    private String empTimeId;

    /** USER ID */
    @Excel(name = "USER ID")
    private Long userId;

    /** USER Name */
    @Excel(name = "USER Name")
    private String nickName;

    /** Working Days */
    @Excel(name = "Working Days")
    private Integer mouthDays;

    /** Attendance TIME */
    @Excel(name = "Attendance TIME")
    private Double presentHours;

    /** Paid HOLIDAY */
    @Excel(name = "Paid HOLIDAY")
    private Double holidaysHours;

    /** LEAVE APPLICATION TIME */
    @Excel(name = "LEAVE APPLICATION TIME")
    private Double leaveHours;

    /** Late TIME */
    @Excel(name = "Late TIME")
    private Double lateHours;

    /** Early Leave TIME */
    @Excel(name = "Early Leave TIME")
    private Double earlyHours;

    /** Overtime TIME */
    @Excel(name = "Overtime TIME")
    private Double overHours;

    /** Absenteeism TIME */
    @Excel(name = "Absenteeism TIME")
    private Double absentHours;

    /** Working Hours This Month */
    @Excel(name = "Working Hours This Month")
    private Double workHours;

    /** Month (SALARY Month) */
    @JsonFormat(pattern = "yyyy-MM")
    @Excel(name = "Month (SALARY Month)", width = 30, dateFormat = "yyyy-MM")
    private Date payrollData;
    /** Enterprise Number */
    @Excel(name = "Enterprise Number")
    private String enterpriseId;

    private String payrollStatus;

    @TableField(exist = false)
    private String searchTime;

    @TableField(exist = false)
    private Long baseSalary;
    @TableField(exist = false)
    private Double socialSecurityCost;
    @TableField(exist = false)
    private Double comInsuIdCost;
    @TableField(exist = false)
    private Double accuFundIdCost;
    @TableField(exist = false)
    private Double beforeTaxSalary;
    @TableField(exist = false)
    private Double overTimeCost;
    @TableField(exist = false)
    private Double abnormalTimeCost;
    @TableField(exist = false)
    private Double presentCost;

}
