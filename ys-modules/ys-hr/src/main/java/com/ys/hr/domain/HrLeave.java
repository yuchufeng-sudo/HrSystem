package com.ys.hr.domain;

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

import java.util.Date;

/**
 * LEAVE APPLICATION Object hr_leave
 *
 * @author ys
 * @date 2025-05-21
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrLeave extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "Nick Name")
    @TableField(exist = false)
    private String nickName;

    @TableId(value = "leave_id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long leaveId;
    /** LEAVE APPLICATION Start TIME */
    @Excel(name = "LEAVE APPLICATION Start TIME")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Date stateTime;
    /** LEAVE APPLICATION End TIME */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    @Excel(name = "LEAVE APPLICATION End TIME")
    private Date endTime;
    /** Leave Note Status */
    @Excel(name = "Leave Note Status",readConverterExp = "1=Approved,2=Applied,3=Pending,4=Rejected")
    private String leaveStatus;
    /** Leave Note Type */
    @Excel(name = "Leave Note Type",readConverterExp = "1=Annual Leave,2=Sick Leave,3=Maternity Leave,6=Personal Leave,5=Paid Leave")
    private String leaveType;
    /** MANAGEMENT Staff ID */
    private Long managerId;
    /** LEAVE APPLICATION Reason */
    private String leaveReason;
    /** Rejection Reason */
    private String rejectReason;
    private String leaveDaysJson;
    /** USER ID */
    private Long userId;
    /** Enterprise Number */
    private String enterpriseId;
    /** LEAVE APPLICATION Days */
    @Excel(name = "Leave Hours")
    private Long leaveDuration;

    /** Whether Paid Leave 1 Allow 2 Reject */
    @Excel(name = "Whether Paid Leave 1 Allow 2 Reject")
    private String paidLeave;

    private String syncId;

    @TableField(exist = false)
    private Long remainingDays;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private Long deptId;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private String leaderNickName;

    @TableField(exist = false)
    private String leaderAvatar;

    @TableField(exist = false)
    private String email;

    // Determine whether it is an AND USER or HR.
    @TableField(exist = false)
    private Character flag;

    // Finally LEAVE APPLICATION TIME
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date LastStateTime;
    // Finally LEAVE APPLICATION TIME
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date LastEndTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("leaveId", getLeaveId())
                .append("stateTime", getStateTime())
                .append("endTime", getEndTime())
                .append("leaveStatus", getLeaveStatus())
                .append("leaveType", getLeaveType())
                .append("managerId", getManagerId())
                .append("leaveReason", getLeaveReason())
                .append("rejectReason", getRejectReason())
                .append("userId", getUserId())
                .append("enterpriseId", getEnterpriseId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("leaveDuration", getLeaveDuration())
                .toString();
    }
}
