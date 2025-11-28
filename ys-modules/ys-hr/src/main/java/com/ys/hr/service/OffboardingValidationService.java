package com.ys.hr.service;

import com.ys.common.core.exception.ServiceException;
import com.ys.hr.domain.HrEmployees;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Offboarding Process Input Validation Service
 */
@Slf4j
@Service
public class OffboardingValidationService {

    // Email validation regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Maximum resignation reason length
    private static final int MAX_REASON_LENGTH = 1000;

    /**
     * Validate resign request input parameters
     *
     * @param employee Employee resignation information
     * @throws ServiceException if validation fails
     */
    public void validateResignInput(HrEmployees employee) {
        log.debug("Validating resign input for employee: {}", employee.getEmployeeId());

        // 1. Validate employee ID
        if (employee.getEmployeeId() == null || employee.getEmployeeId() <= 0) {
            throw new ServiceException("Invalid employee ID");
        }

        // 2. Validate resignation date
        if (employee.getResignationDate() == null) {
            throw new ServiceException("Resignation date is required");
        }

        // 3. Validate resignation reason
        validateResignationReason(employee.getResignationReason());

        // 4. Validate handling HR user ID
        if (employee.getResignationHrUserId() == null) {
            throw new ServiceException("Resignation HR user is required");
        }

        // 5. Validate employee email (if provided)
        if (employee.getEmail() != null) {
            validateEmail(employee.getEmail());
        }

        log.debug("Resign input validation passed for employee: {}", employee.getEmployeeId());
    }

    /**
     * Validate email format and prevent injection
     *
     * @param email Email address
     * @throws ServiceException if email is invalid
     */
    public void validateEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new ServiceException("Email address cannot be empty");
        }

        // Trim whitespace
        email = email.trim();

        // Length check (prevent abnormally long emails)
        if (email.length() > 320) { // RFC 5321 standard max length
            throw new ServiceException("Email address is too long (max 320 characters)");
        }

        // Format validation
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            log.warn("Invalid email format detected: {}", email);
            throw new ServiceException("Invalid email format");
        }

        // Prevent email injection attacks - check special characters
        if (email.contains("\n") || email.contains("\r") ||
                email.contains("%0a") || email.contains("%0d")) {
            log.error("Email injection attempt detected: {}", email);
            throw new ServiceException("Invalid email address - contains forbidden characters");
        }

        log.debug("Email validation passed: {}", email);
    }

    /**
     * Validate resignation reason
     */
    private void validateResignationReason(String reason) {
        if (!StringUtils.hasText(reason)) {
            // Resignation reason is optional, so empty is not an error
            return;
        }

        if (reason.length() > MAX_REASON_LENGTH) {
            throw new ServiceException("Resignation reason is too long " +
                    "(max " + MAX_REASON_LENGTH + " characters)");
        }

        // Check for potential SQL injection patterns (basic protection)
        String lowerReason = reason.toLowerCase();
        if (lowerReason.contains("drop table") ||
                lowerReason.contains("delete from") ||
                lowerReason.contains("insert into") ||
                lowerReason.contains("update ")) {
            log.warn("Suspicious resignation reason detected, potential SQL injection attempt");
            throw new ServiceException("Invalid resignation reason content");
        }
    }
}
