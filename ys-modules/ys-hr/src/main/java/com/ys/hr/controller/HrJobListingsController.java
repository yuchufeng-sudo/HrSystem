package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrJobListings;
import com.ys.hr.service.IHrJobListingsService;
import org.springframework.validation.annotation.Validated;

/**
 * Job Listings Controller
 *
 * @author ys
 * @date 2025-09-25
 */
@RestController
@RequestMapping("/jobListings")
public class HrJobListingsController extends BaseController
{
    @Autowired
    private IHrJobListingsService hrJobListingsService;

    /**
     * Query Job Listings list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrJobListings hrJobListings)
    {
        startPage();
        hrJobListings.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrJobListings> list = hrJobListingsService.selectHrJobListingsList(hrJobListings);
        return getDataTable(list);
    }

    /**
     * Export Job Listings list
     */
    @RequiresPermissions("hr:jobListings:export")
    @Log(title = "Job Listings", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrJobListings hrJobListings)
    {
        hrJobListings.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrJobListings> list = hrJobListingsService.selectHrJobListingsList(hrJobListings);
        ExcelUtil<HrJobListings> util = new ExcelUtil<HrJobListings>(HrJobListings.class);
        util.exportExcel(response, list, "Job Listings Data");
    }

    /**
     * Get Job Listings details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        HrJobListings data = hrJobListingsService.selectHrJobListingsById(id);
        if (data!=null&&!data.getEnterpriseId().equals(SecurityUtils.getUserEnterpriseId())) {
            return AjaxResult.error("No data permission");
        }
        return success(data);
    }

    /**
     * Add Job Listings
     */
    @RequiresPermissions("hr:jobListings:add")
    @Log(title = "Job Listings", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrJobListings hrJobListings) {
        hrJobListings.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrJobListingsService.insertHrJobListings(hrJobListings));
    }

    /**
     * Update Job Listings
     */
    @RequiresPermissions("hr:jobListings:edit")
    @Log(title = "Job Listings", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrJobListings hrJobListings) {
        return toAjax(hrJobListingsService.updateHrJobListings(hrJobListings));
    }

    /**
     * Delete Job Listings
     */
    @RequiresPermissions("hr:jobListings:remove")
    @Log(title = "Job Listings", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(hrJobListingsService.removeByIds(Arrays.asList(ids)));
    }
}
