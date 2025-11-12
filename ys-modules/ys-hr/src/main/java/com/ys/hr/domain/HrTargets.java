package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
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
 * Main table storing all target information entity hr_targets
 *
 * @author ys
 * @date 2025-06-18
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrTargets extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key identifier */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** Name of the target */
    @Excel(name = "Name of the target")
    @NotNull(message = "Target name cannot be null")
    @Size(max = 200, message = "Target name must be less than 200 characters")
    private String name;

    /** Detailed description of the target */
    @Excel(name = "Detailed description of the target")
    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    /** Type of target: Team Goal/KPI/Personal Goal */
    @Excel(name = "Type of target: Team Goal/KPI/Personal Goal")
    @Size(max = 50, message = "Target type must be less than 50 characters")
    private String type;

    /** Current status: Complete/In progress/Not started */
    @Excel(name = "Current status: Complete/In progress/Not started")
    @Size(max = 50, message = "Status must be less than 50 characters")
    private String status;

    /** Start timestamp of the target period */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Start timestamp of the target period", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** End timestamp of the target period */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "End timestamp of the target period", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** Completion percentage */
    @Excel(name = "Completion percentage")
    private Integer progress;

    /** Flag indicating if this is a key target */
    @Excel(name = "Flag indicating if this is a key target")
    @Size(max = 10, message = "Key target flag must be less than 10 characters")
    private String isKeyTarget;

    private Long head;

    @Size(max = 50, message = "Enterprise ID must be less than 50 characters")
    private String enterpriseId;

    @JsonFormat(pattern = "yyyy-MM-dd mm:ss")
    @Excel(name = "complete Time", width = 30, dateFormat = "yyyy-MM-dd mm:ss")
    private Date completeTime;

    @JsonFormat(pattern = "yyyy-MM-dd mm:ss")
    @Excel(name = "complete Time", width = 30, dateFormat = "yyyy-MM-dd mm:ss")
    private Date configTime;

    private Long resignEmployeeId;

    @Version
    private Integer version;

    @TableField(exist = false)
    private Long[] employeeIds;

    @TableField(exist = false)
    private Long userId;

    @TableField(exist = false)
    private HrTargetEmployee targetEmployee;
}
