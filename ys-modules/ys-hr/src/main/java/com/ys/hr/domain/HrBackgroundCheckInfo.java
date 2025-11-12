package com.ys.hr.domain;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Background check personnel information entity hr_background_check_info
 *
 * @author ys
 * @date 2025-06-25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrBackgroundCheckInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary Key */
    @TableId(value = "id", type = IdType.AUTO)
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
    /** Staff Type: 1 Employee; 2 Candidate */
    @Excel(name = "Staff Type: 1 Employee; 2 Candidate")
    private String userType;
    /** First Name */
    @Excel(name = "First Name")
    private String firstName;

    /** Last Name */
    @Excel(name = "Last Name")
    private String lastName;

    /** Middle Name */
    @Excel(name = "Middle Name")
    private String middleName;

    /** Suffix */
    @Excel(name = "Suffix")
    private String suffix;

    /** Date of Birth */
    @Excel(name = "Date of Birth")
    private Date birthdate;

    /** Social Security Number */
    @Excel(name = "Social Security Number")
    private String ssn;

    /** Email */
    @Excel(name = "Email")
    private String email;

    /** Mobile Phone */
    @Excel(name = "Mobile Phone")
    private String phone;

    /** Street Address */
    @Excel(name = "Street Address")
    private String address;

    /** City */
    @Excel(name = "City")
    private String city;

    /** Region */
    @Excel(name = "Region")
    private String region;

    /** Country */
    @Excel(name = "Country")
    private String country;

    /** Postal Code */
    @Excel(name = "Postal Code")
    private String postalCode;

    /** Aliases List */
    @Excel(name = "Aliases List")
    private String aliasesList;

    /** Education List */
    @Excel(name = "Education List")
    private String educationList;

    /** Employment History List */
    @Excel(name = "Employment History List")
    private String employmentList;

    /** Conviction List */
    @Excel(name = "Conviction List")
    private String convictionList;

    /** Address History List */
    @Excel(name = "Address History List")
    private String addressHistoryList;

    /**
     * Education Experience List
     */
    @TableField(exist = false)
    private JSONArray educations;

    /**
     * Employment History
     */
    @TableField(exist = false)
    private JSONArray employments;

    @TableField(exist = false)
    private boolean prevEmployed = true;

    @TableField(exist = false)
    private boolean convicted = false;

    @TableField(exist = false)
    private String dateOfBirth;

}
