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
 *  TRAINING PROGRAM SUPERVISOR  Controller
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
     * Query  TRAINING PROGRAM SUPERVISOR  list
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
     * Export  TRAINING PROGRAM SUPERVISOR  list
     */
    @Log(title = " TRAINING PROGRAM SUPERVISOR ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrTrainingPrograms hrTrainingPrograms)
    {
        hrTrainingPrograms.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrTrainingPrograms> list = hrTrainingProgramsService.selectHrTrainingProgramsList(hrTrainingPrograms);
        ExcelUtil<HrTrainingPrograms> util = new ExcelUtil<HrTrainingPrograms>(HrTrainingPrograms.class);
        util.exportExcel(response, list, " TRAINING PROGRAM SUPERVISOR  Data");
    }

    /**
     * Get  TRAINING PROGRAM SUPERVISOR  details
     */
    @GetMapping(value = "/{programId}")
    public AjaxResult getInfo(@PathVariable("programId") Long programId) {
        return success(hrTrainingProgramsService.selectHrTrainingProgramsByProgramId(programId));
    }

    /**
     * Add  TRAINING PROGRAM SUPERVISOR
     */
    @RequiresPermissions("hr:programs:add")
    @Log(title = " TRAINING PROGRAM SUPERVISOR ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrTrainingPrograms hrTrainingPrograms) {
        return toAjax(hrTrainingProgramsService.insertHrTrainingPrograms(hrTrainingPrograms));
    }

    /**
     * Update  TRAINING PROGRAM SUPERVISOR
     */
    @RequiresPermissions("hr:programs:assign")
    @Log(title = " TRAINING PROGRAM SUPERVISOR ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrTrainingPrograms hrTrainingPrograms) {
        return toAjax(hrTrainingProgramsService.updateHrTrainingPrograms(hrTrainingPrograms));
    }

    /**
     * Delete  TRAINING PROGRAM SUPERVISOR
     */
    @RequiresPermissions("hr:programs:remove")
    @Log(title = " TRAINING PROGRAM SUPERVISOR ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{programIds}")
    public AjaxResult remove(@PathVariable Long[] programIds) {
        return toAjax(hrTrainingProgramsService.removeByIds(Arrays.asList(programIds)));
    }
}
