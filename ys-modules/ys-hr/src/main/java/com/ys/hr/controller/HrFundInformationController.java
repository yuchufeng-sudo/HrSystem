package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrSuperUsiInfo;
import com.ys.hr.service.IHrSuperUsiInfoService;
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
import com.ys.hr.domain.HrFundInformation;
import com.ys.hr.service.IHrFundInformationService;
import org.springframework.validation.annotation.Validated;

/**
 * Comprehensive fund information Controller
 *
 * @author ys
 * @date 2025-10-17
 */
@RestController
@RequestMapping("/fundInformation")
public class HrFundInformationController extends BaseController
{
    @Autowired
    private IHrFundInformationService hrFundInformationService;

    @Autowired
    private IHrSuperUsiInfoService hrSuperUsiInfoService;

    /**
     * Query Comprehensive fund information list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrSuperUsiInfo hrSuperUsiInfo)
    {
        startPage();
        List<HrSuperUsiInfo> list = hrSuperUsiInfoService.selectHrSuperUsiInfoList(hrSuperUsiInfo);
        return getDataTable(list);
    }

    /**
     * Get Comprehensive fund information details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        HrFundInformation fundInformation = new HrFundInformation();
        fundInformation.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        fundInformation.setEmployeeId(id);
        List<HrFundInformation> hrFundInformations = hrFundInformationService.selectHrFundInformationList(fundInformation);
        if (!hrFundInformations.isEmpty()) {
            HrFundInformation fundInformation1 = hrFundInformations.get(0);
            return success(fundInformation1);
        }else {
            hrFundInformationService.insertHrFundInformation(fundInformation);
            return success(fundInformation);
        }
    }

    /**
     * Update Comprehensive fund information
     */
    @RequiresPermissions("hr:employees:edit")
    @Log(title = "Comprehensive fund information", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrFundInformation hrFundInformation) {
        return toAjax(hrFundInformationService.updateHrFundInformation(hrFundInformation));
    }
}
