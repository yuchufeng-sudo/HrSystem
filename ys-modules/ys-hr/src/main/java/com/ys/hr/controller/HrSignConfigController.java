package com.ys.hr.controller;

import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrSignConfig;
import com.ys.hr.domain.HrSignPlatformType;
import com.ys.hr.service.IHrSignConfigService;
import com.ys.hr.service.IHrSignPlatformTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Electronic signature platform configuration Controller
 *
 * @author ys
 * @date 2025-06-12
 */
@RestController
@RequestMapping("/signConfig")
public class HrSignConfigController extends BaseController
{
    @Autowired
    private IHrSignConfigService hrSignConfigService;

    @Autowired
    private IHrSignPlatformTypeService hrSignPlatformTypeService;


    /**
     * Query Electronic signature platform configuration list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrSignConfig hrSignConfig)
    {
        hrSignConfig.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrSignConfig> list = hrSignConfigService.selectHrSignConfigList(hrSignConfig);
        return getDataTable(list);
    }

    /**
     * Export Electronic signature platform configuration list
     */
    @Log(title = "Electronic signature platform configuration", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrSignConfig hrSignConfig)
    {
        List<HrSignConfig> list = hrSignConfigService.selectHrSignConfigList(hrSignConfig);
        ExcelUtil<HrSignConfig> util = new ExcelUtil<HrSignConfig>(HrSignConfig.class);
        util.exportExcel(response, list, "Electronic signature platform configuration Data");
    }

    /**
     * Get Electronic signature platform configuration details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrSignConfigService.selectHrSignConfigById(id));
    }

    /**
     * Add Electronic signature platform configuration
     */
    @Log(title = "Electronic signature platform configuration", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrSignConfig hrSignConfig) {
        hrSignConfig.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrSignConfigService.insertHrSignConfig(hrSignConfig));
    }

    /**
     * Update Electronic signature platform configuration
     */
    @Log(title = "Electronic signature platform configuration", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrSignConfig hrSignConfig) {
        return toAjax(hrSignConfigService.updateHrSignConfig(hrSignConfig));
    }

    /**
     * Delete Electronic signature platform configuration
     */
    @Log(title = "Electronic signature platform configuration", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrSignConfigService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * Status modification
     */
    @Log(title = "Electronic signature platform configuration", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody HrSignConfig hrSignConfig)
    {
        //If the Status is enabled, other enabled Statuses need to be handled.
        if (hrSignConfig.getStatus().equals("1")){
            hrSignConfigService.updateByStatus();
        }
        hrSignConfig.setUpdateBy(SecurityUtils.getUserId().toString());
        hrSignConfig.setUpdateTime(DateUtils.getNowDate());
        return toAjax(hrSignConfigService.updateHrSignConfig(hrSignConfig));
    }

    @GetMapping("/getPlatformTypeList")
    public AjaxResult getPlatformTypeList(){
        HrSignPlatformType hrSignPlatformType = new HrSignPlatformType();
        hrSignPlatformType.setIsDisabled(0);
        List<HrSignPlatformType> hrSignPlatformTypes = hrSignPlatformTypeService.selectHrSignPlatformTypeList(hrSignPlatformType);
        return AjaxResult.success(hrSignPlatformTypes);
    }
}
