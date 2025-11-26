package com.ys.hr.controller;

import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.vo.BirthdayReportVo;
import com.ys.hr.domain.vo.DailyTimeVo;
import com.ys.hr.domain.vo.DepartmentsVo;
import com.ys.hr.service.IHrDeptService;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IHrLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * hr dashboard
 */
@RestController
@RequestMapping("/hrDashboard")
public class HrDashboardController extends BaseController {

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Autowired
    private IHrLeaveService hrLeaveService;

    @Autowired
    private IHrDeptService hrDeptService;

    /**
     * Total Employee
     * @return
     */
    @GetMapping("/getEmployeeTotal")
    public AjaxResult getEmployeeTotal(){
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        Map<String, Object> map = hrEmployeesService.selectEmployeeCount(userEnterpriseId);
        return AjaxResult.success(map);
    }


    /**
     * On Leave
     * @return
     */
    @GetMapping("/getLeaveTotal")
    public AjaxResult getLeaveTotal(){
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        Map<String, Object> map = hrLeaveService.selectLeaveTotal(userEnterpriseId);
        return AjaxResult.success(map);
    }

    /**
     * Employeement status
     * @return
     */
    @GetMapping("/getEmployeeStatus")
    public AjaxResult getEmployeeStatus() {
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        Map<String, Object> map =  hrEmployeesService.getEmployeeStatusCount(userEnterpriseId);
        return AjaxResult.success(map);
    }

    /**
     * Departments
     * @return
     */
    @GetMapping("/getDepartments")
    public AjaxResult getDepartments(){
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        List<DepartmentsVo> list = hrDeptService.selectDepartments(userEnterpriseId);
        return AjaxResult.success(list);
    }

    /**
     * Upcoming Birthday
     * @param hrEmployees
     * @return
     */
    @GetMapping("/getUpcomingBirthday")
    public TableDataInfo getUpcomingBirthday(HrEmployees hrEmployees){
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<BirthdayReportVo> list = hrEmployeesService.selectEmployeeBirthday(hrEmployees);
        return getDataTable(list);
    }
}
