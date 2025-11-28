package com.ys.hr.service;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.enums.EmployeeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Offboarding Process Security Validation Service
 * Responsible for security checks of all offboarding-related operations
 */
@Slf4j
@Service
public class OffboardingSecurityService {

    /**
     * Validate if user has permission to resign the specified employee
     *
     * @param employee The employee to resign
     * @throws ServiceException if validation fails
     */
    public void validateResignPermission(HrEmployees employee) {
        // 1. Validate employee object is not null
        if (employee == null) {
            log.error("Validation failed: Employee object is null");
            throw new ServiceException("Employee information cannot be null");
        }

        // 2. Validate current user login status
        String currentEnterpriseId = SecurityUtils.getUserEnterpriseId();
        if (currentEnterpriseId == null) {
            log.error("Validation failed: User not authenticated");
            throw new ServiceException("Authentication required");
        }

        // 3. Validate employee belongs to current user's enterprise
        if (!currentEnterpriseId.equals(employee.getEnterpriseId())) {
            log.warn("Security violation: User {} attempted to resign employee {} from different enterprise. " +
                            "User enterprise: {}, Employee enterprise: {}",
                    SecurityUtils.getUserId(), employee.getEmployeeId(),
                    currentEnterpriseId, employee.getEnterpriseId());
            throw new ServiceException("You do not have permission to resign this employee");
        }

        // 4. Validate employee status
        if (!EmployeeStatus.ACTIVE.equalsCode(employee.getStatus())) {
            log.warn("Invalid operation: Attempted to resign employee {} with status {}",
                    employee.getEmployeeId(), employee.getStatus());
            throw new ServiceException("Only active employees can be resigned. Current status: " +
                    getStatusDescription(employee.getStatus()));
        }

        // 5. Validate employee is not self
        if (employee.getUserId().equals(SecurityUtils.getUserId())) {
            log.warn("Self-resignation attempt blocked: User {} attempted to resign themselves",
                    SecurityUtils.getUserId());
            throw new ServiceException("You cannot resign yourself. Please contact HR or management.");
        }

        log.info("Resign permission validated successfully for employee: {}", employee.getEmployeeId());
    }

    /**
     * Validate resign cancellation permission
     *
     * @param employee The employee whose resignation to cancel
     * @throws ServiceException if validation fails
     */
    public void validateResignCancelPermission(HrEmployees employee) {
        // 1. Basic validation
        if (employee == null) {
            throw new ServiceException("Employee information cannot be null");
        }

        String currentEnterpriseId = SecurityUtils.getUserEnterpriseId();

        // 2. Enterprise ID validation
        if (!currentEnterpriseId.equals(employee.getEnterpriseId())) {
            log.warn("Security violation: User {} attempted to cancel resignation for employee {} " +
                            "from different enterprise",
                    SecurityUtils.getUserId(), employee.getEmployeeId());
            throw new ServiceException("You do not have permission to cancel this resignation");
        }

        // 3. Validate employee is in offboarding status
        if (!EmployeeStatus.OFFBOARDING.equalsCode(employee.getStatus())) {
            log.warn("Invalid operation: Attempted to cancel resignation for employee {} " +
                            "with status {}",
                    employee.getEmployeeId(), employee.getStatus());
            throw new ServiceException("Only employees in offboarding status can have " +
                    "their resignation cancelled");
        }

        log.info("Resign cancel permission validated for employee: {}", employee.getEmployeeId());
    }

    /**
     * Validate employee restore permission
     *
     * @param employee The employee to restore
     * @throws ServiceException if validation fails
     */
    public void validateRestorePermission(HrEmployees employee) {
        if (employee == null) {
            throw new ServiceException("Employee information cannot be null");
        }

        String currentEnterpriseId = SecurityUtils.getUserEnterpriseId();

        if (!currentEnterpriseId.equals(employee.getEnterpriseId())) {
            log.warn("Security violation: User {} attempted to restore employee {} " +
                            "from different enterprise",
                    SecurityUtils.getUserId(), employee.getEmployeeId());
            throw new ServiceException("You do not have permission to restore this employee");
        }

        // Only resigned employees can be restored
        if (!EmployeeStatus.RESIGNED.equalsCode(employee.getStatus())) {
            throw new ServiceException("Only resigned employees can be restored");
        }

        log.info("Restore permission validated for employee: {}", employee.getEmployeeId());
    }

    /**
     * Get status description
     */
    private String getStatusDescription(String statusCode) {
        if (EmployeeStatus.ACTIVE.equalsCode(statusCode)) {
            return EmployeeStatus.ACTIVE.getDescription();
        } else if (EmployeeStatus.RESIGNED.equalsCode(statusCode)) {
            return EmployeeStatus.RESIGNED.getDescription();
        } else if (EmployeeStatus.OFFBOARDING.equalsCode(statusCode)) {
            return EmployeeStatus.OFFBOARDING.getDescription();
        }
        return "Unknown";
    }
}
