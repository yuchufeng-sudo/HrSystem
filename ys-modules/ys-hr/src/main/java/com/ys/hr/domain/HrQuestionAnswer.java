package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.web.domain.BaseEntity;
import com.ys.common.core.annotation.Excel;
import javax.validation.constraints.NotNull;

/**
 * Candidate question answers table entity hr_question_answer
 *
 * @author ys
 * @date 2025-09-27
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrQuestionAnswer extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key ID */
    @TableId(value = "id", type =  IdType.ASSIGN_UUID )
    private String id;
    /** Candidate ID */
    @Excel(name = "Candidate ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "Candidate ID cannot be empty")
    private Long candidateId;
    /** Question ID */
    @Excel(name = "Question ID")
    @NotNull(message = "Question ID cannot be empty")
    private String questionId;
    /** Question answer content */
    @Excel(name = "Question answer content")
    @NotNull(message = "Question answer content cannot be empty")
    private String questionAnswer;
    /** Enterprise Id */
    @Excel(name = "Enterprise Id")
    private String enterpriseId;

    @TableField(exist = false)
    private String questionMsg;

    @TableField(exist = false)
    private String questionType;

    @TableField(exist = false)
    private String answerOptions;

    @TableField(exist = false)
    private Boolean isAnswerVideo;
}
