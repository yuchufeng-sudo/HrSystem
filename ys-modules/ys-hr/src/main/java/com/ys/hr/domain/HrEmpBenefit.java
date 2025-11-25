package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 *   Employee Welfare Application Form entity hr_emp_benefit
 *
 * @author ys
 * @date 2025-06-09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrEmpBenefit extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**  Employee Welfare ID */
    @TableId(value = "benefit_emp_id", type =  IdType.ASSIGN_UUID )
    private String benefitEmpId;
    /** Welfare ID */
    @Excel(name = "Welfare ID")
    private String benefitId;
    /** User  ID */
    @Excel(name = "User  ID")
    private Long userId;
    /** Welfare Status */
    @Excel(name = "Welfare Status")
    private String benefitStatus;
    @Excel(name = "Coverage")
    private String coverage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date benefitData;
    @TableField(exist = false)
    private Double benefitEmpMoney;
    @TableField(exist = false)
    private String fullName;
    @TableField(exist = false)
    private String nickName;
    @TableField(exist = false)
    private String benefitName;
    @TableField(exist = false)
    private Date defaultStartTime;
    @TableField(exist = false)
    private Date defaultEndTime;
    @TableField(exist = false)
    private Character flag;
    @TableField(exist = false)
    private String enterpriseId;
    @TableField(exist = false)
    private String searchValue;
}
