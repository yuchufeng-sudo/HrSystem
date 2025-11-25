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

import java.time.LocalTime;
import java.util.Date;

/**
 * Attendance RecordObject hr_attendance
 *
 * @author ys
 * @date 2025-05-26
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrAttendance extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Attendance  ID */
    @TableId(value = "attendance_id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long attendanceId;
    /** User  ID */
    private Long userId;
    /** User Name */
    @Excel(name = "User Name")
    private String nickName;

    @Excel(name = "Work Clock-in Time")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime checkIn;

    @Excel(name = "Work Clock-out Time")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime checkOut;

    @Excel(name = "Scheduled Working Hours")
    private String workTime;

    @Excel(name = "Actual Working Hours")
    private String myWorkTime;

    /** Attendance Status */
    @Excel(name = "Attendance Status",readConverterExp = "1=Present")
    private String attendanceStatus;

    /** Attendance Status */
    private String presentStatus;

    @Excel(name = "Present Status")
    @TableField(exist = false)
    /** Attendance Status */
    private String presentStatusString;

    @Excel(name = "Attendance Date")
    @TableField(exist = false)
    private String time;

    /** Overtime Hours */
    @Excel(name = "Overtime Hours")
    private String overTime;

    /** Late Arrival Time */
    @Excel(name = "Late Arrival Time")
    private String lateTime;

    /** Early Departure Time */
    @Excel(name = "Early Departure Time")
    private String earlyTime;

    /** Early Arrival Time */
    private String inAdvanceTime;

    /** Attendance Date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date attendanceTime;
    /** Enterprise Number */
    private String enterpriseId;

    // Clock in  Type
    private Integer punchType;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private Integer flag;

    @TableField(exist = false)
    private String searchValue;

    @TableField(exist = false)
    private String searchTime;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private Long absentDays;
    @TableField(exist = false)
    private Long searchId;
    @TableField(exist = false)
    private String startDate;
    @TableField(exist = false)
    private String endDate;
    @TableField(exist = false)
    private String absentDate;


    @TableField(exist = false)
    private String weekDay;


    @TableField(exist = false)
    private Double restTime;


    @TableField(exist = false)
    private String isBefore;


    @TableField(exist = false)
    private String showType;

    @TableField(exist = false)
    private Integer year;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM")
    private Date searchData;

}
