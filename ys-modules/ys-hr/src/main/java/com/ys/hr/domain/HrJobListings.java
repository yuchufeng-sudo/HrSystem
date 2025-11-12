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
import java.math.BigDecimal;
import java.util.List;

/**
 * Job Listings entity hr_job_listings
 *
 * @author ys
 * @date 2025-09-25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrJobListings extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Job Listings ID */
    @TableId(value = "id", type =  IdType.ASSIGN_UUID )
    private String id;
    /** Job Listings Title */
    @Excel(name = "Job Listings Title")
    private String title;
    /** Employment Type */
    @Excel(name = "Employment Type")
    private String employmentType;
    /** currency */
    @Excel(name = "currency")
    private String currency;
    /** salaryType */
    @Excel(name = "salaryType")
    private String salaryType;
    /** Location */
    @Excel(name = "Location")
    private String location;
    /** Experience */
    @Excel(name = "Experience")
    private String experience;
    /** Pay Min Amount */
    @Excel(name = "Pay Min Amount")
    private BigDecimal payMin;
    /** Pay Max Amount */
    @Excel(name = "Pay Max Amount")
    private BigDecimal payMax;
    /** pay Type */
    @Excel(name = "pay Type")
    private String payType;
    /** Details */
    @Excel(name = "Details")
    private String details;
    /** Company ID */
    @Excel(name = "Company ID")
    private String enterpriseId;
    /** Show Pay */
    @Excel(name = "Show Pay")
    private Boolean showPay;
    /** Show Experience */
    @Excel(name = "Show Experience")
    private String showExperience;
    /** Status */
    @Excel(name = "Status")
    private String status;

    @TableField(exist = false)
    private List<HrJobListingsQuestion> questions;

    @TableField(exist = false)
    private String careersId;

    @TableField(exist = false)
    private String enterpriseName;

    @TableField(exist = false)
    private String enterpriseLogo;

}
