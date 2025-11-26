package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrPosition;
import com.ys.hr.service.IHrPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Position Management Controller
 *
 * Handles HTTP requests for position management operations.
 * This controller follows the thin controller pattern - it only handles
 * request/response logic and delegates all business logic to the service layer.
 *
 * Key improvements:
 * - Removed all business logic (filtering, validation)
 * - Removed direct dependencies on other services
 * - All methods are now 2-5 lines
 * - No QueryWrapper usage
 * - Clean separation of concerns
 *
 * @author ys
 * @date 2025-06-23
 */
@RestController
@RequestMapping("/position")
public class HrPositionController extends BaseController {

    @Autowired
    private IHrPositionService hrPositionService;

    /**
     * Query position list with optional filtering
     *
     * Supports two modes based on the flag parameter:
     * - Without flag: Returns all positions
     * - With flag: Returns only positions with available openings
     *   (positions where candidate count < quota or no quota defined)
     *
     * @param hrPosition Query parameters including optional flag
     * @return Paginated table data containing positions
     */
    @RequiresPermissions("hr:position:list")
    @GetMapping("/list")
    public TableDataInfo list(HrPosition hrPosition) {
        startPage();
        List<HrPosition> list = hrPositionService.selectHrPositionList(
                hrPosition, SecurityUtils.getUserEnterpriseId()
        );
        return getDataTable(list);
    }

    /**
     * Query positions without recruitment quotas
     *
     * Returns only positions that don't have any recruitment quotas defined.
     * Used for quota assignment operations.
     *
     * @param hrPosition Query parameters
     * @return Ajax result containing list of positions without quotas
     */
    @GetMapping("/qute/list")
    public AjaxResult quteList(HrPosition hrPosition) {
        List<HrPosition> list = hrPositionService.selectPositionsWithoutQuotas(
                hrPosition, SecurityUtils.getUserEnterpriseId()
        );
        return success(list);
    }

    /**
     * Export position list to Excel
     *
     * @param response HTTP response object to write Excel file
     * @param hrPosition Query parameters for filtering positions to export
     */
    @RequiresPermissions("hr:position:export")
    @Log(title = "Position Management", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrPosition hrPosition) {
        List<HrPosition> list = hrPositionService.selectHrPositionList(
                hrPosition, SecurityUtils.getUserEnterpriseId()
        );
        ExcelUtil<HrPosition> util = new ExcelUtil<>(HrPosition.class);
        util.exportExcel(response, list, "Position Management Data");
    }

    /**
     * Get position details by ID
     *
     * @param id The unique identifier of the position
     * @return Ajax result containing the position details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrPositionService.selectHrPositionById(id));
    }

    /**
     * Create a new position
     *
     * @param hrPosition The position object to be created
     * @return Ajax result indicating success or failure
     */
    @RequiresPermissions("hr:position:add")
    @Log(title = "Position Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrPosition hrPosition) {
        hrPositionService.createPosition(hrPosition, SecurityUtils.getUserEnterpriseId());
        return success("Position created successfully");
    }

    /**
     * Update an existing position
     *
     * @param hrPosition The position object with updated information
     * @return Ajax result indicating success or failure
     */
    @RequiresPermissions("hr:position:edit")
    @Log(title = "Position Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrPosition hrPosition) {
        return toAjax(hrPositionService.updateHrPosition(hrPosition));
    }

    /**
     * Delete positions by their IDs
     *
     * Validates that positions can be safely deleted before deletion.
     * A position cannot be deleted if:
     * - It's assigned to any employees
     * - It has candidate applications
     * - It has recruitment quotas defined
     *
     * @param ids Array of position IDs to be deleted
     * @return Ajax result indicating success or failure with appropriate error message
     */
    @RequiresPermissions("hr:position:remove")
    @Log(title = "Position Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return hrPositionService.deletePositions(Arrays.asList(ids));
    }
}
