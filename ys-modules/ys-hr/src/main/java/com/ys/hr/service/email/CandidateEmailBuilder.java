package com.ys.hr.service.email;

import com.ys.hr.domain.HrCandidateInfo;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrEnterprise;
import org.apache.commons.lang3.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Candidate Email Builder
 * Responsible for building template data for various candidate-related emails
 *
 * @author ys
 * @date 2025-11-27
 */
public class CandidateEmailBuilder {

    private static final String DEFAULT_VALUE = "No details yet";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";

    private final Map<String, Object> templateData;

    private CandidateEmailBuilder() {
        this.templateData = new HashMap<>();
    }

    /**
     * Create a new email builder
     *
     * @return Email builder instance
     */
    public static CandidateEmailBuilder create() {
        return new CandidateEmailBuilder();
    }

    /**
     * Set company information
     *
     * @param company Enterprise information
     * @return Builder itself
     */
    public CandidateEmailBuilder withCompany(HrEnterprise company) {
        if (ObjectUtils.isNotEmpty(company)) {
            templateData.put("CompanyName", company.getEnterpriseName());
            if (company.getContactEmail() != null) {
                templateData.put("SupportEmail", company.getContactEmail());
            }
        } else {
            templateData.put("CompanyName", DEFAULT_VALUE);
        }
        return this;
    }

    /**
     * Set HR information
     *
     * @param hrEmployee HR employee information
     * @return Builder itself
     */
    public CandidateEmailBuilder withHrEmployee(HrEmployees hrEmployee) {
        if (ObjectUtils.isNotEmpty(hrEmployee)) {
            templateData.put("HrName", hrEmployee.getFullName());
            templateData.put("HrEmail", hrEmployee.getEmail());
        } else {
            templateData.put("HrName", DEFAULT_VALUE);
            templateData.put("HrEmail", DEFAULT_VALUE);
        }
        return this;
    }

    /**
     * Set candidate basic information
     *
     * @param candidateInfo Candidate information
     * @return Builder itself
     */
    public CandidateEmailBuilder withCandidateBasicInfo(HrCandidateInfo candidateInfo) {
        templateData.put("FirstName", candidateInfo.getCandidateName());

        if (ObjectUtils.isNotEmpty(candidateInfo.getPostName())) {
            templateData.put("JobTitle", candidateInfo.getPostName());
        } else {
            templateData.put("JobTitle", DEFAULT_VALUE);
        }

        return this;
    }

    /**
     * Set interview information
     *
     * @param interviewTime Interview time
     * @param interviewLocation Interview location
     * @return Builder itself
     */
    public CandidateEmailBuilder withInterviewInfo(Date interviewTime, String interviewLocation) {
        if (interviewTime != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
            templateData.put("InterviewDate", dateFormat.format(interviewTime));
            templateData.put("InterviewTime", timeFormat.format(interviewTime));
        }

        if (interviewLocation != null) {
            templateData.put("InterviewLocation", interviewLocation);
        }

        return this;
    }

    /**
     * Set invite URL
     *
     * @param inviteUrl Invite URL
     * @return Builder itself
     */
    public CandidateEmailBuilder withInviteUrl(String inviteUrl) {
        templateData.put("InviteUrl", inviteUrl);
        templateData.put("HrisToolName", "Shiftcare HR");
        return this;
    }

    /**
     * Add custom key-value pair
     *
     * @param key Key
     * @param value Value
     * @return Builder itself
     */
    public CandidateEmailBuilder with(String key, Object value) {
        templateData.put(key, value);
        return this;
    }

    /**
     * Build and return template data map
     *
     * @return Template data
     */
    public Map<String, Object> build() {
        return new HashMap<>(templateData);
    }
}
