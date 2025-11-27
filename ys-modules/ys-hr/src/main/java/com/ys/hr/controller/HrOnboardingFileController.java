package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
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
import com.ys.hr.domain.HrOnboardingFile;
import com.ys.hr.service.IHrOnboardingFileService;
import org.springframework.validation.annotation.Validated;

/**
 * Onboarding File Controller
 *
 * @author ys
 * @date 2025-10-13
 */
@Slf4j
@RestController
@RequestMapping("/onboardingFile")
public class HrOnboardingFileController extends BaseController
{
    @Autowired
    private IHrOnboardingFileService hrOnboardingFileService;


    @GetMapping("/listByEmp")
    public TableDataInfo listByEmp(HrOnboardingFile hrOnboardingFile)
    {
        hrOnboardingFile.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrOnboardingFile> list = hrOnboardingFileService.selectHrOnboardingFileListByEmp(hrOnboardingFile);
        return getDataTable(list);
    }

    /**
     * Query Onboarding File list
     */
    @RequiresPermissions("hr:onboarding:list")
    @GetMapping("/list")
    public TableDataInfo list(HrOnboardingFile hrOnboardingFile)
    {
        hrOnboardingFile.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrOnboardingFile> list = hrOnboardingFileService.selectHrOnboardingFileList(hrOnboardingFile);
        return getDataTable(list);
    }

    /**
     * Get Onboarding File details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        try {
            HrOnboardingFile file = hrOnboardingFileService.selectHrOnboardingFileById(id);
            if (file == null) {
                return AjaxResult.error("Onboarding file not found");
            }
            return success(file);
        } catch (Exception e) {
            log.error("Error retrieving onboarding file: {}", id, e);
            return AjaxResult.error("Failed to retrieve onboarding file");
        }
    }

    /**
     * Add Onboarding File
     */
    @RequiresPermissions("hr:onboarding:add")
    @Log(title = "Onboarding File", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrOnboardingFile hrOnboardingFile) {
        hrOnboardingFile.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrOnboardingFileService.insertHrOnboardingFile(hrOnboardingFile));
    }

    /**
     * Update Onboarding File
     */
    @RequiresPermissions("hr:onboarding:edit")
    @Log(title = "Onboarding File", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrOnboardingFile hrOnboardingFile) {
        return toAjax(hrOnboardingFileService.updateHrOnboardingFile(hrOnboardingFile));
    }

    /**
     * Delete Onboarding File
     */
    @RequiresPermissions("hr:onboarding:remove")
    @Log(title = "Onboarding File", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(hrOnboardingFileService.removeByIds(Arrays.asList(ids)));
    }
}
