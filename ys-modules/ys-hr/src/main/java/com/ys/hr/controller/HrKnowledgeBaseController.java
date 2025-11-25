package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrKnowledgeBase;
import com.ys.hr.service.IHrKnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 *  Knowledge base  Controller
 *
 * @author ys
 * @date 2025-06-04
 */
@RestController
@RequestMapping("/knowledgeBase")
public class HrKnowledgeBaseController extends BaseController
{
    @Autowired
    private IHrKnowledgeBaseService hrKnowledgeBaseService;

    /**
     * Query Knowledge base  list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrKnowledgeBase hrKnowledgeBase)
    {
        startPage();
        hrKnowledgeBase.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrKnowledgeBase> list = hrKnowledgeBaseService.selectHrKnowledgeBaseList(hrKnowledgeBase);
        return getDataTable(list);
    }

    /*
    *
    *
    * */
    @GetMapping("/searchAll")
    public TableDataInfo searchAll(@RequestParam(required = false) String searchParam) {
        startPage();
        //获取企业id
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        List<HrKnowledgeBase> list = hrKnowledgeBaseService.searchAllHrKnowledgeBase(searchParam, userEnterpriseId);
        return getDataTable(list);
    }

    /**
     * Export Knowledge base  list
     */
    @Log(title = "Knowledge base", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrKnowledgeBase hrKnowledgeBase)
    {
        hrKnowledgeBase.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrKnowledgeBase> list = hrKnowledgeBaseService.selectHrKnowledgeBaseList(hrKnowledgeBase);
        ExcelUtil<HrKnowledgeBase> util = new ExcelUtil<HrKnowledgeBase>(HrKnowledgeBase.class);
        util.exportExcel(response, list, " Knowledge base  Data");
    }

    /**
     * Get Knowledge base  details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrKnowledgeBaseService.selectHrKnowledgeBaseById(id));
    }

    /**
     * Add Knowledge base
     */
    @Log(title = "Knowledge base", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrKnowledgeBase hrKnowledgeBase) {
        hrKnowledgeBase.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrKnowledgeBaseService.insertHrKnowledgeBase(hrKnowledgeBase));
    }

    /**
     * Update Knowledge base
     */
    @Log(title = "Knowledge base", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrKnowledgeBase hrKnowledgeBase) {
        return toAjax(hrKnowledgeBaseService.updateHrKnowledgeBase(hrKnowledgeBase));
    }

    /**
     * Delete Knowledge base
     */
    @Log(title = "Knowledge base", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrKnowledgeBaseService.removeByIds(Arrays.asList(ids)));
    }
}
