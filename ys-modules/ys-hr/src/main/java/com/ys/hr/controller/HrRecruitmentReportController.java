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

    /**
     * Query THE Employee list.
     */
    @GetMapping("/list")
    public TableDataInfo list(HrEmployees hrEmployees) {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<EmergencyContactsReportVo> list = hrEmployeesService.selectEmergencyContactsReportVo(hrEmployees);
        return getDataTable(list);
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
    @PostMapping("/birthDayExport")
    public void birthDayExport(HttpServletResponse response, HrEmployees hrEmployees) {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<BirthdayReportVo> list = hrEmployeesService.selectBirthdayReportVoReportVo(hrEmployees);
        ExcelUtil<BirthdayReportVo> util = new ExcelUtil<BirthdayReportVo>(BirthdayReportVo.class);
        util.exportExcel(response, list, " Employee birthday data");
    }
}
