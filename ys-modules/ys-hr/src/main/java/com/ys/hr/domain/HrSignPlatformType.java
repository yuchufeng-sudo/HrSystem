package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Electronic signature platform type entity hr_sign_platform_type
 *
 * @author ys
 * @date 2025-08-08
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrSignPlatformType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;

    private String typeName;

    private Integer typeValue;

    private String apiUrl;

    private Integer isDisabled;

}
