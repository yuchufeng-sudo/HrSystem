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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Target allocation of employees entity hr_target_employee
 *
 * @author ys
 * @date 2025-06-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrTargetEmployee extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** Target ID */
    @Excel(name = "Target ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "Target ID cannot be null")
    private Long targetId;

    /** Employee ID */
    @Excel(name = "Employee ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "Employee ID cannot be null")
    private Long employeeId;

    /** Type */
    @Excel(name = "Type")
    @NotNull(message = "Type cannot be null")
    @PositiveOrZero(message = "Type must be a non-negative number")
    private Long type;

    /** Start flag (Y/N) */
    @NotNull(message = "Start flag cannot be null")
    @Size(max = 1, message = "Start flag must be 1 character")
    private String isStart;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date executionTime;

    /** Execution seconds */
    @PositiveOrZero(message = "Execution seconds must be a non-negative number")
    private Long executionSecond;

    @TableField(exist = false)
    private Boolean startStatus;

    /** Complete time (string format) */
    @Size(max = 20, message = "Complete time must be less than 20 characters")
    private String completeTime;

    @TableField(exist = false)
    private String employeeName;

    @TableField(exist = false)
    private String employeeAvatar;

    @TableField(exist = false)
    private String employeePosition;

    @TableField(exist = false)
    private Integer isNotFeedback;

    @TableField(exist = false)
    private Long userId;

    @TableField(exist = false)
    private String targetName;

    @TableField(exist = false)
    private String description;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date targetCreateTime;

    @TableField(exist = false)
    private Integer isAssigned;

    @TableField(exist = false)
    private String targetExecutionTime;

    @TableField(exist = false)
    private Date stopTime;

}
