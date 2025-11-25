package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Employee Contract Object tb_emp_contract
 *
 * @author collection
 * @date 2023-02-26
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrEmployeeContract extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    private String id;

    @TableField(exist = false)
    private Long userId;

    /** Recruiter ID */
    private Long aUserId;

    /** Employee ID */
    private Long bUserId;

    /** POSITION */
    private String post;

    /** Company ID */
    private String companyId;

    /** Company Name */
    private String companyName;

    /** Company Phone */
    private String companyPhone;

    private String companyAddress;

    /**
     * Company Email
     */
    private String companyEmail;

    /** Last Name */
    private String name;

    /** ID Number */
    private String idNumber;

    /** Contact Address */
    private String relationAddress;

    /** Household Register Address */
    private String permanentAddress;

    /** Phone */
    private String phone;

    /**
     * User Email
     */
    private String email;

    /** Bank Account Number */
    private String bankNumber;

    /** Template Content */
    private String templateContent;

    /** Whether Labor Contract Signed */
    private String isSign;

    /** Probation Period (0: No, 1: Yes) */
    private String hasProbationPeriod;

    /** Monthly Salary */
    private BigDecimal salary;

    /** Probation Monthly Salary */
    private BigDecimal trySalary;

    /** Contract Type */
    @Excel(name = "Contract Type")
    private String contractType;

    /** Contract Form */
    private String contractForm;

    /** Contract Start Date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date contractStartTime;

    /** Contract End Date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date contractEndTime;

    /** Probation Start Time */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tryStartDate;

    /** Probation End Time */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tryEndDate;

    /** Signing Date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signDate;

    /** Contract Code */
    private String contractCode;

    /** Contract Attachment */
    private String accessory;

    /** Signing Process ID */
    private String signFlowId;

    /** Contract File ID */
    private String fileId;

    /** Signing Status */
    private String signStatu;

    private String signUrl1;

    private String signUrl2;

    // User Signing Status
    private String userSign;

    // Enterprise Signing Status
    private String enterpriseSign;

    // Payday
    private String paymentDate;

    /**
     * Contract Type
     */
    private String signType;

    /**
     * Platform Type
     */
    private Integer platformType;

    /**
     * Contract Template ID
     */
    private Long templateId;

    /**
     * Position Id
     */
    private Long postId;

    private String payRate;

    @Excel(name = "Account Holder Name")
    private String bankName;

    /** Bank of Deposit */
    @Excel(name = "Bank of Deposit")
    private String openingBank;

    @Excel(name = "Social Security Cost")
    private Double socialSecurityCost;

    /** Commercial Insurance Cost */
    @Excel(name = "Commercial Insurance Cost")
    private Double comInsuIdCost;

    /** Housing Fund Cost */
    @Excel(name = "Housing Fund Cost")
    private Double accuFundIdCost;

    /**
     * Whether Noticed
     */
    private String isNotify;

    /**
     * Whether Expired
     */
    private String isMaturity;

    private Integer signaturePlatformId;

    @TableField(exist = false)
    private String userType;

    @TableField(exist = false)
    private String signUrl;

    /**
     * Email Content
     */
    @TableField(exist = false)
    private String message;

    /**
     * Email Subject
     */
    @TableField(exist = false)
    private String subject;

    @TableField(exist = false)
    private Long maturityContractId;

    @TableField(exist = false)
    private String empId;

}
