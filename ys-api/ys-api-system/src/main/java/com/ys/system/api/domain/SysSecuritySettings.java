package com.ys.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * System security configurations entity hr_security_settings
 *
 * @author ys
 * @date 2025-07-01
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysSecuritySettings extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Enterprise Id */
    private String enterpriseId;
    /** Min password chars */
    @Excel(name = "Min password chars")
    private Integer minPasswordLength;
    /** Require special chars */
    @Excel(name = "Require special chars")
    private Boolean requireSpecialChars;
    /** Require numbers */
    @Excel(name = "Require numbers")
    private Boolean requireNumbers;
    /** Require uppercase */
    @Excel(name = "Require uppercase")
    private Boolean requireUppercase;
    /** Enable two-factor auth */
    @Excel(name = "Enable two-factor auth")
    private Boolean enable2fa;
    /** Timeout after inactivity */
    @Excel(name = "Timeout after inactivity")
    private Integer sessionTimeoutMinutes;
    /** Max failed logins before lock */
    @Excel(name = "Max failed logins before lock")
    private Integer maxFailedAttempts;

}
