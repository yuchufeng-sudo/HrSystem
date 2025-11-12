package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Employee Probationary Evaluation entity hr_probation_evaluation
 *
 * @author ys
 * @date 2025-06-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrProbationEvaluation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key ID */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Associated employee ID */
    @Excel(name = "Associated employee ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "Associated employee ID cannot be empty")
    private Long employeeId;
    /** Evaluation score */
    @Excel(name = "Evaluation score")
    @NotNull(message = "Evaluation score cannot be empty")
    private BigDecimal score;
    /** evaluator */
    @Excel(name = "evaluator")
    @Size(max = 50, message = "evaluator length cannot exceed 50 characters")
    private String evaluator;
    /** Detailed content of the evaluation */
    @Excel(name = "Detailed content of the evaluation")
    @NotNull(message = "Detailed content of the evaluation cannot be empty")
    private String evaluationContent;

}
