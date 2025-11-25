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
import com.ys.hr.domain.HrTrainingPrograms;
import com.ys.hr.service.IHrTrainingProgramsService;
import org.springframework.validation.annotation.Validated;

/**
 *  Training Management Controller
 *
 * @author ys
 * @date 2025-05-29
 */
@RestController
@RequestMapping("/programs")
public class HrTrainingProgramsController extends BaseController
{
    @Autowired
    private IHrTrainingProgramsService hrTrainingProgramsService;

    /**
     * Query Training Management list
     */
    @RequiresPermissions("hr:programs:list")
    @GetMapping("/list")
    public TableDataInfo list(HrTrainingPrograms hrTrainingPrograms)
    {
        startPage();
        hrTrainingPrograms.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrTrainingPrograms> list = hrTrainingProgramsService.selectHrTrainingProgramsList(hrTrainingPrograms);
        return getDataTable(list);
    }

    /**
     * Export Training Management list
     */
    @Log(title = "Training Management", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrTrainingPrograms hrTrainingPrograms)
    {
        hrTrainingPrograms.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrTrainingPrograms> list = hrTrainingProgramsService.selectHrTrainingProgramsList(hrTrainingPrograms);
        ExcelUtil<HrTrainingPrograms> util = new ExcelUtil<HrTrainingPrograms>(HrTrainingPrograms.class);
        util.exportExcel(response, list, " Training Management Data");
    }

    /**
     * Get Training Management details
     */
    @GetMapping(value = "/{programId}")
    public AjaxResult getInfo(@PathVariable("programId") Long programId) {
        return success(hrTrainingProgramsService.selectHrTrainingProgramsByProgramId(programId));
    }

    /**
     * Add Training Management
     */
    @RequiresPermissions("hr:programs:add")
    @Log(title = "Training Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrTrainingPrograms hrTrainingPrograms) {
        return toAjax(hrTrainingProgramsService.insertHrTrainingPrograms(hrTrainingPrograms));
    }

    /**
     * Update Training Management
     */
    @RequiresPermissions("hr:programs:assign")
    @Log(title = "Training Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrTrainingPrograms hrTrainingPrograms) {
        return toAjax(hrTrainingProgramsService.updateHrTrainingPrograms(hrTrainingPrograms));
    }

    /**
     * Delete Training Management
     */
    @RequiresPermissions("hr:programs:remove")
    @Log(title = "Training Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{programIds}")
    public AjaxResult remove(@PathVariable Long[] programIds) {
        return toAjax(hrTrainingProgramsService.removeByIds(Arrays.asList(programIds)));
    }
}
