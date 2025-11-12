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

import java.time.LocalTime;
import java.util.Date;

/**
 * EMPLOYEE SCHEDULING entity hr_scheduling_emp
 *
 * @author ys
 * @date 2025-06-08
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrSchedulingEmp extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** LAYOUT ID */
    @TableId(value = "scheduling_emp_id", type = IdType.ASSIGN_UUID)
    private String schedulingEmpId;
    /** USER ID */
    @Excel(name = "USER  ID")
    private Long userId;
    /** Scheduling Month */
    @Excel(name = "Scheduling Month", width = 30, dateFormat = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date schedulingData;

    private String schedulingId;

    @TableField(exist = false)
    @Excel(name = "Monthly Paid Vacation Days")
    private Long paidVacationDays;
    @TableField(exist = false)
    private String schedulingName;
    @TableField(exist = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime defaultStartTime;
    /** Default workday end time */
    @TableField(exist = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime defaultEndTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime restStartTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime restEndTime;
    @TableField(exist = false)
    private Character schedulingType;
    @TableField(exist = false)
    private String weekDays;
    /** Monthly holiday */
    @TableField(exist = false)
    private String monthDaysOff;
    @TableField(exist = false)
    private String searchTime;
    @TableField(exist = false)
    private Double workTime;

}
