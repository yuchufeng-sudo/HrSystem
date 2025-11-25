package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Date;

/**
 * Employee Scheduling entity hr_emp_scheduling
 *
 * @author ys
 * @date 2025-06-04
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrEmpScheduling extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** LAYOUT ID */
    @TableId(value = "scheduling_id", type = IdType.ASSIGN_UUID)
    private String schedulingId;
    /** LAYOUT Type(Weekly, Monthly, Daily) */
    @Excel(name = "LAYOUT Type(Weekly, Monthly, Daily)")
    private Character schedulingType;

    /** Weekly Date */
    @Excel(name = "Weekly Date")
    private String weekDays;

    /** Monthly Holiday */
    @Excel(name = "Monthly Holiday")
    private String monthDaysOff;

    /** Special Shift */
    @Excel(name = "Special Shift")
    private String dailySchedule;

    /** Monthly Paid Vacation Days */
    @Excel(name = "Monthly Paid Vacation Days")
    private Long paidVacationDays;

    @JsonFormat(pattern = "yyyy-MM")
    @Excel(name = "Month", width = 30, dateFormat = "yyyy-MM")
    private Date schedulingData;

    /** User ID */
    @Excel(name = "User ID")
    private Long userId;

    private String schedulingName;

    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "Default Workday Start Time", width = 30)
    @NotNull(message = "Default Workday Start Time cannot be empty")
    private LocalTime defaultStartTime;

    /** Default Workday End Time */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "Default Workday End Time", width = 30)
    @NotNull(message = "Default Workday End Time cannot be empty")
    private LocalTime defaultEndTime;

    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "Lunch Break Start time", width = 30)
    private LocalTime restStartTime;

    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "Lunch Break End time", width = 30)
    private LocalTime restEndTime;
    /** Enterprise ID */
    @Excel(name = "Enterprise ID")
    private String enterpriseId;
}
