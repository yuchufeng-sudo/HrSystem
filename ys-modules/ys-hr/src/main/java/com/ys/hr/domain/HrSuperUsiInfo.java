package com.ys.hr.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ys.common.core.web.domain.BaseEntity;
import com.ys.common.core.annotation.Excel;
import javax.validation.constraints.NotNull;

/**
 * USI and product information table entity hr_super_usi_info
 *
 * @author ys
 * @date 2025-10-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrSuperUsiInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key ID */
    @TableId(value = "id", type =  IdType.ASSIGN_ID )
    private Long id;
    /** Australian Business Number (11 digits) */
    @Excel(name = "Australian Business Number (11 digits)")
    @NotNull(message = "Australian Business Number (11 digits) cannot be empty")
    private String abn;
    /** Fund name */
    @Excel(name = "Fund name")
    @NotNull(message = "Fund name cannot be empty")
    private String fundName;
    /** Unique Superannuation Identifier (20 digits) */
    @Excel(name = "Unique Superannuation Identifier (20 digits)")
    @NotNull(message = "Unique Superannuation Identifier (20 digits) cannot be empty")
    private String usi;
    /** Product name */
    @Excel(name = "Product name")
    @NotNull(message = "Product name cannot be empty")
    private String productName;
    /** Contribution restriction (Y/N) */
    @Excel(name = "Contribution restriction (Y/N)")
    @NotNull(message = "Contribution restriction (Y/N) cannot be empty")
    private String contributionRestriction;
    /** Effective date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Effective date", width = 30, dateFormat = "yyyy-MM-dd")
    @NotNull(message = "Effective date cannot be empty")
    private LocalDate fromDate;
    /** Expiry date (NULL=active) */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Expiry date (NULL=active)", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate toDate;

}
