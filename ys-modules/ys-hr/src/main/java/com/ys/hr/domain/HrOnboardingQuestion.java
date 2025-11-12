package com.ys.hr.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import javax.validation.constraints.Size;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ys.common.core.web.domain.BaseEntity;
import com.ys.common.core.annotation.Excel;
import javax.validation.constraints.NotNull;

/**
 * Job listings questions table entity hr_onboarding_question
 *
 * @author ys
 * @date 2025-10-13
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrOnboardingQuestion extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key ID */
    @TableId(value = "id", type =  IdType.ASSIGN_UUID )
    private String id;
    /** Question description */
    @Excel(name = "Question description")
    @NotNull(message = "Question description cannot be empty")
    private String questionMsg;
    /** Question type */
    @Excel(name = "Question type")
    @NotNull(message = "Question type cannot be empty")
    private String questionType;
    /** Answer options */
    @Excel(name = "Answer options")
    private String answerOptions;
    /** Is Required */
    @Excel(name = "Is Required")
    private Boolean isRequired;

    private Integer questionIndex;

    private String enterpriseId;

    private String employeesId;

    @TableField(exist = false)
    private Integer isResult;

    @TableField(exist = false)
    private String result;

    @TableField(exist = false)
    private String resultId;

}
