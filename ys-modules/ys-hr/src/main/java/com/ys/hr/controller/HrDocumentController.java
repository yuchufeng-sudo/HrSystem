package com.ys.hr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrDocument;
import com.ys.hr.domain.HrDocumentShare;
import com.ys.hr.domain.vo.HrDocumentShareVo;
import com.ys.hr.service.IHrDocumentService;
import com.ys.hr.service.IHrDocumentShareService;
import com.ys.hr.service.impl.HrDocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Document Management Controller
 *
 * @author ys
 * @date 2025-05-27
 */
@RestController
@RequestMapping("/document")
public class HrDocumentController extends BaseController
{
    @Autowired
    private IHrDocumentService hrDocumentService;
    @Autowired
    private IHrDocumentShareService hrDocumentShareService;

    /**
     * Query Document Management list
     */
    @RequiresPermissions("hr:document:list")
    @GetMapping("/myList")
    public TableDataInfo myList(HrDocument hrDocument)
    {
        startPage();
        hrDocument.setUploadUserId(SecurityUtils.getUserId());
        List<HrDocument> list = hrDocumentService.selectHrDocumentList(hrDocument);
        return getDataTable(list);
    }

    @RequiresPermissions("hr:document:list")
    @GetMapping("/shareList")
    public TableDataInfo shareList(HrDocument hrDocument)
    {
        startPage();
        hrDocument.setUserId(SecurityUtils.getUserId());
        hrDocument.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrDocumentShareVo> list = hrDocumentService.selectHrDocumentShareList(hrDocument);
        return getDataTable(list);
    }

    @RequiresPermissions("hr:employees:edit")
    @GetMapping("/myList/{userId}")
    public TableDataInfo myList1(HrDocument hrDocument,@PathVariable("userId") Long userId)
    {
        startPage();
        hrDocument.setUploadUserId(userId);
        List<HrDocument> list = hrDocumentService.selectHrDocumentList(hrDocument);
        return getDataTable(list);
    }

    @RequiresPermissions("hr:employees:edit")
    @GetMapping("/shareList/{userId}")
    public TableDataInfo shareList1(HrDocument hrDocument,@PathVariable("userId") Long userId)
    {
        startPage();
        hrDocument.setUserId(userId);
        hrDocument.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrDocumentShareVo> list = hrDocumentService.selectHrDocumentShareList(hrDocument);
        return getDataTable(list);
    }

    /**
     * Export Document Management list
     */
    @RequiresPermissions("hr:document:export")
    @Log(title = "Document Management", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrDocument hrDocument)
    {
        List<HrDocument> list = hrDocumentService.selectHrDocumentList(hrDocument);
        ExcelUtil<HrDocument> util = new ExcelUtil<HrDocument>(HrDocument.class);
        util.exportExcel(response, list, "Document Management Data");
    }

    /**
     * Get Document Management details
     */
    @GetMapping(value = "/{documentId}")
    public AjaxResult getInfo(@PathVariable("documentId") Long documentId) {
        return success(hrDocumentService.selectHrDocumentByDocumentId(documentId));
    }

    /**
     * Get Document Management details
     */
    @GetMapping(value = "/share/{documentId}")
    public AjaxResult shareInfo(@PathVariable("documentId") Long documentId) {
        return success(hrDocumentShareService.selectHrDocumentShareByDocumentId(documentId));
    }

    /**
     * Add Document Management
     */
    @RequiresPermissions("hr:document:add")
    @Log(title = "Document Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody List<HrDocument> hrDocuments) {
        for (HrDocument hrDocument : hrDocuments) {
            hrDocument.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            hrDocument.setUploadUserId(SecurityUtils.getUserId());
            hrDocument.setUploadDate(DateUtils.getNowDate());
        }
        return toAjax(hrDocumentService.saveBatch(hrDocuments));
    }

    @RequiresPermissions("hr:employees:edit")
    @PostMapping("addShared")
    public AjaxResult addShared(@RequestBody List<HrDocument> hrDocuments) {
        for (HrDocument hrDocument : hrDocuments) {
            hrDocument.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            hrDocument.setUploadUserId(SecurityUtils.getUserId());
            hrDocument.setUploadDate(DateUtils.getNowDate());
        }
        boolean result = hrDocumentService.saveBatch(hrDocuments);
        for (HrDocument hrDocument : hrDocuments) {
            HrDocumentServiceImpl.insertShareHrDocument(hrDocument, hrDocumentShareService);
        }
        return toAjax(result);
    }

    /**
     * Save Share Document Management
     */
    @RequiresPermissions("hr:document:share")
    @Log(title = "Document Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody List<HrDocumentShare> hrDocumentShareList) {
        QueryWrapper<HrDocumentShare> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("document_id",hrDocumentShareList.get(0).getDocumentId());
        hrDocumentShareService.remove(queryWrapper);
        for (HrDocumentShare hrDocumentShare : hrDocumentShareList) {
            hrDocumentShare.setSharedBy(SecurityUtils.getUserId());
            hrDocumentShare.setCreateTime(DateUtils.getNowDate());
        }
        return toAjax(hrDocumentShareService.saveBatch(hrDocumentShareList));
    }

    /**
     * Delete Document Management
     */
    @RequiresPermissions("hr:document:remove")
    @Log(title = "Document Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{documentIds}")
    public AjaxResult remove(@PathVariable Long[] documentIds) {
        QueryWrapper<HrDocumentShare> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("document_id",documentIds);
        hrDocumentShareService.remove(queryWrapper);
        return toAjax(hrDocumentService.removeByIds(Arrays.asList(documentIds)));
    }

    @DeleteMapping("/cut/{documentId}")
    public AjaxResult cut(@PathVariable Long documentId){
        QueryWrapper<HrDocumentShare> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("document_id",documentId);
        queryWrapper.eq("shared_to",SecurityUtils.getUserId());
        return toAjax(hrDocumentShareService.remove(queryWrapper));
    }

}
