package com.ys.hr.controller;

import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrTargetLinks;
import com.ys.hr.service.IHrTargetLinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Table storing relationships between targets Controller
 *
 * @author ys
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/targetLinks")
public class HrTargetLinksController extends BaseController
{
    @Autowired
    private IHrTargetLinksService hrTargetLinksService;

    /**
     * Query Table storing relationships between targets list
     */
    @RequiresPermissions("hr:targets:list")
    @GetMapping("/list")
    public TableDataInfo list(HrTargetLinks hrTargetLinks)
    {
        startPage();
        List<HrTargetLinks> list = hrTargetLinksService.selectHrTargetLinksList(hrTargetLinks);
        return getDataTable(list);
    }

    /**
     * Add Table storing relationships between targets
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Table storing relationships between targets", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrTargetLinks hrTargetLinks) {
        return toAjax(hrTargetLinksService.insertHrTargetLinks(hrTargetLinks));
    }

    /**
     * Update Table storing relationships between targets
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Table storing relationships between targets", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrTargetLinks hrTargetLinks) {
        return toAjax(hrTargetLinksService.updateHrTargetLinks(hrTargetLinks));
    }

    /**
     * Delete Table storing relationships between targets
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Table storing relationships between targets", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(hrTargetLinksService.removeById(id));
    }
}
