package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmailTemplate;

import java.util.List;

/**
 * Email Template Service Interface
 *
 * Defines business operations for email template management.
 *
 * @author ys
 * @date 2025-09-09
 */
public interface IHrEmailTemplateService extends IService<HrEmailTemplate> {

    /**
     * Query email template by ID
     *
     * @param templateId Email template primary key
     * @return Email template object
     */
    HrEmailTemplate selectHrEmailTemplateByTemplateId(Long templateId);

    /**
     * Query email templates list based on conditions
     *
     * @param hrEmailTemplate Email template query criteria
     * @return List of email templates matching the criteria
     */
    List<HrEmailTemplate> selectHrEmailTemplateList(HrEmailTemplate hrEmailTemplate);

    /**
     * Insert a new email template
     *
     * @param hrEmailTemplate Email template to be inserted
     * @return Number of rows affected
     */
    int insertHrEmailTemplate(HrEmailTemplate hrEmailTemplate);

    /**
     * Update an existing email template
     *
     * @param hrEmailTemplate Email template to be updated
     * @return Number of rows affected
     */
    int updateHrEmailTemplate(HrEmailTemplate hrEmailTemplate);

    /**
     * Send email using the specified template
     *
     * This method handles the complete email sending workflow:
     * 1. Validates the recipient email address
     * 2. Retrieves employee information for template variables
     * 3. Prepares template data with employee details
     * 4. Converts attachment paths to File objects
     * 5. Sends the email with the template content and attachments
     *
     * @param hrEmailTemplate Email template containing recipient, subject, body, and attachments
     * @throws RuntimeException if email sending fails
     */
    void sendEmailWithTemplate(HrEmailTemplate hrEmailTemplate);
}
