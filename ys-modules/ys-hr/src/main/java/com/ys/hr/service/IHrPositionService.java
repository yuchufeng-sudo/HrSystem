package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.hr.domain.HrPosition;

import java.util.List;

/**
 * Position Management Service Interface
 *
 * Defines business operations for position management.
 * Updated to include new methods that encapsulate complex business logic
 * previously located in the controller.
 *
 * @author ys
 * @date 2025-06-23
 */
public interface IHrPositionService extends IService<HrPosition> {

    /**
     * Query position by ID
     *
     * @param id Position primary key
     * @return Position object
     */
    HrPosition selectHrPositionById(Long id);

    /**
     * Query position list based on conditions
     *
     * Handles filtering logic including:
     * - Basic query parameters
     * - Optional flag-based filtering for positions with available openings
     * - Enterprise ID filtering
     *
     * When flag is present, returns only positions where:
     * - No quota is defined, OR
     * - Candidate count is less than quota number
     *
     * @param hrPosition Position query criteria
     * @param enterpriseId Current user's enterprise ID
     * @return List of positions matching the criteria
     */
    List<HrPosition> selectHrPositionList(HrPosition hrPosition, String enterpriseId);

    /**
     * Query positions without recruitment quotas
     *
     * Returns only positions that don't have any recruitment quotas defined.
     * Used for quota assignment operations where positions need to be
     * identified that can have quotas added.
     *
     * @param hrPosition Position query criteria
     * @param enterpriseId Current user's enterprise ID
     * @return List of positions without quotas
     */
    List<HrPosition> selectPositionsWithoutQuotas(HrPosition hrPosition, String enterpriseId);

    /**
     * Create a new position
     *
     * This method handles the complete position creation workflow:
     * 1. Sets the enterprise ID
     * 2. Sets creation timestamp
     * 3. Saves the position
     * 4. (Optional) Creates initial quota if needed
     *
     * @param hrPosition Position data to create
     * @param enterpriseId Current user's enterprise ID
     * @return Number of rows affected
     */
    int createPosition(HrPosition hrPosition, String enterpriseId);

    /**
     * Update an existing position
     *
     * @param hrPosition Position data to update
     * @return Number of rows affected
     */
    int updateHrPosition(HrPosition hrPosition);

    /**
     * Delete positions with validation
     *
     * This method handles the complete deletion workflow:
     * 1. Validates each position can be safely deleted by checking:
     *    - No employees assigned to position
     *    - No candidates applied for position
     *    - No recruitment quotas defined for position
     * 2. If validation fails, returns error with descriptive message
     * 3. If validation passes, deletes all positions
     *
     * @param ids List of position IDs to delete
     * @return AjaxResult indicating success or failure with error message
     */
    AjaxResult deletePositions(List<Long> ids);
}
