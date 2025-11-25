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
 *  Contract expiration entity hr_maturity_contract
 *
 * @author ys
 * @date 2025-06-20
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrMaturityContract extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Contract id */
    @Excel(name = "Contract id")
    private String contractId;
    /** Whether to renew the contract: 1 No 2 Yes */
    @Excel(name = "Whether to renew the contract: 1 No 2 Yes")
    private String isRenewal;
    /** Enterprise ID */
    @Excel(name = "Enterprise ID")
    private String enterpriseId;

    @TableField(exist = false)
    private String companyName;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String contractName;

    @TableField(exist = false)
    private String post;

    @TableField(exist = false)
    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date expirationDate;

    @TableField(exist = false)
    private Long templateId;

    @TableField(exist = false)
    private String signType;
}
