package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.Date;

/**
 * Employee Holiday Object hr_emp_holiday
 *
 * @author ys
 * @date 2025-05-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrEmpHoliday extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Holiday ID */
    private Long empleHolidayId;
    /** Holiday Name */
    @Excel(name = " Holiday  Name")
    private String holidayName;
    /** Whether to allow carry-over: 1 Allow 2 Reject */
    @Excel(name = "Whether to allow carry-over: 1 Allow 2 Reject")
    private String carrayForward;
    /** Whether Paid Leave 1 Allow 2 Reject */
    @Excel(name = "Whether Paid Leave 1 Allow 2 Reject")
    private String paidLeave;

    /** Enterprise Number */
    private String enterpriseId;

    /** Holiday Type */
    @Excel(name = "Holiday Type")
    private String holidayType;

    /** Holiday Start time */
    @Excel(name = "Holiday Start time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date stateTime;

    /** Holiday End time */
    @Excel(name = "Holiday End time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @Excel(name = "User Serial Number")
    private Long userId;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private LocalDate searchTime;

    @TableField(exist = false)
    private String time;

}
