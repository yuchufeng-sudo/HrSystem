package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION entity
 * hr_enterprise_real_name_info
 *
 * @author ys
 * @date 2025-06-05
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrEnterpriseRealNameInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Id Primary Key */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** Pre-filled Enterprise Name */
    private String companyName;

    /** Pre-filled Unified Social Credit Code */
    private String creditCode;

    /** Pre-filled Legal Person Name */
    private String legalPersonName;

    /** Pre-filled Legal Person ID Card Number */
    private String legalPersonIdCardNo;

    /** Pre-filled Agent Name */
    private String agentName;

    /** Pre-filled Agent ID Card */
    private String agentIdCardNo;

    /**
     * Whether to Enable Alipay Quick Authentication
     * 1: Not Enabled (Default)
     * 2: Enable Alipay Enterprise Certification
     */
    private Integer authMode;

    /** USER Custom Business ID */
    private String bizId;

    /**
     * Face Recognition Method:
     * 1: Alipay (Cannot be Integrated in Alipay Mini Program)
     * 2: H5 (Default)
     * 4: WeChat Mini Program (Supports Using Webview in WeChat Mini Program
     * Face Recognition, Requires Contacting Business Staff to Enable Permission
     * Before Use)
     * 5: Alipay Mini Program (Supports Face Recognition in Alipay Mini Program,
     * Requires Contacting Business Staff to Enable Permission Before Use)
     * 9: Alipay H5 Version
     */
    private Integer faceAuthMode;

    /**
     * Whether to Display Result Page After Authentication 1. Yes (Default) 0. No
     */
    private Integer showResult;

    /** Notice Mobile Phone Number */
    private String noticeMobile;

    /** Whether to Authorize Agent to Sign (1 Yes 2 No) */
    private String signPower;

    /**
     * Enterprise USER ID
     */
    private String userId;

    /**
     * Certification Status (0: Certifying, 1: Certification SUCCESS, 2:
     * Certification Failure, 3: Pending Certification)
     */
    private Integer certificationStatus;

    /**
     * Certification INFORMATION Notice
     */
    private String massage;

    /**
     * Certification Type
     */
    private String certificationType;

    /**
     * Certification Link
     */
    private String identifyUrl;

    /**
     * Certification Serial Number
     */
    private String serialNo;
}
