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

/**
 * Electronic signature platform configuration entity hr_sign_config
 *
 * @author ys
 * @date 2025-06-12
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrSignConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary Key */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** Platform Type */
    @Excel(name = "Platform Type")
    private Integer signType;
    /** Configuration Content */
    @Excel(name = "Configuration Content")
    private String signConfig;
    /** Enable Status: 0 Not Enabled, 1 Enabled */
    @Excel(name = "Enable Status: 0 Not Enabled, 1 Enabled")
    private String status;

    /**
     * Enterprise Id
     */
    private String enterpriseId;

    @TableField(exist = false)
    private String platformName;

    @TableField(exist = false)
    private String apiUrl;

    @TableField(exist = false)
    private Integer typeValue;

}
