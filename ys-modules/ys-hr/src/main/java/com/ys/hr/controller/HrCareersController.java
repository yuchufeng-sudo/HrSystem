package com.ys.hr.controller;

import java.util.List;

import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEnterprise;
import com.ys.hr.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrCareers;
import com.ys.hr.service.IHrCareersService;

/**
 * Company careers information Controller
 *
 * @author ys
 * @date 2025-09-27
 */
@RestController
@RequestMapping("/careers")
public class HrCareersController extends BaseController
{
    @Autowired
    private IHrCareersService hrCareersService;

    /**
     * Get Company careers information details
     */
    @GetMapping()
    public AjaxResult getInfo() {
        String enterpriseId = SecurityUtils.getUserEnterpriseId();
        HrCareers careers = hrCareersService.getOrCreateEnterpriseCareer(enterpriseId);
        return success(careers);
    }

    /**
     * Update Company careers information
     */
    @RequiresPermissions("hr:careers:add")
    @PostMapping("add")
    public AjaxResult add(@RequestBody HrEnterprise hrEnterprise) {
        HrCareers hrCareers = new HrCareers();
        hrCareers.setEnterpriseId(hrEnterprise.getId());
        hrCareers.setCareersName(hrEnterprise.getEnterpriseName());
        hrCareers.setLogo(hrEnterprise.getLogoUrl());
        return toAjax(hrCareersService.insertHrCareers(hrCareers));
    }

    /**
     * Update Company careers information
     */
    @RequiresPermissions("hr:careers:edit")
    @Log(title = "Company careers information", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrCareers hrCareers) {
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        HrCareers existing = hrCareersService.selectHrCareersById(hrCareers.getId());

        if (existing == null) {
            return error("Career information not found");
        }
        if (!existing.getEnterpriseId().equals(userEnterpriseId)) {
            return error("Unauthorized access");
        }

        return toAjax(hrCareersService.updateHrCareers(hrCareers));
    }
}
