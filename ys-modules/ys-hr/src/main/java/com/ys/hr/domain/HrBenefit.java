package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *  WELFARE BENEFITS entity hr_benefit
 *
 * @author ys
 * @date 2025-06-09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrBenefit extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Welfare ID */
    @TableId(value = "benefit_id", type =  IdType.ASSIGN_UUID )
    private String benefitId;
    /**  WELFARE TYPE  ID */
    @Excel(name = " WELFARE TYPE  ID")
    private String benefitTypeId;
    private String benefitName;
    /**  WELFARE BENEFITS */
    @Excel(name = " WELFARE BENEFITS")
    private Double benefitEmpMoney;
    /**  WELFARE BENEFITS */
    @Excel(name = " WELFARE BENEFITS")
    private String benefitEmpCompany;
    /** Welfare Applicable Groups */
    @Excel(name = "Welfare Applicable Groups")
    private String benefitColony;
    /** Default workday start time */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Default workday start time")
    private Date defaultStartTime;
    /** Default workday end time */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Default workday end time")
    private Date defaultEndTime;
    /** Enterprise Number */
    @Excel(name = "Enterprise Number")
    private String enterpriseId;

}
