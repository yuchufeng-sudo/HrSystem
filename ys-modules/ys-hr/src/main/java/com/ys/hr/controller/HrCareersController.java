package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEnterprise;
import com.ys.hr.domain.vo.EnterpriseVo;
import com.ys.hr.service.EnterpriseService;
import com.ys.hr.service.IHrEnterpriseRealNameInfoService;
import com.ys.system.api.model.LoginUser;
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
import com.ys.hr.domain.HrCareers;
import com.ys.hr.service.IHrCareersService;
import org.springframework.validation.annotation.Validated;

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

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * Get Company careers information details
     */
    @GetMapping()
    public AjaxResult getInfo() {
        HrCareers hrCareers = new HrCareers();
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        hrCareers.setEnterpriseId(userEnterpriseId);
        List<HrCareers> hrCareers1 = hrCareersService.selectHrCareersList(hrCareers);
        if (hrCareers1.isEmpty()) {
            String enterpriseName = SecurityUtils.getLoginUser().getEnterpriseName();
            String enterpriseLogo = SecurityUtils.getLoginUser().getEnterpriseLogo();
            hrCareers.setCareersName(enterpriseName);
            hrCareers.setLogo(enterpriseLogo);
            hrCareersService.insertHrCareers(hrCareers);
            return success(hrCareers);
        }else {
            return success(hrCareers1.get(0));
        }
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
        return toAjax(hrCareersService.updateHrCareers(hrCareers));
    }
}
