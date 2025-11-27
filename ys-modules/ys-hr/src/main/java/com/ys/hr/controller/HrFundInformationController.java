package com.ys.hr.controller;

import com.ys.common.core.exception.ResourceNotFoundException;
import com.ys.common.core.exception.ValidationException;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrFundInformation;
import com.ys.hr.domain.HrSuperUsiInfo;
import com.ys.hr.service.IHrFundInformationService;
import com.ys.hr.service.IHrSuperUsiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Fund Information Controller (Optimized Version)
 *
 * @author ys
 * @date 2025-10-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/fundInformation")
public class HrFundInformationController extends BaseController {

    @Autowired
    private IHrFundInformationService hrFundInformationService;

    @Autowired
    private IHrSuperUsiInfoService hrSuperUsiInfoService;

    /**
     * Query fund information list
     */
    @GetMapping("/list")
    @RequiresPermissions("hr:fundInformation:list")
    public TableDataInfo list(HrSuperUsiInfo hrSuperUsiInfo) {
        log.info("Querying fund information list with criteria: {}", hrSuperUsiInfo);

        try {
            startPage();
            List<HrSuperUsiInfo> list = hrSuperUsiInfoService.selectHrSuperUsiInfoList(hrSuperUsiInfo);
            log.debug("Found {} fund information records", list.size());
            return getDataTable(list);

        } catch (Exception e) {
            log.error("Error querying fund information list", e);
            throw e; // Let global exception handler handle it
        }
    }

    /**
     * Get employee fund information details
     *
     * @param employeeId Employee ID
     * @return Fund information details
     */
    @GetMapping(value = "/{employeeId}")
    @RequiresPermissions("hr:fundInformation:query")
    public AjaxResult getInfo(
            @PathVariable("employeeId")
            @NotBlank(message = "Employee ID cannot be empty")
            String employeeId) {

        log.info("Fetching fund information for employee: {}", employeeId);

        // Parameter validation
        if (!StringUtils.hasText(employeeId)) {
            throw new ValidationException("employeeId", "Employee ID cannot be empty");
        }

        try {
            String enterpriseId = SecurityUtils.getUserEnterpriseId();

            // Query fund information
            HrFundInformation fundInfo = hrFundInformationService
                    .findByEmployeeAndEnterprise(employeeId, enterpriseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Fund Information", employeeId));

            log.debug("Successfully retrieved fund information for employee: {}", employeeId);
            return success(fundInfo);

        } catch (ResourceNotFoundException e) {
            // Resource not found, log and rethrow
            log.warn("Fund information not found for employee: {}", employeeId);
            throw e;

        } catch (Exception e) {
            // Other exceptions
            log.error("Error fetching fund information for employee: {}", employeeId, e);
            throw e;
        }
    }

    /**
     * Add fund information
     */
    @PostMapping
    @RequiresPermissions("hr:fundInformation:add")
    @Log(title = "Fund Information", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody HrFundInformation hrFundInformation) {

        log.info("Creating new fund information for employee: {}",
                hrFundInformation.getEmployeeId());

        try {
            // Set enterprise ID
            hrFundInformation.setEnterpriseId(SecurityUtils.getUserEnterpriseId());

            // Execute insert
            hrFundInformationService.insertHrFundInformation(hrFundInformation);

            log.info("Successfully created fund information for employee: {}",
                    hrFundInformation.getEmployeeId());
            return success();

        } catch (ValidationException e) {
            log.warn("Validation failed when creating fund information: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Error creating fund information", e);
            throw e;
        }
    }

    /**
     * Update fund information
     */
    @PutMapping
    @RequiresPermissions("hr:fundInformation:edit")
    @Log(title = "Fund Information", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody HrFundInformation hrFundInformation) {

        log.info("Updating fund information: {}", hrFundInformation.getId());

        try {
            // Verify record exists
            if (!hrFundInformationService.exists(hrFundInformation.getId())) {
                throw new ResourceNotFoundException("Fund Information",
                        hrFundInformation.getId());
            }

            // Execute update
            int rows = hrFundInformationService.updateHrFundInformation(hrFundInformation);

            if (rows > 0) {
                log.info("Successfully updated fund information: {}", hrFundInformation.getId());
                return success();
            } else {
                log.warn("No rows updated for fund information: {}", hrFundInformation.getId());
                return error("Update failed: no rows affected");
            }

        } catch (ResourceNotFoundException | ValidationException e) {
            log.warn("Error updating fund information: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Unexpected error updating fund information", e);
            throw e;
        }
    }

    /**
     * Delete fund information
     */
    @DeleteMapping("/{ids}")
    @RequiresPermissions("hr:fundInformation:remove")
    @Log(title = "Fund Information", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable String[] ids) {

        log.info("Deleting fund information records: {}", (Object) ids);

        try {
            int deletedCount = hrFundInformationService.deleteByIds(ids);

            log.info("Successfully deleted {} fund information records", deletedCount);
            return success(String.format("Deleted %d records", deletedCount));

        } catch (Exception e) {
            log.error("Error deleting fund information records", e);
            throw e;
        }
    }
}
