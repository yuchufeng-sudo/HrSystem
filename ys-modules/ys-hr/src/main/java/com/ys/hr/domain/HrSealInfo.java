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
 * SEAL INFORMATION entity hr_seal_info
 *
 * @author ys
 * @date 2025-06-05
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrSealInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** idPrimary Key */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** USER Unique Identifier */
    @Excel(name = "USER Unique Identifier")
    private String account;

    /** Seal Unique ID */
    @Excel(name = "Seal Unique ID")
    private String sealNo;

    /** Seal Link */
    @Excel(name = "Seal Link")
    private String sealRul;

    /** Whether Default 1 - Yes, 0 - No (Default) */
    private Integer isDefault;

    /** Seal Name */
    @Excel(name = "Seal Name")
    private String sealName;

    /**
     * Whether to Stamp According to Original Picture Size 1 - Yes, 0 - No (Default)
     */
    private Integer userOriginalImg;

}
