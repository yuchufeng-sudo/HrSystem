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
 * Job listings questions entity hr_job_listings_question
 *
 * @author ys
 * @date 2025-09-27
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrJobListingsQuestion extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key ID */
    @TableId(value = "id", type =  IdType.ASSIGN_UUID )
    private String id;
    /** Job listing ID */
    @Excel(name = "Job listing ID")
    @NotNull(message = "Job listing ID cannot be empty")
    private String jobListingsId;
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
    /** Video answer required */
    @Excel(name = "Video answer required")
    @NotNull(message = "Video answer required cannot be empty")
    private Boolean isAnswerVideo;

    private Integer questionIndex;

    private String enterpriseId;
}
