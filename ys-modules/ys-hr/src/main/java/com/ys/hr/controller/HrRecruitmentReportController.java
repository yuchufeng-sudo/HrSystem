package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.*;
import com.ys.hr.domain.vo.AddressReportVo;
import com.ys.hr.domain.vo.AnnualPayrollStatisticsVo;
import com.ys.hr.domain.vo.BirthdayReportVo;
import com.ys.hr.domain.vo.EmergencyContactsReportVo;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IHrPayrollService;
import com.ys.hr.service.ITbCandidateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author：hzz
 * @Date ：2025/6/11 14:41
 */
@RestController
@RequestMapping("/recruitmentReport")
public class HrRecruitmentReportController extends BaseController {

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Autowired
    private IHrPayrollService hrPayrollService;

    @Autowired
    private ITbCandidateInfoService tbCandidateInfoService;


    /**
     * Query THE Employee list.
     */
//    @RequiresPermissions("hr:employees:list")
    @GetMapping("/list")
    public TableDataInfo list(HrEmployees hrEmployees) {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<EmergencyContactsReportVo> list = hrEmployeesService.selectEmergencyContactsReportVo(hrEmployees);
        return getDataTable(list);
    }


    //Export Employee Emergency Contact
//    @RequiresPermissions("hr:employees:export")
    @Log(title = "Employee Emergency Contact data", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmployees hrEmployees) {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<EmergencyContactsReportVo> list = hrEmployeesService.selectEmergencyContactsReportVo(hrEmployees);
        ExcelUtil<EmergencyContactsReportVo> util = new ExcelUtil<EmergencyContactsReportVo>(EmergencyContactsReportVo.class);
        util.exportExcel(response, list, " Employee Emergency Contact data");
    }


    @GetMapping("/birthDaylist")
    public TableDataInfo birthDaylist(HrEmployees hrEmployees) {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<BirthdayReportVo> list = hrEmployeesService.selectBirthdayReportVoReportVo(hrEmployees);
        return getDataTable(list);
    }

    //Export employee birthdays.
    @Log(title = "Employee birthday data", businessType = BusinessType.EXPORT)
    @PostMapping("/birthDayexport")
    public void birthDayexport(HttpServletResponse response, HrEmployees hrEmployees) {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<BirthdayReportVo> list = hrEmployeesService.selectBirthdayReportVoReportVo(hrEmployees);
        ExcelUtil<BirthdayReportVo> util = new ExcelUtil<BirthdayReportVo>(BirthdayReportVo.class);
        util.exportExcel(response, list, " Employee birthday data");
    }


    // Employee salary statistics
    @GetMapping("/payrollList")
    public TableDataInfo payrollList(HrPayroll hrPayroll) {
        hrPayroll.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<AnnualPayrollStatisticsVo> list = hrPayrollService.selectAnnualPayrollStatisticsList(hrPayroll);
        return getDataTable(list);
    }

    //Export annual salary.
    @Log(title = "Annual salary data", businessType = BusinessType.EXPORT)
    @PostMapping("/payrollExport")
    public void payrollExport(HttpServletResponse response, HrPayroll hrPayroll) {
        hrPayroll.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<AnnualPayrollStatisticsVo> list = hrPayrollService.selectAnnualPayrollStatisticsList(hrPayroll);
        ExcelUtil<AnnualPayrollStatisticsVo> util = new ExcelUtil<AnnualPayrollStatisticsVo>(AnnualPayrollStatisticsVo.class);
        util.exportExcel(response, list, "Annual salary data");
    }

    // Candidate Status  list
    @GetMapping("/candidateStatus")
    public TableDataInfo candidateStatus(HrCandidateInfo hrCandidateInfo) {
        hrCandidateInfo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrCandidateInfo> list = tbCandidateInfoService.selectTbCandidateInfoListByStatus(hrCandidateInfo);
        return getDataTable(list);
    }


    @GetMapping("/addressList")
    public TableDataInfo addressList(HrEmployees hrEmployees) {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<AddressReportVo> list = hrEmployeesService.selectAddressReportVoReportVo(hrEmployees);
        return getDataTable(list);
    }


    @Log(title = "Employee birthday data", businessType = BusinessType.EXPORT)
    @PostMapping("/addressExport")
    public void addressExport(HttpServletResponse response, HrEmployees hrEmployees) {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<AddressReportVo> list = hrEmployeesService.selectAddressReportVoReportVo(hrEmployees);
        ExcelUtil<AddressReportVo> util = new ExcelUtil<AddressReportVo>(AddressReportVo.class);
        util.exportExcel(response, list, " Employee Address Data");
    }
}
