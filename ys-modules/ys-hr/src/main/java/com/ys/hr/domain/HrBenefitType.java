package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  WELFARE TYPE   LIST entity hr_benefit_type
 *
 * @author ys
 * @date 2025-06-09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrBenefitType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**  WELFARE TYPE  ID */
    @TableId(value = "benefit_type_id", type =  IdType.ASSIGN_UUID )
    private String benefitTypeId;
    /**  WELFARE TYPE Name */
    @Excel(name = " WELFARE TYPE Name")
    private String benefitName;
    /** Enterprise Number */
    @Excel(name = "Enterprise Number")
    private String enterpriseId;

}
