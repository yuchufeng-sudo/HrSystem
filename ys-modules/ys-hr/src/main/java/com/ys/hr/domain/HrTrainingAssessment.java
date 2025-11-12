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
import java.util.Date;

/**
 * Training Assessment entity hr_training_assessment
 *
 * @author ys
 * @date 2025-06-25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrTrainingAssessment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Assessment ID */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Training ID */
    @Excel(name = "Training ID")
    @NotNull(message = "Training ID cannot be empty")
    private String trainingId;
    /** Assessor ID */
    @Excel(name = "Assessor ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long assessorId;
    /** Assessee ID */
    @Excel(name = "Assessee ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "Assessee ID cannot be empty")
    private Long assesseeId;
    /** Assessment time */
    @Excel(name = "Assessment time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date assessmentTime;
    /** Assessment comments */
    @Excel(name = "Assessment comments")
    private String comments;

    @TableField(exist = false)
    private String assessorName;

    @TableField(exist = false)
    private String assessorAvatar;

    @TableField(exist = false)
    private String assesseeName;

    @TableField(exist = false)
    private String assesseeAvatar;
}
