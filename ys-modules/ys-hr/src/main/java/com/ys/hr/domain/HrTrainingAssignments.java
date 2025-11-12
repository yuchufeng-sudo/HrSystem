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

/**
 * TRAINING ALLOCATION RECORD entity hr_training_assignments
 *
 * @author ys
 * @date 2025-05-29
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrTrainingAssignments extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "assignment_id", type =  IdType.AUTO )
    private Long assignmentId;
    /** Reference to training program */
    @Excel(name = "Reference to training program")
    private Long programId;
    /** Assigned employee from image4 selection */
    @Excel(name = "Assigned employee from image4 selection")
    private Long employeeId;

    @TableField(exist = false)
    private String employeeName;

    @TableField(exist = false)
    private String employeeAvatar;

    @TableField(exist = false)
    private String employeePosition;
}
