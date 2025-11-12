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
 * Personnel Quota Management entity hr_quota
 *
 * @author ys
 * @date 2025-06-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrQuota extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Quota ID */
    @TableId(value = "quota_id", type = IdType.AUTO)
    private Long quotaId;
    /** Position ID */
    @Excel(name = "Position ID")
    private Long postId;
    /** Quota Target */
    @Excel(name = "Quota Target")
    private String quotaContent;
    /** Quota Personnel Number */
    @Excel(name = "Quota Personnel Number")
    private Long quotaNumber;
    /** Enterprise ID */
    @Excel(name = "Enterprise ID")
    private String enterpriseId;

    @TableField(exist = false)
    private String postName;

    @TableField(exist = false)
    private Long quotaNumberExist;

}
