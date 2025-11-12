package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Job Management entity hr_job
 *
 * @author ys
 * @date 2025-06-24
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrJob extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Job title */
    @Excel(name = "Job title")
    @NotNull(message = "Job title cannot be empty")
    private String jobTitle;
    /** Job level/grade */
    @Excel(name = "Job level/grade")
    @NotNull(message = "Job level/grade cannot be empty")
    private String jobLevel;
    /** Job classification */
    @Excel(name = "Job classification")
    @NotNull(message = "Job classification cannot be empty")
    private String jobCategory;
    /** Job nature */
    @Excel(name = "Job nature")
    private String jobType;
    /** Minimum salary range */
    @Excel(name = "Minimum salary range")
    private BigDecimal minSalary;
    /** Maximum salary range */
    @Excel(name = "Maximum salary range")
    private BigDecimal maxSalary;
    /** Job responsibilities */
    @Excel(name = "Job responsibilities")
    @NotNull(message = "Job responsibilities cannot be empty")
    private String responsibilities;
    /** Job requirements */
    @Excel(name = "Job requirements")
    @NotNull(message = "Job requirements cannot be empty")
    private String requirements;
    /** Job description */
    @Excel(name = "Job description")
    private String description;
    /** Job status */
    @Excel(name = "Job status")
    @NotNull(message = "Job status cannot be empty")
    private String status;
    /** currency */
    @Excel(name = "currency")
    private String currency;

    private String enterpriseId;

}
