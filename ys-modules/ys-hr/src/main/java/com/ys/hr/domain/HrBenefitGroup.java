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
 * Table of benefit types entity hr_benefit_group
 *
 * @author ys
 * @date 2025-07-10
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrBenefitGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Benefit group number */
    @TableId(value = "benefit_group_id", type =  IdType.ASSIGN_UUID )
    private String benefitGroupId;
    /** The name of the benefit group */
    @Excel(name = "The name of the benefit group")
    private String groupName;
    /** Business number */
    @Excel(name = "Business number")
    private String enterpriseId;

}
