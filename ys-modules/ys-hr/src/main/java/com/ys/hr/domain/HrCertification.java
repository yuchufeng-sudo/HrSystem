package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Certification information entity hr_certification
 *
 * @author ys
 * @date 2025-06-24
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrCertification extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Certification code */
    @Excel(name = "Certification code")
    private String code;
    /** Certification name */
    @Excel(name = "Certification name")
    private String name;
    /** Issuing organization */
    @Excel(name = "Issuing organization")
    private String issuingOrg;
    /** Certification type */
    @Excel(name = "Certification type")
    private String certType;
    /** Detailed description */
    @Excel(name = "Detailed description")
    private String description;
    /** Training ID */
    @Excel(name = "Training ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long trainingId;


    @TableField(exist = false)
    private Long[] employeeIds;
    private String enterpriseId;
}
