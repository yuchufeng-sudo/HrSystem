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
 * Comprehensive fund information entity hr_fund_information
 *
 * @author ys
 * @date 2025-10-17
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrFundInformation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key identifier */
    @TableId(value = "id", type =  IdType.ASSIGN_UUID )
    private String id;
    /** Type of fund (APRA/SMSF) */
    @Excel(name = "Type of fund (APRA/SMSF)")
    @NotNull(message = "Type of fund (APRA/SMSF) cannot be empty")
    private String fundType;
    /** URLs of required documents */
    @Excel(name = "URLs of required documents")
    private String documentUrls;
    /** Type of tax declaration */
    @Excel(name = "Type of tax declaration")
    private String taxDeclarationType;
    /** Flag indicating user agreement */
    @Excel(name = "Flag indicating user agreement")
    private Boolean isAgree;
    /** Unique fund identifier */
    @Excel(name = "Unique fund identifier")
    private String fundId;
    /** Account name for APRA funds */
    @Excel(name = "Account name for APRA funds")
    private String apraAccountName;
    /** Member account number */
    @Excel(name = "Member account number")
    private String memberAccountNumber;
    /** Name of the SMSF */
    @Excel(name = "Name of the SMSF")
    private String smsfName;
    /** ABN number of the SMSF */
    @Excel(name = "ABN number of the SMSF")
    private String smsfAbn;
    /** Electronic service address */
    @Excel(name = "Electronic service address")
    private String esa;
    /** Contact email address for the fund */
    @Excel(name = "Contact email address for the fund")
    private String fundContactEmail;
    /** Member number for SMSF */
    @Excel(name = "Member number for SMSF")
    private String smsfMemberNumber;
    /** Name on bank account */
    @Excel(name = "Name on bank account")
    private String bankAccountName;
    /** Bank BSB code */
    @Excel(name = "Bank BSB code")
    private String bankAccountBsb;
    /** Bank account number */
    @Excel(name = "Bank account number")
    private String bankAccountNumber;

    private String enterpriseId;

    private String employeeId;

    private String smsfAccountName;
}
