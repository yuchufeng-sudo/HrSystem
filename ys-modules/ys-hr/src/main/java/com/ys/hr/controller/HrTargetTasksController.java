package com.ys.hr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrTargetEmployee;
import com.ys.hr.domain.HrTargetTasks;
import com.ys.hr.domain.HrTargets;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IHrTargetEmployeeService;
import com.ys.hr.service.IHrTargetTasksService;
import com.ys.hr.service.IHrTargetsService;
import com.ys.system.api.model.LoginUser;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Targets Manages Controller
 *
 * @author ys
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/targetTasks")
public class HrTargetTasksController extends BaseController
{
    @Autowired
    private IHrTargetTasksService hrTargetTasksService;
    @Autowired
    private IHrTargetsService hrTargetsService;

    /**
     * Query Targets Manages list
     */
    @RequiresPermissions("hr:targets:list")
    @GetMapping("/list")
    public TableDataInfo list(HrTargetTasks hrTargetTasks)
    {
        startPage();
        List<HrTargetTasks> list = hrTargetTasksService.selectHrTargetTasksList(hrTargetTasks);
        return getDataTable(list);
    }

    /**
     * Get Targets Manages details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        HrTargetTasks data = hrTargetTasksService.selectHrTargetTasksById(id);
        hrTargetsService.checkParticipantsByTasks(data.getId());
        return success(data);
    }

    /**
     * Add Targets Manages
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Targets Manages", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrTargetTasks hrTargetTasks) {
        return toAjax(hrTargetTasksService.insertHrTargetTasks(hrTargetTasks));
    }

    /**
     * Update Targets Manages
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Targets Manages", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrTargetTasks hrTargetTasks) {
        hrTargetsService.checkParticipantsByTasks(hrTargetTasks.getId());
        return toAjax(hrTargetTasksService.updateHrTargetTasks(hrTargetTasks));
    }

    @PutMapping("complete")
    public AjaxResult complete(@RequestBody HrTargetTasks hrTargetTasks) {
        hrTargetsService.checkParticipantsByTasks(hrTargetTasks.getId());
        hrTargetTasks.setStatus("Complete");
        hrTargetTasks.setCompleteTime(DateUtils.getNowDate());
        return toAjax(hrTargetTasksService.updateHrTargetTasks(hrTargetTasks));
    }

    @PutMapping("confirm/{id}")
    public AjaxResult confirm(@PathVariable Long id) {
        hrTargetsService.checkHeadByTasks(id);
        HrTargetTasks hrTargetTasks = new HrTargetTasks();
        hrTargetTasks.setId(id);
        hrTargetTasks.setStatus("Confirm");
        hrTargetTasks.setConfigTime(DateUtils.getNowDate());
        return toAjax(hrTargetTasksService.updateHrTargetTasks(hrTargetTasks));
    }

    @PutMapping("refuse/{id}")
    public AjaxResult refuse(@PathVariable Long id) {
        hrTargetsService.checkHeadByTasks(id);
        HrTargetTasks hrTargetTasks = new HrTargetTasks();
        hrTargetTasks.setId(id);
        hrTargetTasks.setStatus("Refuse");
        return toAjax(hrTargetTasksService.updateHrTargetTasks(hrTargetTasks));
    }

    /**
     * Delete Targets Manages
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Targets Manages", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrTargetTasksService.removeByIds(Arrays.asList(ids)));
    }

}
