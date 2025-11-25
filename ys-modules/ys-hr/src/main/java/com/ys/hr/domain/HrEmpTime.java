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
 * Employee time entity hr_emp_time
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

    /** Employee Working Hours Confirmation Form */
    @TableId(value = "emp_time_id", type = IdType.ASSIGN_UUID)
    private String empTimeId;

    /** User ID */
    @Excel(name = "User ID")
    private Long userId;

    /** User Name */
    @Excel(name = "User Name")
    private String nickName;

    /** Working Days */
    @Excel(name = "Working Days")
    private Integer mouthDays;

    /** Attendance time */
    @Excel(name = "Attendance time")
    private Double presentHours;

    /** Paid Holiday */
    @Excel(name = "Paid Holiday")
    private Double holidaysHours;

    /** Leave Application time */
    @Excel(name = "Leave Application time")
    private Double leaveHours;

    /** Late time */
    @Excel(name = "Late time")
    private Double lateHours;

    /** Early Leave time */
    @Excel(name = "Early Leave time")
    private Double earlyHours;

    /** Overtime time */
    @Excel(name = "Overtime time")
    private Double overHours;

    /** Absenteeism time */
    @Excel(name = "Absenteeism time")
    private Double absentHours;

    /** Working Hours This Month */
    @Excel(name = "Working Hours This Month")
    private Double workHours;

    /** Month (salary Month) */
    @JsonFormat(pattern = "yyyy-MM")
    @Excel(name = "Month (salary Month)", width = 30, dateFormat = "yyyy-MM")
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
