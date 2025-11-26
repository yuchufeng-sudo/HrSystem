package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
import com.ys.hr.domain.HrEmailTemplate;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.mapper.HrEmailTemplateMapper;
import com.ys.hr.service.IHrEmailTemplateService;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IFileConversionService;
import com.ys.utils.email.EmailUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Email Template Service Implementation
 *
 * Implements business logic for email template management and email sending.
 * This service orchestrates operations between employee lookup, file conversion,
 * and email delivery systems.
 *
 * @author ys
 * @date 2025-09-09
 */
@Service
public class HrEmailTemplateServiceImpl extends ServiceImpl<HrEmailTemplateMapper, HrEmailTemplate>
        implements IHrEmailTemplateService {

    private static final Logger log = LoggerFactory.getLogger(HrEmailTemplateServiceImpl.class);

    @Resource
    private IHrEmployeesService hrEmployeesService;

    @Resource
    private EmailUtils emailUtils;

    @Resource
    private IFileConversionService fileConversionService;

    /**
     * Query email template by ID
     *
     * @param templateId Email template primary key
     * @return Email template object
     */
    @Override
    public HrEmailTemplate selectHrEmailTemplateByTemplateId(Long templateId) {
        return baseMapper.selectHrEmailTemplateByTemplateId(templateId);
    }

    /**
     * Query email templates list based on conditions
     *
     * @param hrEmailTemplate Email template query criteria
     * @return List of email templates matching the criteria
     */
    @Override
    public List<HrEmailTemplate> selectHrEmailTemplateList(HrEmailTemplate hrEmailTemplate) {
        return baseMapper.selectHrEmailTemplateList(hrEmailTemplate);
    }

    /**
     * Insert a new email template
     *
     * @param hrEmailTemplate Email template to be inserted
     * @return Number of rows affected
     */
    @Override
    public int insertHrEmailTemplate(HrEmailTemplate hrEmailTemplate) {
        hrEmailTemplate.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrEmailTemplate);
    }

    /**
     * Update an existing email template
     *
     * @param hrEmailTemplate Email template to be updated
     * @return Number of rows affected
     */
    @Override
    public int updateHrEmailTemplate(HrEmailTemplate hrEmailTemplate) {
        hrEmailTemplate.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrEmailTemplate);
    }

    /**
     * Send email using the specified template
     *
     * This method implements the complete email sending workflow:
     * 1. Validates that recipient email is provided
     * 2. Retrieves employee information based on recipient email
     * 3. Prepares template variables with employee data
     * 4. Converts attachment paths to File objects (handles both local and URL paths)
     * 5. Sends the email through the email utility
     *
     * @param hrEmailTemplate Email template containing recipient, subject, body, and attachments
     * @throws ServiceException if recipient email is not provided
     * @throws RuntimeException if email sending fails
     */
    @Override
    public void sendEmailWithTemplate(HrEmailTemplate hrEmailTemplate) {
        log.info("Starting email send process for template: {}", hrEmailTemplate.getTemplateName());

        // Validate recipient email is provided
        if (ObjectUtils.isEmpty(hrEmailTemplate.getSendTo())) {
            log.error("Recipient email address is required but not provided");
            throw new ServiceException("Recipient email address is required");
        }

        try {
            // Retrieve employee information for template variables
            HrEmployees employee = getEmployeeByEmail(hrEmailTemplate.getSendTo());

            // Prepare template data with employee information
            Map<String, Object> templateData = prepareTemplateData(employee);

            // Convert attachment paths to File objects
            List<File> attachmentFiles = fileConversionService.convertToFileList(
                    hrEmailTemplate.getAttachments()
            );

            // Prepare recipient arrays
            String[] recipients = new String[]{hrEmailTemplate.getSendTo()};
            // Could be extended to support CC
            String[] ccRecipients = null;
            // Could be extended to support BCC
            String[] bccRecipients = null;

            // Send the email
            emailUtils.sendEmailByEmailTemplate(
                    templateData,
                    recipients,
                    hrEmailTemplate.getTemplateBody(),
                    templateData,
                    hrEmailTemplate.getTemplateSubject(),
                    ccRecipients,
                    bccRecipients,
                    attachmentFiles
            );

            log.info("Email sent successfully to: {}", hrEmailTemplate.getSendTo());

        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}",
                    hrEmailTemplate.getSendTo(), e.getMessage(), e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve employee information by email address
     *
     * @param email The email address to search for
     * @return The first matching employee, or null if not found
     */
    private HrEmployees getEmployeeByEmail(String email) {
        HrEmployees queryParam = new HrEmployees();
        queryParam.setEmail(email);

        List<HrEmployees> employees = hrEmployeesService.selectHrEmployeesList(queryParam);

        if (ObjectUtils.isNotEmpty(employees)) {
            log.debug("Found employee with email: {}", email);
            return employees.get(0);
        }

        log.warn("No employee found with email: {}", email);
        return null;
    }

    /**
     * Prepare template data map with employee information
     *
     * Creates a map of variable names to values that will be used
     * to populate the email template placeholders.
     *
     * @param employee The employee object containing personal information
     * @return Map of template variable names to their values
     */
    private Map<String, Object> prepareTemplateData(HrEmployees employee) {
        Map<String, Object> templateData = new HashMap<>();

        if (employee != null && ObjectUtils.isNotEmpty(employee.getFullName())) {
            templateData.put("name", employee.getFullName());
            log.debug("Added employee name to template data: {}", employee.getFullName());
        } else {
            templateData.put("name", "No details yet");
            log.debug("No employee details available, using default name");
        }

        // Additional template variables can be added here
        // Example:
        // templateData.put("position", employee.getPosition());
        // templateData.put("department", employee.getDepartment());

        return templateData;
    }
}
