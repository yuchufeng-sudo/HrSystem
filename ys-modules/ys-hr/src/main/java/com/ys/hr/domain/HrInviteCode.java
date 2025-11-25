package com.ys.hr.domain;

import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Invitation link request Object hr_invite_requests
 *
 * @author ys
 * @date 2025-05-21
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrInviteCode extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String code;
    private String enterpriseId;
    private Integer status;
}
