package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * CANDIDATE  INFORMATIONObject hr_candidate_info
 *
 * @author ys
 * @date 2025-05-20
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrCandidateInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * CANDIDATE  ID
     */
    @TableId(value = "candidate_id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long candidateId;
    /**
     * CANDIDATE Name
     */
    @Size(max = 50, message = "Candidate name cannot exceed 50 characters")
    @NotNull()
    private String candidateName;
    /**
     * Age
     */
    private Long age;

    /**
     * DATE OF BIRTH
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    /**
     * Gender
     */
    @Size(max = 1, message = "Gender must be 1 character")
    private String gender;

    /**
     * EMAIL (EMAIL ADDRESS)
     */
    @Size(max = 50)
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Email(message = "Please enter a valid email address")
    private String contactEmail;

    /**
     * Mobile Phone
     */
    @Size(max = 15)
    @Size(max = 255, message = "Phone number cannot exceed 255 characters")
    @Pattern(regexp = "^[0-9]{8,15}$",message = "Please enter a valid phone number (8–15 digits, no symbols or spaces)")
    private String phone;

    /**
     * Job Application Status
     */
    @Size(max = 1, message = "Job status must be 1 character")
    private String jobStatus;

    /**
     * Source Platform
     */
    @Excel(name = "Source Platform")
    private String source;

    /**
     * CANDIDATE Status
     */
    @Excel(name = "CANDIDATE Status")
    private String candidateStatus;

    /**
     * Profile Photo
     */
    @Excel(name = "Profile Photo")
    @Size(max = 255, message = "Avatar URL cannot exceed 255 characters")
    private String avatar;

    /**
     * Job INFORMATION
     */
    private Long jobInformation;

    /**
     * Job City
     */
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    /**
     * Job Expectations
     */
    @Size(max = 255, message = "Job expectations cannot exceed 255 characters")
    private String jobExpectations;

    /**
     * Personal Advantages
     */
    @Size(max = 255, message = "Personal strengths cannot exceed 255 characters")
    private String personalStrengths;

    /**
     * Finally Start Work TIME
     */
    @Excel(name = "Finally Start Work TIME")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastWorkTime;

    /**
     * Industry Type
     */
    @Excel(name = "Industry Type")
    @Size(max = 1, message = "Industry type must be 1 character")
    private String industryType;

    /**
     * Other Skills
     */
    @Excel(name = "Other Skills")
    @Size(max = 255, message = "Other skills cannot exceed 255 characters")
    private String otherSkills;

    /**
     * Hobbies
     */
    @Excel(name = "Hobbies")
    @Size(max = 255, message = "Hobbies cannot exceed 255 characters")
    private String hobbies;

    /**
     * Willing to Travel
     */
    @Excel(name = "Willing to Travel")
    @Size(max = 1, message = "Business trip indicator must be 1 character")
    private String businessTrip;

    /**
     * Willing to Overtime
     */
    @Excel(name = "Willing to Overtime")
    @Size(max = 1, message = "Overtime indicator must be 1 character")
    private String isOvertime;

    /**
     * Enterprise ID
     */
    @Excel(name = "Enterprise Number")
    @Size(max = 50, message = "Enterprise ID cannot exceed 50 characters")
    private String enterpriseId;

    /**
     * Tags
     */
    @Size(max = 2000, message = "Tags cannot exceed 2000 characters")
    private String tags;

    /**
     * MOBILE PHONE AREA CODE
     */
    @Excel(name = "MOBILE PHONE AREA CODE")
    @Size(max = 20, message = "Phone code cannot exceed 20 characters")
    private String phoneCode;

    /**
     * Shortlist Reason
     */
    @Size(max = 255, message = "Shortlist reason cannot exceed 255 characters")
    private String shortlistReason;

    /**
     * Interview Type
     */
    @Size(max = 255, message = "Interview type cannot exceed 255 characters")
    private String interviewType;

    /**
     * Interview Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date interviewTime;

    /**
     * Interview Location
     */
    @Size(max = 255, message = "Interview location cannot exceed 255 characters")
    private String interviewLocation;

    /**
     * Rejected Reason
     */
    @Size(max = 255, message = "Rejected reason cannot exceed 255 characters")
    private String rejectedReason;

    /**
     * Probation Start Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date probationStartTime;

    /**
     * Probation End Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date probationEndTime;

    /**
     * Probation Salary
     */
    private Long probationSalary;

    /**
     * Basic Salary
     */
    @Excel(name = "Basic Salary")
    private Long baseSalary;

    /**
     * Account Holder Name
     */
    @Excel(name = "Account Holder Name")
    @Size(max = 255, message = "Account holder name cannot exceed 255 characters")
    private String bankName;

    /**
     * Bank Account Number
     */
    @Excel(name = "Bank Account Number")
    @Size(max = 50, message = "Bank account number cannot exceed 50 characters")
    private String bankNumber;

    /**
     * Bank of Deposit
     */
    @Excel(name = "Bank of Deposit")
    @Size(max = 255, message = "Bank of deposit cannot exceed 255 characters")
    private String openingBank;

    /**
     * Social Security Cost
     */
    @Excel(name = "Social Security Cost")
    private Double socialSecurityCost;

    /**
     * Commercial Insurance Cost
     */
    @Excel(name = "Commercial Insurance Cost")
    private Double comInsuIdCost;

    /**
     * SWIFT/BSB
     */
    @Excel(name = "SWIFT/BSB")
    @Size(max = 20, message = "SWIFT/BSB cannot exceed 20 characters")
    private String swiftOrBsb;

    /**
     * Employment Type
     */
    @Size(max = 255, message = "Employment type cannot exceed 255 characters")
    private String employmentType;

    /**
     * Housing Fund Cost
     */
    @Excel(name = "Housing Fund Cost")
    private Double accuFundIdCost;

    private String filesJson;

    private String postcode;

    private String jobListingsId;

    private Date shortlistedTime;

    private Date interviewingTime;

    private Date hiredTime;

    private Date rejectedTime;

    @TableField(exist = false)
    private List<HrDocument> filesValue;

    @Excel(name = "Position")
    @TableField(exist = false)
    private String postName;
    @TableField(exist = false)
    private Boolean isEmail;
    @TableField(exist = false)
    private String inviteUrl;
    @TableField(exist = false)
    private String statusKey;
    @TableField(exist = false)
    private Integer count;
    @TableField(exist = false)
    private String startTime;
    @TableField(exist = false)
    private String endTime;
    @TableField(exist = false)
    private List<HrQuestionAnswer> answers;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("candidateId", getCandidateId())
                .append("candidateName", getCandidateName())
                .append("age", getAge())
                .append("birthDate", getBirthDate())
                .append("gender", getGender())
                .append("contactEmail", getContactEmail())
                .append("phone", getPhone())
                .append("jobStatus", getJobStatus())
                .append("source", getSource())
                .append("candidateStatus", getCandidateStatus())
                .append("avatar", getAvatar())
                .append("jobInformation", getJobInformation())
                .append("address", getAddress())
                .append("jobExpectations", getJobExpectations())
                .append("personalStrengths", getPersonalStrengths())
                .append("lastWorkTime", getLastWorkTime())
                .append("industryType", getIndustryType())
                .append("otherSkills", getOtherSkills())
                .append("hobbies", getHobbies())
                .append("businessTrip", getBusinessTrip())
                .append("isOvertime", getIsOvertime())
                .toString();
    }
}
