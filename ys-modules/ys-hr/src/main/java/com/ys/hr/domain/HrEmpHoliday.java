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
 * EMPLOYEE HOLIDAY Object hr_emp_holiday
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

    /** HOLIDAY ID */
    private Long empleHolidayId;
    /** HOLIDAY Name */
    @Excel(name = " HOLIDAY  Name")
    private String holidayName;
    /** Whether to allow carry-over: 1 Allow 2 Reject */
    @Excel(name = "Whether to allow carry-over: 1 Allow 2 Reject")
    private String carrayForward;
    /** Whether Paid Leave 1 Allow 2 Reject */
    @Excel(name = "Whether Paid Leave 1 Allow 2 Reject")
    private String paidLeave;

    /** Enterprise Number */
    private String enterpriseId;

    /** HOLIDAY Type */
    @Excel(name = "HOLIDAY Type")
    private String holidayType;

    /** HOLIDAY Start TIME */
    @Excel(name = "HOLIDAY Start TIME")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date stateTime;

    /** HOLIDAY End TIME */
    @Excel(name = "HOLIDAY End TIME")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @Excel(name = "USER Serial Number")
    private Long userId;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private LocalDate searchTime;

    @TableField(exist = false)
    private String time;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("empleHolidayId", getEmpleHolidayId())
                .append("holidayName", getHolidayName())
                .append("carrayForward", getCarrayForward())
                .append("paidLeave", getPaidLeave())
                .append("enterpriseId", getEnterpriseId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("holidayType", getHolidayType())
                .append("stateTime", getStateTime())
                .append("endTime", getEndTime())
                .toString();
    }
}
