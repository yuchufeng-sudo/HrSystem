package com.ys.hr.service.email;

import com.ys.hr.domain.HrCandidateInfo;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrEnterprise;
import com.ys.hr.enums.CandidateStatus;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IHrCandidateInfoService;
import com.ys.utils.email.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Candidate Email Service
 * Handles all email sending logic related to candidates
 *
 * @author ys
 * @date 2025-11-27
 */
@Service
public class CandidateEmailService {

    private static final Logger log = LoggerFactory.getLogger(CandidateEmailService.class);

    @Resource
    private EmailUtils emailUtils;

    public static final String EMAIL_TEMPLATE_INTERVIEW = "Interview";

    public static final String EMAIL_TEMPLATE_SHORTLISTED = "Candidate-Shortlisted";

    public static final String EMAIL_TEMPLATE_HIRED = "CandidateHired";

    public static final String EMAIL_TEMPLATE_INVITE = "Invite";

    /**
     * Send shortlisted notification email
     *
     * @param candidateInfo Candidate information
     * @param company Enterprise information
     */
    public void sendShortlistedEmail(HrCandidateInfo candidateInfo, HrEnterprise company) {
        log.info("Sending shortlisted notification email: candidateId={}, email={}",
                candidateInfo.getCandidateId(), candidateInfo.getContactEmail());

        Map<String, Object> templateData = CandidateEmailBuilder.create()
                .withCompany(company)
                .withCandidateBasicInfo(candidateInfo)
                .build();

        emailUtils.sendEmailByTemplate(
                templateData,
                candidateInfo.getContactEmail(),
                EMAIL_TEMPLATE_SHORTLISTED
        );
    }

    /**
     * Send interview invitation email
     *
     * @param candidateInfo Candidate information
     * @param company Enterprise information
     */
    public void sendInterviewEmail(HrCandidateInfo candidateInfo, HrEnterprise company) {
        if (candidateInfo.getContactEmail() == null) {
            log.warn("Candidate email is null, skipping interview email: candidateId={}",
                    candidateInfo.getCandidateId());
            return;
        }

        log.info("Sending interview invitation email: candidateId={}, email={}",
                candidateInfo.getCandidateId(), candidateInfo.getContactEmail());

        Map<String, Object> templateData = CandidateEmailBuilder.create()
                .withCompany(company)
                .withCandidateBasicInfo(candidateInfo)
                .withInterviewInfo(candidateInfo.getInterviewTime(), candidateInfo.getInterviewLocation())
                .build();

        emailUtils.sendEmailByTemplate(
                templateData,
                candidateInfo.getContactEmail(),
                EMAIL_TEMPLATE_INTERVIEW
        );
    }

    /**
     * Send hired notification email
     *
     * @param candidateInfo Candidate information
     * @param company Enterprise information
     * @param hrEmployee HR employee information
     */
    public void sendHiredEmail(HrCandidateInfo candidateInfo, HrEnterprise company, HrEmployees hrEmployee) {
        log.info("Sending hired notification email: candidateId={}, email={}",
                candidateInfo.getCandidateId(), candidateInfo.getContactEmail());

        Map<String, Object> templateData = CandidateEmailBuilder.create()
                .withCompany(company)
                .withHrEmployee(hrEmployee)
                .withCandidateBasicInfo(candidateInfo)
                .build();

        emailUtils.sendEmailByTemplate(
                templateData,
                candidateInfo.getContactEmail(),
                EMAIL_TEMPLATE_HIRED
        );
    }

    /**
     * Send invite email
     *
     * @param candidateInfo Candidate information
     * @param company Enterprise information
     */
    public void sendInviteEmail(HrCandidateInfo candidateInfo, HrEnterprise company) {
        log.info("Sending invite email: candidateId={}, email={}",
                candidateInfo.getCandidateId(), candidateInfo.getContactEmail());

        Map<String, Object> templateData = CandidateEmailBuilder.create()
                .withCompany(company)
                .with("FirstName", candidateInfo.getCandidateName())
                .withInviteUrl(candidateInfo.getInviteUrl())
                .build();

        emailUtils.sendEmailByTemplate(
                templateData,
                candidateInfo.getContactEmail(),
                EMAIL_TEMPLATE_INVITE
        );
    }

    /**
     * Send appropriate email based on candidate status
     *
     * @param candidateInfo Candidate information
     * @param company Enterprise information
     * @param hrEmployee HR employee information
     * @param userId User ID
     */
    public void sendEmailByStatus(HrCandidateInfo candidateInfo, HrEnterprise company,
                                  HrEmployees hrEmployee, Long userId) {
        String statusCode = candidateInfo.getCandidateStatus();

        if (CandidateStatus.SHORTLISTED.equalsCode(statusCode)) {
            sendShortlistedEmail(candidateInfo, company);
        } else if (CandidateStatus.INTERVIEW.equalsCode(statusCode)) {
            sendInterviewEmail(candidateInfo, company);
        } else if (CandidateStatus.HIRED.equalsCode(statusCode)) {
            // Only send hired notification when explicitly requested
            if (candidateInfo.getIsEmail() != null && candidateInfo.getIsEmail()) {
                sendHiredEmail(candidateInfo, company, hrEmployee);
            }
        }
    }
}
