package com.ys.system.controller;

import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.system.domain.SysMessageTemplate;
import com.ys.system.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  Message Template management Controller
 *
 * @author ys
 * @date 2024-10-23
 */
@RestController
@RequestMapping("/messageTemplate")
public class SysMessageTemplateController extends BaseController
{
    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;

    /**
     * Query Message Template management list
     */
    @GetMapping("/list")
    public TableDataInfo list(SysMessageTemplate sysMessageTemplate)
    {
        startPage();
        List<SysMessageTemplate> list = sysMessageTemplateService.selectSysMessageTemplateList(sysMessageTemplate);
        return getDataTable(list);
    }

    @GetMapping("/allList")
    public AjaxResult allList(SysMessageTemplate sysMessageTemplate)
    {
        List<SysMessageTemplate> list = sysMessageTemplateService.selectSysMessageTemplateList(sysMessageTemplate);
        return AjaxResult.success(list);
    }

    /**
     * Get Message Template management Details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(sysMessageTemplateService.selectSysMessageTemplateById(id));
    }

    /**
     * Add Message Template management
     */
    @PostMapping
    public AjaxResult add(@RequestBody SysMessageTemplate sysMessageTemplate)
    {
        return toAjax(sysMessageTemplateService.insertSysMessageTemplate(sysMessageTemplate));
    }

    /**
     * Update Message Template management
     */
    @PutMapping
    public AjaxResult edit(@RequestBody SysMessageTemplate sysMessageTemplate)
    {
        return toAjax(sysMessageTemplateService.updateSysMessageTemplate(sysMessageTemplate));
    }

    /**
     * Delete Message Template management
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(sysMessageTemplateService.deleteSysMessageTemplateByIds(ids));
    }
}
