package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.hr.domain.HrCandidateInfo;
import com.ys.hr.domain.HrPosition;
import com.ys.hr.domain.HrQuota;
import com.ys.hr.enums.CandidateStatus;
import com.ys.hr.mapper.HrPositionMapper;
import com.ys.hr.service.IHrPositionService;
import com.ys.hr.service.IHrQuotaService;
import com.ys.hr.service.IHrCandidateInfoService;
import com.ys.hr.service.PositionValidationService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Position Management Service Implementation
 *
 * Implements business logic for position management.
 * This service orchestrates operations between position, quota, and candidate management.
 *
 * Key improvements:
 * - All business logic moved from controller
 * - Proper dependency injection (interfaces, not implementations)
 * - Removed commented code
 * - Added comprehensive logging
 * - Extracted validation to separate service
 *
 * @author ys
 * @date 2025-06-23
 */
@Service
public class HrPositionServiceImpl extends ServiceImpl<HrPositionMapper, HrPosition>
        implements IHrPositionService {

    private static final Logger log = LoggerFactory.getLogger(HrPositionServiceImpl.class);

    @Autowired
    private HrPositionMapper hrPositionMapper;

    @Autowired
    private IHrQuotaService hrQuotaService;  // ✓ Fixed: Inject interface, not implementation

    @Autowired
    private IHrCandidateInfoService candidateInfoService;

    @Autowired
    private PositionValidationService validationService;

    /**
     * Query position by ID
     *
     * @param id Position primary key
     * @return Position object
     */
    @Override
    public HrPosition selectHrPositionById(Long id) {
        return hrPositionMapper.selectHrPositionById(id);
    }

    /**
     * Query position list based on conditions
     *
     * Handles two filtering modes:
     * 1. Standard list: Returns all positions matching criteria
     * 2. Available openings: Returns positions with available slots
     *
     * @param hrPosition Position query criteria
     * @param enterpriseId Current user's enterprise ID
     * @return List of positions matching the criteria
     */
    @Override
    public List<HrPosition> selectHrPositionList(HrPosition hrPosition, String enterpriseId) {
        hrPosition.setEnterpriseId(enterpriseId);
        List<HrPosition> positions = hrPositionMapper.selectHrPositionList(hrPosition);

        // If flag is set, filter for positions with available openings
        if (ObjectUtils.isNotEmpty(hrPosition.getFlag())) {
            log.debug("Filtering positions with available openings for enterprise: {}", enterpriseId);
            return filterPositionsWithOpenings(positions);
        }

        return positions;
    }

    /**
     * Query positions without recruitment quotas
     *
     * @param hrPosition Position query criteria
     * @param enterpriseId Current user's enterprise ID
     * @return List of positions without quotas
     */
    @Override
    public List<HrPosition> selectPositionsWithoutQuotas(HrPosition hrPosition, String enterpriseId) {
        hrPosition.setEnterpriseId(enterpriseId);
        List<HrPosition> positions = hrPositionMapper.selectHrPositionList(hrPosition);

        log.debug("Filtering positions without quotas for enterprise: {}", enterpriseId);

        return positions.stream()
                .filter(position -> !hasQuota(position.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Create a new position
     *
     * @param hrPosition Position data to create
     * @param enterpriseId Current user's enterprise ID
     * @return Number of rows affected
     */
    @Override
    public int createPosition(HrPosition hrPosition, String enterpriseId) {
        hrPosition.setEnterpriseId(enterpriseId);
        hrPosition.setCreateTime(DateUtils.getNowDate());

        int result = hrPositionMapper.insert(hrPosition);

        if (result > 0) {
            log.info("Position created successfully: {} (ID: {})",
                    hrPosition.getPositionName(), hrPosition.getId());
        } else {
            log.error("Failed to create position: {}", hrPosition.getPositionName());
        }

        return result;
    }

    /**
     * Update an existing position
     *
     * @param hrPosition Position data to update
     * @return Number of rows affected
     */
    @Override
    public int updateHrPosition(HrPosition hrPosition) {
        hrPosition.setUpdateTime(DateUtils.getNowDate());

        int result = hrPositionMapper.updateById(hrPosition);

        if (result > 0) {
            log.info("Position updated successfully: ID {}", hrPosition.getId());
        } else {
            log.warn("No position updated for ID: {}", hrPosition.getId());
        }

        return result;
    }

    /**
     * Delete positions with validation
     *
     * Validates each position before deletion to ensure it's not in use.
     *
     * @param ids List of position IDs to delete
     * @return AjaxResult indicating success or failure
     */
    @Override
    public AjaxResult deletePositions(List<Long> ids) {
        log.info("Attempting to delete {} position(s)", ids.size());

        // Validate all positions before deletion
        for (Long id : ids) {
            HrPosition position = selectHrPositionById(id);

            if (position == null) {
                String errorMessage = String.format("Position with ID %d not found", id);
                log.error(errorMessage);
                return AjaxResult.error(errorMessage);
            }

            // Validate if position can be deleted
            PositionValidationService.ValidationResult validationResult =
                    validationService.canDeletePosition(position);

            if (!validationResult.isValid()) {
                log.warn("Position deletion validation failed: {}", validationResult.getErrorMessage());
                return AjaxResult.error(validationResult.getErrorMessage());
            }
        }

        // All validations passed, proceed with deletion
        boolean success = removeByIds(ids);

        if (success) {
            log.info("Successfully deleted {} position(s)", ids.size());
            return AjaxResult.success("Position(s) deleted successfully");
        } else {
            log.error("Failed to delete positions: {}", ids);
            return AjaxResult.error("Failed to delete position(s)");
        }
    }

    /**
     * Filter positions that have available openings
     *
     * A position has available openings if:
     * - No quota is defined, OR
     * - Number of shortlisted candidates is less than quota
     *
     * @param positions List of positions to filter
     * @return Filtered list of positions with openings
     */
    private List<HrPosition> filterPositionsWithOpenings(List<HrPosition> positions) {
        List<HrPosition> result = new ArrayList<>();

        for (HrPosition position : positions) {
            Long positionId = position.getId();

            // Check if position has quota defined
            HrQuota quota = getQuotaForPosition(positionId);

            if (quota == null) {
                // No quota defined - position is available
                result.add(position);
                continue;
            }

            // Check if current candidate count is below quota
            long candidateCount = countShortlistedCandidates(positionId);

            if (candidateCount < quota.getQuotaNumber()) {
                result.add(position);
                log.debug("Position {} has openings: {}/{} filled",
                        positionId, candidateCount, quota.getQuotaNumber());
            }
        }

        log.debug("Filtered {} positions with openings from {} total",
                result.size(), positions.size());

        return result;
    }

    /**
     * Get recruitment quota for a position
     *
     * @param positionId Position ID
     * @return HrQuota object, or null if no quota defined
     */
    private HrQuota getQuotaForPosition(Long positionId) {
        HrQuota queryParam = new HrQuota();
        queryParam.setPostId(positionId);

        List<HrQuota> quotas = hrQuotaService.selectHrQuotaList(queryParam);

        return quotas.isEmpty() ? null : quotas.get(0);
    }

    /**
     * Check if a position has a quota defined
     *
     * @param positionId Position ID
     * @return true if quota exists, false otherwise
     */
    private boolean hasQuota(Long positionId) {
        return getQuotaForPosition(positionId) != null;
    }

    /**
     * Count number of shortlisted candidates for a position
     *
     * Only counts candidates with HIRED status.
     *
     * @param positionId Position ID
     * @return Number of shortlisted candidates
     */
    private long countShortlistedCandidates(Long positionId) {
        HrCandidateInfo queryParam = new HrCandidateInfo();
        queryParam.setJobInformation(positionId);
        queryParam.setCandidateStatus(CandidateStatus.HIRED.getCode());

        List<HrCandidateInfo> candidates =
                candidateInfoService.selectHrCandidateInfoList(queryParam);

        return candidates.size();
    }
}
