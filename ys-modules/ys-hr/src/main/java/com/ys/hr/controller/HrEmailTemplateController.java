package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrEmailTemplate;
import com.ys.hr.service.IHrEmailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Email Template Controller
 *
 * Handles HTTP requests for email template management operations.
 * This controller follows the thin controller pattern - it only handles
 * request/response logic and delegates all business logic to the service layer.
 *
 * @author ys
 * @date 2025-09-09
 */
@RestController
@RequestMapping("/template")
public class HrEmailTemplateController extends BaseController {

    @Autowired
    private IHrEmailTemplateService hrEmailTemplateService;

    /**
     * Query email templates list with pagination
     *
     * @param hrEmailTemplate Query parameters for filtering templates
     * @return Paginated table data containing email templates
     */
    @GetMapping("/list")
    public TableDataInfo list(HrEmailTemplate hrEmailTemplate) {
        startPage();
        List<HrEmailTemplate> list = hrEmailTemplateService.selectHrEmailTemplateList(hrEmailTemplate);
        return getDataTable(list);
    }

    /**
     * Export email templates list to Excel
     *
     * @param response HTTP response object to write Excel file
     * @param hrEmailTemplate Query parameters for filtering templates to export
     */
    @RequiresPermissions("system:template:export")
    @Log(title = "Email Templates", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmailTemplate hrEmailTemplate) {
        List<HrEmailTemplate> list = hrEmailTemplateService.selectHrEmailTemplateList(hrEmailTemplate);
        ExcelUtil<HrEmailTemplate> util = new ExcelUtil<>(HrEmailTemplate.class);
        util.exportExcel(response, list, "Email Templates Data");
    }

    /**
     * Get email template details by ID
     *
     * @param templateId The unique identifier of the email template
     * @return Ajax result containing the email template details
     */
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId) {
        return success(hrEmailTemplateService.selectHrEmailTemplateByTemplateId(templateId));
    }

    /**
     * Add a new email template
     *
     * @param hrEmailTemplate The email template object to be created
     * @return Ajax result indicating success or failure
     */
    @RequiresPermissions("system:template:add")
    @Log(title = "Email Templates", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrEmailTemplate hrEmailTemplate) {
        return toAjax(hrEmailTemplateService.insertHrEmailTemplate(hrEmailTemplate));
    }

    /**
     * Update an existing email template
     *
     * @param hrEmailTemplate The email template object with updated information
     * @return Ajax result indicating success or failure
     */
    @RequiresPermissions("system:template:edit")
    @Log(title = "Email Templates", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEmailTemplate hrEmailTemplate) {
        return toAjax(hrEmailTemplateService.updateHrEmailTemplate(hrEmailTemplate));
    }

    /**
     * Send email using the specified template
     *
     * This method delegates all business logic to the service layer,
     * including employee lookup, template data preparation, and email sending.
     *
     * @param hrEmailTemplate The email template containing recipient and content information
     * @return Ajax result indicating whether the email was sent successfully
     */
    @PutMapping("/send")
    public AjaxResult sendEmail(@RequestBody HrEmailTemplate hrEmailTemplate) {
        hrEmailTemplateService.sendEmailWithTemplate(hrEmailTemplate);
        return success("Email sent successfully");
    }

    /**
     * Delete email templates by their IDs
     *
     * @param templateIds Array of template IDs to be deleted
     * @return Ajax result indicating success or failure
     */
    @RequiresPermissions("system:template:remove")
    @Log(title = "Email Templates", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds) {
        return toAjax(hrEmailTemplateService.removeByIds(Arrays.asList(templateIds)));
    }
}
