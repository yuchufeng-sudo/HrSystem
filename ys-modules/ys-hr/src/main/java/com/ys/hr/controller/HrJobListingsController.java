package com.ys.hr.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.core.exception.PermissionDeniedException;
import com.ys.common.core.exception.ResourceNotFoundException;
import com.ys.common.security.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrJobListings;
import com.ys.hr.service.IHrJobListingsService;

/**
 * Job Listings Controller
 *
 * @author ys
 * @date 2025-09-25
 */
@RestController
@RequestMapping("/jobListings")
public class HrJobListingsController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(HrJobListingsController.class);

    @Autowired
    private IHrJobListingsService hrJobListingsService;

    /**
     * Query Job Listings list
     *
     * @param hrJobListings Query criteria
     * @return Table data with job listings
     */
    @GetMapping("/list")
    public TableDataInfo list(HrJobListings hrJobListings) {
        try {
            log.debug("Received request to list job listings");

            startPage();

            // Set enterprise ID filter
            String enterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.isBlank(enterpriseId)) {
                log.error("User enterprise ID is null");
                return getDataTable(new ArrayList<>());
            }

            hrJobListings.setEnterpriseId(enterpriseId);

            List<HrJobListings> list = hrJobListingsService.selectHrJobListingsList(hrJobListings);

            log.info("Successfully retrieved {} job listings for enterprise: {}",
                    list.size(), enterpriseId);

            return getDataTable(list);

        } catch (Exception e) {
            log.error("Error retrieving job listings list", e);
            throw e;
        }
    }

    /**
     * Export Job Listings list
     *
     * @param response HTTP response for export
     * @param hrJobListings Query criteria for export
     */
    @RequiresPermissions("hr:jobListings:export")
    @Log(title = "Job Listings", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrJobListings hrJobListings) {
        try {
            log.info("Starting job listings export");

            String enterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.isBlank(enterpriseId)) {
                log.error("Cannot export: User enterprise ID is null");
                throw new PermissionDeniedException("export", "job listings");
            }

            hrJobListings.setEnterpriseId(enterpriseId);

            List<HrJobListings> list = hrJobListingsService.selectHrJobListingsList(hrJobListings);

            ExcelUtil<HrJobListings> util = new ExcelUtil<>(HrJobListings.class);
            util.exportExcel(response, list, "Job Listings Data");

            log.info("Successfully exported {} job listings", list.size());

        } catch (Exception e) {
            log.error("Error exporting job listings", e);
            throw e;
        }
    }

    /**
     * Get Job Listings details
     *
     * @param id Job Listing ID
     * @return Ajax result with job listing details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        // Input validation
        if (StringUtils.isBlank(id)) {
            log.warn("Attempt to get job listing with blank id");
            return AjaxResult.error("Job listing ID cannot be empty");
        }

        try {
            log.debug("Retrieving job listing details for id: {}", id);

            // Query data
            HrJobListings data = hrJobListingsService.selectHrJobListingsById(id);

            // Permission validation
            String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.isBlank(userEnterpriseId)) {
                log.error("User enterprise ID is null");
                return AjaxResult.error("Authentication error");
            }

            if (!data.getEnterpriseId().equals(userEnterpriseId)) {
                log.warn("Permission denied: User {} attempted to access job listing {} from different enterprise",
                        SecurityUtils.getUserId(), id);
                throw new PermissionDeniedException("Job Listing", "view");
            }

            log.info("Successfully retrieved job listing: {}", id);
            return success(data);

        } catch (ResourceNotFoundException e) {
            log.warn("Job listing not found: {}", id);
            return AjaxResult.error("Job listing not found: " + id);
        } catch (PermissionDeniedException e) {
            return AjaxResult.error("No data permission");
        } catch (Exception e) {
            log.error("Error retrieving job listing: {}", id, e);
            return AjaxResult.error("Failed to retrieve job listing details");
        }
    }

    /**
     * Add Job Listings
     *
     * @param hrJobListings Job listing to add
     * @param bindingResult Validation results
     * @return Ajax result with operation status
     */
    @RequiresPermissions("hr:jobListings:add")
    @Log(title = "Job Listings", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrJobListings hrJobListings,
                          BindingResult bindingResult) {
        // Validate binding results
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((a, b) -> a + "; " + b)
                    .orElse("Validation failed");
            log.warn("Validation failed when adding job listing: {}", errorMsg);
            return AjaxResult.error(errorMsg);
        }

        try {
            log.info("Adding new job listing: {}", hrJobListings.getTitle());

            // Set enterprise ID
            String enterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.isBlank(enterpriseId)) {
                log.error("Cannot add job listing: User enterprise ID is null");
                return AjaxResult.error("Authentication error");
            }

            hrJobListings.setEnterpriseId(enterpriseId);

            // Execute insertion
            int result = hrJobListingsService.insertHrJobListings(hrJobListings);

            if (result > 0) {
                log.info("Successfully added job listing with id: {}", hrJobListings.getId());
                return AjaxResult.success("Job listing created successfully", hrJobListings.getId());
            } else {
                log.error("Failed to add job listing: no rows affected");
                return AjaxResult.error("Failed to create job listing");
            }

        } catch (Exception e) {
            log.error("Error adding job listing", e);
            return AjaxResult.error("Failed to create job listing: " + e.getMessage());
        }
    }

    /**
     * Update Job Listings
     *
     * @param hrJobListings Job listing to update
     * @return Ajax result with operation status
     */
    @RequiresPermissions("hr:jobListings:edit")
    @Log(title = "Job Listings", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrJobListings hrJobListings) {
        // Input validation
        if (hrJobListings == null || StringUtils.isBlank(hrJobListings.getId())) {
            log.warn("Attempt to update job listing with invalid data");
            return AjaxResult.error("Job listing ID is required");
        }

        try {
            log.info("Updating job listing: {}", hrJobListings.getId());

            // Permission validation - ensure user can only update their own enterprise's data
            HrJobListings existing = hrJobListingsService.selectHrJobListingsById(hrJobListings.getId());

            String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.isBlank(userEnterpriseId)) {
                log.error("User enterprise ID is null");
                return AjaxResult.error("Authentication error");
            }

            if (!existing.getEnterpriseId().equals(userEnterpriseId)) {
                log.warn("Permission denied: User attempted to update job listing {} from different enterprise",
                        hrJobListings.getId());
                throw new PermissionDeniedException("Job Listing", "edit");
            }

            // Keep enterprise ID unchanged
            hrJobListings.setEnterpriseId(existing.getEnterpriseId());

            // Execute update
            int result = hrJobListingsService.updateHrJobListings(hrJobListings);

            if (result > 0) {
                log.info("Successfully updated job listing: {}", hrJobListings.getId());
                return AjaxResult.success("Job listing updated successfully");
            } else {
                log.error("Failed to update job listing {}: no rows affected", hrJobListings.getId());
                return AjaxResult.error("Failed to update job listing");
            }

        } catch (ResourceNotFoundException e) {
            log.warn("Job listing not found for update: {}", hrJobListings.getId());
            return AjaxResult.error("Job listing not found");
        } catch (PermissionDeniedException e) {
            return AjaxResult.error("No data permission");
        } catch (Exception e) {
            log.error("Error updating job listing: {}", hrJobListings.getId(), e);
            return AjaxResult.error("Failed to update job listing: " + e.getMessage());
        }
    }

    /**
     * Delete Job Listings
     *
     * @param ids Job listing IDs to delete
     * @return Ajax result with operation status
     */
    @RequiresPermissions("hr:jobListings:remove")
    @Log(title = "Job Listings", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        // Input validation
        if (ids == null || ids.length == 0) {
            log.warn("Attempt to delete job listings with empty id array");
            return AjaxResult.error("Job listing IDs are required");
        }

        try {
            log.info("Deleting {} job listings", ids.length);

            String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.isBlank(userEnterpriseId)) {
                log.error("User enterprise ID is null");
                return AjaxResult.error("Authentication error");
            }

            // Permission validation - ensure all records to be deleted belong to current user's enterprise
            for (String id : ids) {
                try {
                    HrJobListings existing = hrJobListingsService.selectHrJobListingsById(id);
                    if (!existing.getEnterpriseId().equals(userEnterpriseId)) {
                        log.warn("Permission denied: User attempted to delete job listing {} from different enterprise", id);
                        return AjaxResult.error("No data permission for job listing: " + id);
                    }
                } catch (ResourceNotFoundException e) {
                    log.warn("Job listing not found for deletion: {}", id);
                    return AjaxResult.error("Job listing not found: " + id);
                }
            }

            // Execute deletion
            boolean result = hrJobListingsService.removeByIds(Arrays.asList(ids));

            if (result) {
                log.info("Successfully deleted {} job listings", ids.length);
                return AjaxResult.success("Job listings deleted successfully");
            } else {
                log.error("Failed to delete job listings");
                return AjaxResult.error("Failed to delete job listings");
            }

        } catch (Exception e) {
            log.error("Error deleting job listings", e);
            return AjaxResult.error("Failed to delete job listings: " + e.getMessage());
        }
    }
}
