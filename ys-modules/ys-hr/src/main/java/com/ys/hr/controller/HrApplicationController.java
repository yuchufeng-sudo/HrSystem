package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.hr.domain.HrApplication;
import com.ys.hr.service.IHrApplicationService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * application Controller
 *
 * @author ys
 * @date 2025-06-17
 */
@RestController
@RequestMapping("/application")
public class HrApplicationController extends BaseController
{
    @Autowired
    private IHrApplicationService hrApplicationService;

    /**
     * Query application list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrApplication hrApplication)
    {
        startPage();
        List<HrApplication> list = hrApplicationService.selectHrApplicationList(hrApplication);
        return getDataTable(list);
    }

    /**
     * Export application list
     */
    @Log(title = "application", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrApplication hrApplication)
    {
        List<HrApplication> list = hrApplicationService.selectHrApplicationList(hrApplication);
        ExcelUtil<HrApplication> util = new ExcelUtil<HrApplication>(HrApplication.class);
        util.exportExcel(response, list, "application Data");
    }

    /**
     * Get application details
     */
    @GetMapping(value = "/{applicationId}")
    public AjaxResult getInfo(@PathVariable("applicationId") String applicationId) {
        return success(hrApplicationService.selectHrApplicationByApplicationId(applicationId));
    }

    //Determine whether the email address is requested
    @GetMapping(value = "/emailIsExist")
    public AjaxResult emailIsExist(HrApplication hrApplication) {
        List<HrApplication> hrApplications = hrApplicationService.selectHrApplicationList(hrApplication);
        boolean isExist = true;
        if(ObjectUtils.isNotEmpty(hrApplications)){
            isExist = false;
        }
        return AjaxResult.success(isExist);
    }

    /**
     * Add application
     */
    @Log(title = "application", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add(@Validated @RequestBody HrApplication hrApplication) {
        hrApplication.setStatus("1");
        return toAjax(hrApplicationService.insertHrApplication(hrApplication));
    }

    /**
     * Update application
     */
    @Log(title = "application", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrApplication hrApplication) {
        return toAjax(hrApplicationService.updateHrApplication(hrApplication));
    }

    /**
     * Delete application
     */
    @Log(title = "application", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applicationIds}")
    public AjaxResult remove(@PathVariable String[] applicationIds) {
        return toAjax(hrApplicationService.removeByIds(Arrays.asList(applicationIds)));
    }
}
