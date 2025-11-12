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
 *  MESSAGE TEMPLATE 
 MANAGEMENT  Controller
 *
 * @author ruoyi
 * @date 2024-10-23
 */
@RestController
@RequestMapping("/messageTemplate")
public class SysMessageTemplateController extends BaseController
{
    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;

    /**
     * QUERY MESSAGE TEMPLATE 
 MANAGEMENT    LIST
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
     * OBTAIN  MESSAGE TEMPLATE 
 MANAGEMENT  DETAILEDLY INFORMATION
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(sysMessageTemplateService.selectSysMessageTemplateById(id));
    }

    /**
     * ADD MESSAGE TEMPLATE 
 MANAGEMENT  
     */
    @PostMapping
    public AjaxResult add(@RequestBody SysMessageTemplate sysMessageTemplate)
    {
        return toAjax(sysMessageTemplateService.insertSysMessageTemplate(sysMessageTemplate));
    }

    /**
     * MODIFY MESSAGE TEMPLATE 
 MANAGEMENT  
     */
    @PutMapping
    public AjaxResult edit(@RequestBody SysMessageTemplate sysMessageTemplate)
    {
        return toAjax(sysMessageTemplateService.updateSysMessageTemplate(sysMessageTemplate));
    }

    /**
     * DELETE MESSAGE TEMPLATE 
 MANAGEMENT  
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(sysMessageTemplateService.deleteSysMessageTemplateByIds(ids));
    }
}
