package com.ys.hr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.hr.domain.HrCandidateInfo;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrPosition;
import com.ys.hr.domain.HrQuota;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Position Validation Service
 *
 * Handles validation logic for position operations, particularly
 * checking if a position can be safely deleted.
 *
 * This service centralizes all validation rules and provides
 * clear, descriptive error messages.
 *
 * @author ys
 * @date 2025-11-26
 */
@Service
public class PositionValidationService {

    private static final Logger log = LoggerFactory.getLogger(PositionValidationService.class);

    @Autowired
    private IHrEmployeesService employeesService;

    @Autowired
    private IHrCandidateInfoService candidateInfoService;

    @Autowired
    private IHrQuotaService quotaService;

    /**
     * Validation result class
     *
     * Contains validation status and error message if validation fails.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult error(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * Validate if a position can be deleted
     *
     * Checks if the position is being used by:
     * 1. Employees (assigned to this position)
     * 2. Candidates (applied for this position)
     * 3. Quotas (recruitment quotas for this position)
     *
     * @param position Position to validate
     * @return ValidationResult indicating if deletion is allowed
     */
    public ValidationResult canDeletePosition(HrPosition position) {
        if (position == null) {
            return ValidationResult.error("Position not found");
        }

        Long positionId = position.getId();
        String positionName = position.getPositionName();

        log.debug("Validating deletion for position: {} (ID: {})", positionName, positionId);

        // Check if any employees are assigned to this position
        if (hasEmployeesInPosition(positionId)) {
            String message = String.format(
                    "Position '%s' is assigned to one or more employees and cannot be deleted",
                    positionName
            );
            log.warn(message);
            return ValidationResult.error(message);
        }

        // Check if any candidates have applied for this position
        if (hasCandidatesForPosition(positionId)) {
            String message = String.format(
                    "Position '%s' has candidate applications and cannot be deleted",
                    positionName
            );
            log.warn(message);
            return ValidationResult.error(message);
        }

        // Check if there are recruitment quotas for this position
        if (hasQuotasForPosition(positionId)) {
            String message = String.format(
                    "Position '%s' has recruitment quotas defined and cannot be deleted",
                    positionName
            );
            log.warn(message);
            return ValidationResult.error(message);
        }

        log.info("Position '{}' (ID: {}) can be safely deleted", positionName, positionId);
        return ValidationResult.success();
    }

    /**
     * Check if any employees are assigned to this position
     *
     * @param positionId Position ID to check
     * @return true if employees exist, false otherwise
     */
    private boolean hasEmployeesInPosition(Long positionId) {
        QueryWrapper<HrEmployees> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("position", positionId.toString());
        long count = employeesService.count(queryWrapper);

        log.debug("Found {} employees in position ID: {}", count, positionId);
        return count > 0;
    }

    /**
     * Check if any candidates have applied for this position
     *
     * @param positionId Position ID to check
     * @return true if candidates exist, false otherwise
     */
    private boolean hasCandidatesForPosition(Long positionId) {
        QueryWrapper<HrCandidateInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_information", positionId);
        long count = candidateInfoService.count(queryWrapper);

        log.debug("Found {} candidates for position ID: {}", count, positionId);
        return count > 0;
    }

    /**
     * Check if recruitment quotas exist for this position
     *
     * @param positionId Position ID to check
     * @return true if quotas exist, false otherwise
     */
    private boolean hasQuotasForPosition(Long positionId) {
        QueryWrapper<HrQuota> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", positionId);
        long count = quotaService.count(queryWrapper);

        log.debug("Found {} quotas for position ID: {}", count, positionId);
        return count > 0;
    }

    /**
     * Batch validate multiple positions for deletion
     *
     * @param positions List of positions to validate
     * @return First validation error found, or success if all valid
     */
    public ValidationResult canDeletePositions(java.util.List<HrPosition> positions) {
        for (HrPosition position : positions) {
            ValidationResult result = canDeletePosition(position);
            if (!result.isValid()) {
                return result;
            }
        }
        return ValidationResult.success();
    }
}
