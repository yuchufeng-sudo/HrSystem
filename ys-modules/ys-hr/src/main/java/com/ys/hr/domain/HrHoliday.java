package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * HOLIDAY Object hr_holiday
 *
 * @author ys
 * @date 2025-05-22
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrHoliday extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** HOLIDAY ID */
    @TableId(value = "holiday_id", type = IdType.AUTO)
    private Long holidayId;
    /** HOLIDAY Name */
    @Excel(name = " HOLIDAY  Name")
    private String holidayName;
    /** Maximum Days */
    @Excel(name = "Maximum Days")
    private Long maxDay;
    /** Whether to allow carry-over: 1 Allow 2 Reject */
    @Excel(name = "Whether to allow carry-over: 1 Allow 2 Reject")
    private String carrayForward;
    /** Whether Paid Leave 1 Allow 2 Reject */
    @Excel(name = "Whether Paid Leave 1 Allow 2 Reject")
    private String paidLeave;
    /** Enterprise Number */
    private String enterpriseId;

    /** HOLIDAY Type */
    private String holidayType;

    @TableField(exist = false)
    private boolean paid;

    @TableField(exist = false)
    private boolean carray;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("holidayId", getHolidayId())
                .append("holidayName", getHolidayName())
                .append("maxDay", getMaxDay())
                .append("carrayForward", getCarrayForward())
                .append("paidLeave", getPaidLeave())
                .append("enterpriseId", getEnterpriseId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
