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
 * Targets Manages entity hr_target_tasks
 *
 * @author ys
 * @date 2025-06-18
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrTargetTasks extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key identifier */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** Reference to the parent target */
    @Excel(name = "Reference to the parent target")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long targetId;

    /** Name of the task */
    @Excel(name = "Name of the task")
    @NotNull(message = "Task name cannot be empty")
    @Size(max = 200, message = "Task name must be less than 200 characters")
    private String name;

    /** Detailed description of the task */
    @Excel(name = "Detailed description of the task")
    @Size(max = 2000, message = "Task description must be less than 2000 characters")
    private String description;

    /** Current status: Complete/In progress/Not started */
    @Excel(name = "Current status: Complete/In progress/Not started")
    @Size(max = 50, message = "Status must be less than 50 characters")
    private String status;

    /** Due timestamp for task completion */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Due timestamp for task completion", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dueTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Start timestamp", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** Priority level (1=highest, 5=lowest) */
    @Excel(name = "Priority level (1=highest, 5=lowest)")
    @PositiveOrZero(message = "Priority must be a non-negative number")
    private Integer priority;

    /** Completion percentage */
    @Excel(name = "Completion percentage")
    @PositiveOrZero(message = "Progress must be a non-negative number")
    private Integer progress;

    @PositiveOrZero(message = "Completion progress must be a non-negative number")
    private Integer completionProgress;

    /** Attachment path */
    @Size(max = 500, message = "Attachment path must be less than 500 characters")
    private String attachment;

    @JsonFormat(pattern = "yyyy-MM-dd mm:ss")
    @Excel(name = "complete Time", width = 30, dateFormat = "yyyy-MM-dd mm:ss")
    private Date completeTime;

    @JsonFormat(pattern = "yyyy-MM-dd mm:ss")
    @Excel(name = "config Time", width = 30, dateFormat = "yyyy-MM-dd mm:ss")
    private Date configTime;

    @TableField(exist = false)
    private Long[] employeeIds;
}
