package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.hr.service.IHrSignPlatformTypeService;
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
import com.ys.hr.domain.HrSignPlatformType;
import org.springframework.validation.annotation.Validated;

/**
 * Electronic signature platform type Controller
 *
 * @author ys
 * @date 2025-08-08
 */
@RestController
@RequestMapping("/signType")
public class HrSignPlatformTypeController extends BaseController
{
    @Autowired
    private IHrSignPlatformTypeService hrSignPlatformTypeService;

    /**
     * Query Electronic signature platform type list
     */
    @GetMapping("/list")
    public TableDataInfo getList(HrSignPlatformType hrSignPlatformType){
        startPage();
        List<HrSignPlatformType> hrSignPlatformTypes = hrSignPlatformTypeService.selectHrSignPlatformTypeList(hrSignPlatformType);
        return getDataTable(hrSignPlatformTypes);
    }

    /**
     * Get Electronic signature platform type details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return success(hrSignPlatformTypeService.selectHrSignPlatformTypeById(id));
    }

    /**
     * Add Electronic signature platform type
     */
    @Log(title = "Electronic signature platform type", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrSignPlatformType hrSignPlatformType) {
        return toAjax(hrSignPlatformTypeService.insertHrSignPlatformType(hrSignPlatformType));
    }

    /**
     * Update Electronic signature platform type
     */
    @Log(title = "Electronic signature platform type", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrSignPlatformType hrSignPlatformType) {
        return toAjax(hrSignPlatformTypeService.updateHrSignPlatformType(hrSignPlatformType));
    }

    /**
     * Delete Electronic signature platform type
     */
    @Log(title = "Electronic signature platform type", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer ids) {
        return toAjax(hrSignPlatformTypeService.removeById(ids));
    }
}
