package com.ys.hr.controller;

import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrTargetNotes;
import com.ys.hr.service.IHrTargetNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Table storing notes associated with targets Controller
 *
 * @author ys
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/targetNotes")
public class HrTargetNotesController extends BaseController
{
    @Autowired
    private IHrTargetNotesService hrTargetNotesService;

    /**
     * Query Table storing notes associated with targets list
     */
    @RequiresPermissions("hr:targets:list")
    @GetMapping("/list")
    public TableDataInfo list(HrTargetNotes hrTargetNotes)
    {
        startPage();
        List<HrTargetNotes> list = hrTargetNotesService.selectHrTargetNotesList(hrTargetNotes);
        return getDataTable(list);
    }

    /**
     * Add Table storing notes associated with targets
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Table storing notes associated with targets", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrTargetNotes hrTargetNotes) {
        hrTargetNotesService.insertHrTargetNotes(hrTargetNotes);
        return AjaxResult.success();
    }

    /**
     * Update Table storing notes associated with targets
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Table storing notes associated with targets", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrTargetNotes hrTargetNotes) {
        return toAjax(hrTargetNotesService.updateHrTargetNotes(hrTargetNotes));
    }

    /**
     * Delete Table storing notes associated with targets
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Table storing notes associated with targets", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrTargetNotesService.removeByIds(Arrays.asList(ids)));
    }
}
