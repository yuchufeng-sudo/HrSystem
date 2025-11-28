package com.ys.hr.service;

import com.ys.common.core.utils.DateUtils;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrOffboardingAuditLog;
import com.ys.hr.mapper.HrOffboardingAuditLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Offboarding Process Audit Logging Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OffboardingAuditService {

    @Resource
    private HrOffboardingAuditLogMapper auditLogMapper;

    /**
     * Record resignation operation
     */
    public void logResignation(HrEmployees employee, String operation, String details) {
        try {
            HrOffboardingAuditLog auditLog = buildHrOffboardingAuditLog(employee, operation, details);

            auditLogMapper.insert(auditLog);

            log.info("Audit log recorded - Operation: {}, Employee: {}, Operator: {}",
                    operation, employee.getEmployeeId(), SecurityUtils.getUserId());
        } catch (Exception e) {
            // Audit log failure should not affect business process, only log error
            log.error("Failed to record audit log for operation: {}, employee: {}",
                    operation, employee.getEmployeeId(), e);
        }
    }

    @NotNull
    private static HrOffboardingAuditLog buildHrOffboardingAuditLog(HrEmployees employee, String operation, String details) {
        HrOffboardingAuditLog auditLog = new HrOffboardingAuditLog();
        auditLog.setEmployeeId(employee.getEmployeeId());
        auditLog.setEmployeeName(employee.getFullName());
        auditLog.setOperationType(operation);
        auditLog.setOperatorId(SecurityUtils.getUserId());
        auditLog.setOperatorName(SecurityUtils.getUsername());
        auditLog.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        auditLog.setOperationTime(DateUtils.getNowDate());
        auditLog.setOperationDetails(details);
        auditLog.setIpAddress(SecurityUtils.getLoginUser().getIpaddr());
        return auditLog;
    }

    /**
     * Record resignation initiation
     */
    public void logResignInitiation(HrEmployees employee) {
        String details = String.format(
                "Resignation initiated - Date: %s, Reason: %s, HR Handler: %s",
                employee.getResignationDate(),
                employee.getResignationReason(),
                employee.getResignationHrUserId()
        );
        logResignation(employee, "RESIGN_INITIATED", details);
    }

    /**
     * Record resignation cancellation
     */
    public void logResignCancellation(HrEmployees employee) {
        String details = String.format(
                "Resignation cancelled for employee: %s",
                employee.getFullName()
        );
        logResignation(employee, "RESIGN_CANCELLED", details);
    }

    /**
     * Record employee restore
     */
    public void logEmployeeRestore(HrEmployees employee) {
        String details = String.format(
                "Employee restored from resigned status: %s",
                employee.getFullName()
        );
        logResignation(employee, "EMPLOYEE_RESTORED", details);
    }

    /**
     * Record security violation attempt
     */
    public void logSecurityViolation(Long employeeId, String violationType, String details) {
        try {
            HrOffboardingAuditLog auditLog = new HrOffboardingAuditLog();
            auditLog.setEmployeeId(employeeId);
            auditLog.setOperationType("SECURITY_VIOLATION_" + violationType);
            auditLog.setOperatorId(SecurityUtils.getUserId());
            auditLog.setOperatorName(SecurityUtils.getUsername());
            auditLog.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            auditLog.setOperationTime(DateUtils.getNowDate());
            auditLog.setOperationDetails(details);
            auditLog.setIpAddress(SecurityUtils.getLoginUser().getIpaddr());

            auditLogMapper.insert(auditLog);

            log.warn("Security violation logged - Type: {}, Employee: {}, Operator: {}",
                    violationType, employeeId, SecurityUtils.getUserId());
        } catch (Exception e) {
            log.error("Failed to record security violation log", e);
        }
    }
}
