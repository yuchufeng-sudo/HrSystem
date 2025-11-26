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
 * Contract template Information entity hr_contract_template
 *
 * @author ys
 * @date 2025-05-28
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrContractTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(value = "template_id", type =  IdType.AUTO )
    private Long templateId;
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String templateName;
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String templateContent;
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String status;

    /** Enterprise Id */
    @Excel(name = "Enterprise Id")
    private String enterpriseId;

}
