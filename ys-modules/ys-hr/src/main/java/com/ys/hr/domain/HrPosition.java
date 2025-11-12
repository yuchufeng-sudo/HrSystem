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

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Position Management entity hr_position
 *
 * @author ys
 * @date 2025-06-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrPosition extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key identifier, auto-increment */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Position code */
    @Excel(name = "Position code")
    private String positionCode;
    /** Position name */
    @Excel(name = "Position name")
    @NotNull(message = "Position name cannot be empty")
    private String positionName;
    /** Job responsibilities */
    @Excel(name = "Job responsibilities")
    @NotNull(message = "Job responsibilities cannot be empty")
    private String positionDuty;
    /** Minimum salary */
    @Excel(name = "Minimum salary")
    private BigDecimal salaryMin;
    /** Maximum salary */
    @Excel(name = "Maximum salary")
    private BigDecimal salaryMax;
    /** currency */
    @Excel(name = "currency")
    private String currency;

    @TableField(exist = false)
    private Integer flag;

    private Long jobId;

    private String enterpriseId;
}
