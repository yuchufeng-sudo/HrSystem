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

import javax.validation.constraints.Size;

/**
 * application entity hr_application
 *
 * @author ys
 * @date 2025-06-17
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrApplication extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Application ID */
    @TableId(value = "application_id", type = IdType.ASSIGN_UUID)
    private String applicationId;

    /** Applicant Name */
    @Size(max = 50, message = "The applicant name cannot exceed 50 characters in length")
    @Excel(name = "Applicant Name")
    private String applicationName;

    /** Applicant Email */
    @Excel(name = "Applicant Email")
    private String applicationEmail;

    /** Enterprise Name */
    @Size(max = 50, message = "The enterprise name cannot exceed 50 characters in length")
    @Excel(name = "Enterprise Name")
    private String bussinessName;

    /** Enterprise Phone */
    @Excel(name = "Enterprise Phone")
    private String bussinessPhone;

    /** WORK TYPE (Full-time Part-time) */
    @Excel(name = "WORK TYPE (Full-time Part-time)")
    private String workspaceType;

    /** EMPLOYEE Count */
    @Excel(name = "EMPLOYEE Count")
    private String empNumber;

    /** Country Type */
    @Excel(name = "Country Type")
    private String countryCode;

    /** Change Type */
    @Excel(name = "Change Type")
    private String improveType;

    /** Source */
    @Excel(name = "Source")
    private String source;

    /** Reason */
    @Excel(name = "Reason")
    private String bringType;

    private String phoneCode;

    /** Payment Type */
    @Excel(name = "Payment Type")
    private String payrollType;

    @Excel(name = "status")
    private String status;

    @TableField(exist = false)
    private String url;

}
