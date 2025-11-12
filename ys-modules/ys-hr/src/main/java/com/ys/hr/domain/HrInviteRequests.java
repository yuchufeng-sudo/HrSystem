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
 * INVITATION LINK REQUEST HANDLING Object hr_invite_requests
 *
 * @author ys
 * @date 2025-05-21
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrInviteRequests extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Request Record Unique Identifier */
    @TableId(value = "request_id", type = IdType.AUTO)
    private Long requestId;
    /** Applicant Full Name */
    @Excel(name = "Applicant Full Name")
    private String fullName;
    /** Applicant EMAIL (EMAIL ADDRESS) */
    @Excel(name = "Applicant EMAIL (EMAIL ADDRESS)")
    private String email;
    /** MOBILE PHONE AREA CODE */
    @Excel(name = "MOBILE PHONE AREA CODE")
    private String phoneCode;
    /** CONTACT NUMBER */
    @Excel(name = "CONTACT NUMBER")
    private String contactPhone;
    /** Address */
    private String location;
    private String country;
    private String address1;
    private String address2;
    private String city;
    private String province;
    private String postCode;
    /** Login USER Name */
    private String username;
    /** Requested Permission Level */
    private String accessLevel;
    /** Request Processing Status */
    private String status;
    private String firstName;
    private String password;
    private String enterpriseId;
    @TableField(exist = false)
    private String inviteCode;
}
