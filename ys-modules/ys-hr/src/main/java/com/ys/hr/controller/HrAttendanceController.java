package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrAttendance;
import com.ys.hr.domain.HrEmpPayrollDetail;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.service.IHrAttendanceService;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.utils.excel.EasypoiExcelUtils;
import com.ys.utils.vo.HrAttendanceVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * Attendance RecordController
 *
 * @author ys
 * @date 2025-05-26
 */
@RestController
@RequestMapping("/attendance")
public class HrAttendanceController extends BaseController
{
    @Autowired
    private IHrAttendanceService hrAttendanceService;

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    /**
     * QUERYAttendance Record  LIST
     */
    @RequiresPermissions("hr:attendance:list")
    @GetMapping("/list")
    public TableDataInfo list(HrAttendance hrAttendance)
    {
        if(ObjectUtils.isNotEmpty(hrAttendance.getFlag())){
            hrAttendance.setUserId(SecurityUtils.getUserId());
        }else{
            hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        }
        startPage();
        List<HrAttendance> list = hrAttendanceService.selectHrAttendanceList(hrAttendance);
        return getDataTable(list);
    }

    /**
     * EXPORTAttendance Record  LIST
     */
    @RequiresPermissions("hr:attendance:export")
    @Log(title = "Attendance Record", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrAttendance hrAttendance)
    {
        hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrAttendance> list = hrAttendanceService.selectHrAttendanceList(hrAttendance);
        ExcelUtil<HrAttendance> util = new ExcelUtil<HrAttendance>(HrAttendance.class);
        util.exportExcel(response, list, "Attendance Record data");
    }

    /**
     * OBTAIN Attendance RecordDETAILEDLY INFORMATION
     */
    @GetMapping(value = "/{attendanceId}")
    public AjaxResult getInfo(@PathVariable("attendanceId") Long attendanceId) {
        return success(hrAttendanceService.getById(attendanceId));
    }

    /**
     * ADDAttendance Record
     */
    @RequiresPermissions("hr:attendance:add")
    @Log(title = "Attendance Record", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrAttendance hrAttendance) {
        return toAjax(hrAttendanceService.save(hrAttendance));
    }

    /**
     * MODIFYAttendance Record
     */
    @RequiresPermissions("hr:attendance:edit")
    @Log(title = "Attendance Record", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrAttendance hrAttendance) {
        hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrAttendanceService.updateByIdNew(hrAttendance));
    }

    /**
     * DELETEAttendance Record
     */
    @RequiresPermissions("hr:attendance:remove")
    @Log(title = "Attendance Record", businessType = BusinessType.DELETE)
    @DeleteMapping("/{attendanceIds}")
    public AjaxResult remove(@PathVariable Long[] attendanceIds) {
        return toAjax(hrAttendanceService.removeByIds(Arrays.asList(attendanceIds)));
    }

    /**
     * Import Position INFORMATION
     */
    @RequiresPermissions("hr:attendance:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        List<HrAttendanceVo> userList = EasypoiExcelUtils.importExcel(file, 1, 1, HrAttendanceVo.class);
        String message = hrAttendanceService.importData(userList,SecurityUtils.getUserEnterpriseId());
        return AjaxResult.success(message);
    }

    @GetMapping("/countTime")
    public AjaxResult countTime() {
        return AjaxResult.success(hrAttendanceService.getCountTime(SecurityUtils.getUserEnterpriseId()));
    }

    @GetMapping("/countByUserId")
    public AjaxResult countByUserId() {
        return AjaxResult.success(hrAttendanceService.getCountByUserId(SecurityUtils.getUserId()));
    }

    /**
     * QUERYAttendance Record  LIST
     */
    @GetMapping("/listByHr")
    public AjaxResult listByHr(HrAttendance hrAttendance)
    {
        hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrEmployees> list = hrAttendanceService.selectHrAttendanceListByHr(hrAttendance);
        return AjaxResult.success(list);
    }

    @GetMapping("/listByUser")
    public TableDataInfo listByUser(HrAttendance hrAttendance)
    {
        hrAttendance.setUserId(SecurityUtils.getUserId());
        hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return hrAttendanceService.selectHrAttendanceListByUser(hrAttendance);
    }

    @GetMapping("/listByWeekly")
    public AjaxResult getCountByWeekly()
    {
        HrAttendance hrAttendance = new HrAttendance();
        hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<Map<String, Object>> map = hrAttendanceService.getCountByWeekly(hrAttendance);
        return AjaxResult.success(map);
    }

    @GetMapping("/listByWeeklyRate")
    public AjaxResult getCountByWeeklyRate()
    {
        HrAttendance hrAttendance = new HrAttendance();
        hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        Map<String, Object> map = hrAttendanceService.getCountByWeeklyRate(hrAttendance);
        return AjaxResult.success(map);
    }

    @GetMapping("/getPayrollsByEmp")
    public AjaxResult getPayrollsByEmp(HrAttendance hrAttendance)
    {
        hrAttendance.setUserId(SecurityUtils.getUserId());
        hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        ArrayList<HrEmpPayrollDetail> hrEmpPayrollDetail = hrAttendanceService.getPayrollsByEmp(hrAttendance);
        return AjaxResult.success(hrEmpPayrollDetail);
    }

    //        ArrayList<HrEmpPayrollDetail> hrEmpPayrollDetail = hrAttendanceService.getPayrollsByHr(hrAttendance);
    @RequiresPermissions("hr:payroll:list")
    @GetMapping("/getPayrollsByHr")
    public TableDataInfo getPayrollsByHr(HrAttendance hrAttendance)
    {
        HrEmployees hrEmployees = new HrEmployees();
        Object pageNum = hrAttendance.getParams().get("pageNum");
        Object pageSize = hrAttendance.getParams().get("pageSize");
        // 确保 params Map 已初始化
        if (hrEmployees.getParams() == null) {
            hrEmployees.setParams(new HashMap<>());
        }
        if (hrAttendance.getParams() != null) {
            hrEmployees.getParams().put("pageNum", hrAttendance.getParams().get("pageNum"));
            hrEmployees.getParams().put("pageSize", hrAttendance.getParams().get("pageSize"));
        }
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrEmployees> hrEmployees1 = hrEmployeesService.selectHrEmployeesList(hrEmployees);
        TableDataInfo empTable = getDataTable(hrEmployees1);
        hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrEmpPayrollDetail> hrEmpPayrollDetail = hrAttendanceService.selectPayrollsByHr(hrAttendance,hrEmployees1);
        TableDataInfo dataTableByAttendance = getDataTable(hrEmpPayrollDetail);
        dataTableByAttendance.setTotal(empTable.getTotal());
//        return AjaxResult.success(hrEmpPayrollDetail);
        return dataTableByAttendance;
    }


    @RequiresPermissions("hr:payroll:list")
    @GetMapping("/getPayrollsCount")
    public AjaxResult getPayrollsCount(HrAttendance hrAttendance)
    {
        hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<Map> hrEmpPayrollDetail = hrAttendanceService.getPayrollsCount(hrAttendance);
        return AjaxResult.success(hrEmpPayrollDetail);
    }

    /**
     * OBTAIN This week Attendance
     * @return
     */
    @GetMapping("/getUserWeekAttendance")
    public AjaxResult getUserWeekAttendance(){
        List<HrAttendance> list = hrAttendanceService.getUserWeekAttendance(SecurityUtils.getUserId());
        return AjaxResult.success(list);
    }
}
