package com.ys.hr.controller;

import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.vo.EmployeeBirthdayReportVo;
import com.ys.hr.domain.vo.EmployeeContactsReportVo;
import com.ys.hr.service.IHrReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  EMPLOYEE  HOLIDAY  Controller
 *
 * @author ys
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/report")
public class HrReportController extends BaseController
{
    @Autowired
    private IHrReportService hrReportService;

    /**
     * Employee Birthday Report
     */
    @RequiresPermissions("hr:report:employeeBirthday")
    @GetMapping("/employeeBirthday")
    public TableDataInfo employeeBirthday(String searchValue)
    {
        startPage();
        List<EmployeeBirthdayReportVo> list = hrReportService.selectEmployeeBirthdayReportVo(searchValue);
        return getDataTable(list);
    }

    /**
     * Emergency Contacts Report
     */
    @RequiresPermissions("hr:report:employeeContacts")
    @GetMapping("/employeeContacts")
    public TableDataInfo employeeContacts(String searchValue)
    {
        startPage();
        List<EmployeeContactsReportVo> list = hrReportService.selectEmployeeContactsReportVo(searchValue);
        return getDataTable(list);
    }
}
