package com.ys.hr.domain;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Background Check Order entity hr_background_check_order
 *
 * @author ys
 * @date 2025-06-25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrBackgroundCheckOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary Key */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** Employee/Candidate ID */
    @Excel(name = "Employee/Candidate ID")
    private Long userId;
    /** Enterprise ID */
    @Excel(name = "Enterprise ID")
    private String enterpriseId;
    /** Candidate ID (Platform) */
    @Excel(name = "Candidate ID")
    private String candidateId;
    /** Work experience */
    @Excel(name = "Work experience")
    private String jobLocationList;
    /** Sender's email */
    @Excel(name = "Sender's email")
    private String requestor;
    /** Region */
    @Excel(name = "Region")
    private String region2;
    /** Other product types */
    @Excel(name = "Other product types")
    private String additionalProductTypeList;
    /** Company List */
    @Excel(name = "Company List")
    private String complianceTokenIst;

    /**
     * Describes the package of searches to run in the order.
     * We will provide the correct package type for this parameter if you move forward with the API.
     */
    private String packageType;

    private String status;

    private String orderId;

    @TableField(exist = false)
    private JSONObject jobLocation;

    @TableField(exist = false)
    private JSONArray complianceTokens;

    @TableField(exist = false)
    private String userType;

    @TableField(exist = false)
    private String firstName;

    @TableField(exist = false)
    private String lastName;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;

    @TableField(exist = false)
    private String email;

    @TableField(exist = false)
    private String phone;
}
