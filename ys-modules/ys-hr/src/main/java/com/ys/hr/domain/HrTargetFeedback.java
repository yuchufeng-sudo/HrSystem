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
 * Target Feedback entity hr_target_feedback
 *
 * @author ys
 * @date 2025-06-30
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrTargetFeedback extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.AUTO )
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /** Enterprise ID */
    @Excel(name = "Enterprise ID")
    private String enterpriseId;
    /** Target ID */
    @Excel(name = "Target ID")
    @NotNull(message = "Target ID cannot be empty")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long targetId;
    /** Feedback provider ID */
    @Excel(name = "Feedback provider ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long feedbackBy;
    /** Feedback recipient ID */
    @Excel(name = "Feedback recipient ID")
    @NotNull(message = "Feedback recipient ID cannot be empty")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long feedbackTo;
    /** Feedback timestamp */
    @Excel(name = "Feedback timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date feedbackTime;
    /** Feedback category */
    @Excel(name = "Feedback category")
    @NotNull(message = "Feedback category cannot be empty")
    private String category;
    /** Rating score */
    @Excel(name = "Rating score")
    @NotNull(message = "Rating score cannot be empty")
    private Integer rating;
    /** Detailed feedback */
    @Excel(name = "Detailed feedback")
    private String feedbackContent;
    /** Is anonymous feedback */
    @Excel(name = "Is anonymous feedback")
    private Integer anonymously;

    @TableField(exist = false)
    private String feedbackByName;

    @TableField(exist = false)
    private String feedbackByAvatar;

    @TableField(exist = false)
    private String feedbackToName;

    @TableField(exist = false)
    private String feedbackToAvatar;
}
