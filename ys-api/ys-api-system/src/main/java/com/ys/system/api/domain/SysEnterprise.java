package com.ys.system.api.domain;

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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * ENTERPRISE MANAGEMENT Object sys_enterprise
 *
 * @author ys
 * @date 2025-05-15
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysEnterprise extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /** Company Logo */
    @Excel(name = "Company Logo")
    @NotNull(message = "Company Logo cannot be empty")
    @Size(max = 500, message = "Company Logo length cannot exceed 500 characters")
    private String logoUrl;
    /** Industry Type */
    @Excel(name = "Industry Type")
    @NotNull(message = "Industry Type cannot be empty")
    @Size(max = 100, message = "Industry Type length cannot exceed 100 characters")
    private String industryType;
    /** Company Name */
    @Excel(name = "Company Name")
    @NotNull(message = "Company Name cannot be empty")
    @Size(max = 200, message = "Company Name length cannot exceed 200 characters")
    private String enterpriseName;
    /** Country */
    @Excel(name = "Country")
    @NotNull(message = "Country cannot be empty")
    @Size(max = 100, message = "Country length cannot exceed 100 characters")
    private String country;
    private String address1;
    private String address2;
    private String city;
    private String province;
    private String postCode;
    /** Time Zone */
    @Excel(name = "Time Zone")
    @NotNull(message = "Time Zone cannot be empty")
    @Size(max = 50, message = "Time Zone length cannot exceed 50 characters")
    private String timeZone;
    /** Contact Email */
    @Excel(name = "Contact Email")
    @NotNull(message = "Contact Email cannot be empty")
    @Size(max = 255, message = "Contact Email length cannot exceed 255 characters")
    private String contactEmail;
    /** Currency */
    @Excel(name = "Currency")
    @NotNull(message = "Currency cannot be empty")
    @Size(max = 10, message = "Currency length cannot exceed 10 characters")
    private String currency;
    /** Contact Phone */
    @Excel(name = "Contact Phone")
    @NotNull(message = "Contact Phone cannot be empty")
    @Size(max = 50, message = "Contact Phone length cannot exceed 50 characters")
    private String contactPhone;
    private String phoneCode;

    private String status;

    private Long isPlan;

    private String planId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    private Long planTimeNums;

    private String planOrderId;

    @TableField(exist = false)
    private Long userCount;

    @TableField(exist = false)
    private String applicationId;



    @TableField(exist = false)
    private Long level;

    @TableField(exist = false)
    private Long orderType;

    @TableField(exist = false)
    private Long isFreeOrder;

    @TableField(exist = false)
    private String jobnumber;

    @TableField(exist = false)
    private String fullName;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String password;

    @TableField(exist = false)
    private String orderById;

    @TableField(exist = false)
    private String loginUrl;
}
