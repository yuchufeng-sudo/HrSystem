package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrKnowledgeBaseSort;
import com.ys.hr.service.IHrKnowledgeBaseSortService;
import org.springframework.validation.annotation.Validated;

/**
 *  Knowledge base classification Controller
 *
 * @author ys
 * @date 2025-06-04
 */
@RestController
@RequestMapping("/knowledgeSort")
public class HrKnowledgeBaseSortController extends BaseController
{
    @Autowired
    private IHrKnowledgeBaseSortService hrKnowledgeBaseSortService;

    /**
     * Query Knowledge base classification list
     */
//    @RequiresPermissions("hr:knowledgeSort:list")
    @GetMapping("/list")
    public TableDataInfo list(HrKnowledgeBaseSort hrKnowledgeBaseSort)
    {
        startPage();
        hrKnowledgeBaseSort.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrKnowledgeBaseSort> list = hrKnowledgeBaseSortService.selectHrKnowledgeBaseSortList(hrKnowledgeBaseSort);
        return getDataTable(list);
    }

    /**
     * Export Knowledge base classification list
     */
//    @RequiresPermissions("hr:knowledgeSort:export")
    @Log(title = "Knowledge base classification", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrKnowledgeBaseSort hrKnowledgeBaseSort)
    {
        hrKnowledgeBaseSort.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrKnowledgeBaseSort> list = hrKnowledgeBaseSortService.selectHrKnowledgeBaseSortList(hrKnowledgeBaseSort);
        ExcelUtil<HrKnowledgeBaseSort> util = new ExcelUtil<HrKnowledgeBaseSort>(HrKnowledgeBaseSort.class);
        util.exportExcel(response, list, " Knowledge base classification Data");
    }

    /**
     * Get Knowledge base classification details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrKnowledgeBaseSortService.selectHrKnowledgeBaseSortById(id));
    }

    /**
     * Add Knowledge base classification
     */
//    @RequiresPermissions("hr:knowledgeSort:add")
    @Log(title = "Knowledge base classification", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrKnowledgeBaseSort hrKnowledgeBaseSort) {
        hrKnowledgeBaseSort.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrKnowledgeBaseSortService.insertHrKnowledgeBaseSort(hrKnowledgeBaseSort));
    }

    /**
     * Update Knowledge base classification
     */
//    @RequiresPermissions("hr:knowledgeSort:edit")
    @Log(title = "Knowledge base classification", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrKnowledgeBaseSort hrKnowledgeBaseSort) {
        return toAjax(hrKnowledgeBaseSortService.updateHrKnowledgeBaseSort(hrKnowledgeBaseSort));
    }

    /**
     * Delete Knowledge base classification
     */
//    @RequiresPermissions("hr:knowledgeSort:remove")
    @Log(title = "Knowledge base classification", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrKnowledgeBaseSortService.removeByIds(Arrays.asList(ids)));
    }


    /**
     * 
     */
    @GetMapping("/tree")
    public AjaxResult getTree() {
        String enterpriseId = SecurityUtils.getUserEnterpriseId();
        List<HrKnowledgeBaseSort> tree = hrKnowledgeBaseSortService.buildTree(0L, enterpriseId);
        return AjaxResult.success(tree);
    }


    @GetMapping("/searchTree")
    public AjaxResult searchTree(@RequestParam(value = "searchKey", required = false) String searchKey) {
        String enterpriseId = SecurityUtils.getUserEnterpriseId();
        List<HrKnowledgeBaseSort> tree = hrKnowledgeBaseSortService.searchTree(searchKey, enterpriseId);
        return AjaxResult.success(tree);
    }

    @GetMapping("/searchTree2")
    public AjaxResult searchTree2(@RequestParam(value = "sortId", required = false) Long sortId) {
        String enterpriseId = SecurityUtils.getUserEnterpriseId();
        List<HrKnowledgeBaseSort> tree = hrKnowledgeBaseSortService.searchTree2(sortId, enterpriseId);
        return AjaxResult.success(tree);
    }
}
