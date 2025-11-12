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
 * Onboarding Result entity hr_onboarding_result
 *
 * @author ys
 * @date 2025-10-14
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrOnboardingResult extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.ASSIGN_UUID )
    private String id;
    /** onboarding ID */
    @Excel(name = "onboarding ID")
    private String onboardingId;
    /** step */
    @Excel(name = "step")
    private Integer step;
    /** result */
    @Excel(name = "result")
    private String result;
    /** Enterprise ID */
    @Excel(name = "Enterprise ID")
    private String enterpriseId;
    /** employees ID */
    @Excel(name = "employees ID")
    private String employeesId;

}
