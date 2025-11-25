package com.ys.hr.controller;

import com.ys.common.core.aspect.RateLimit;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrDept;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrPosition;
import com.ys.hr.domain.excel.HrEmployeesExcel;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.service.IHrDeptService;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IHrPositionService;
import com.ys.system.api.domain.SysRole;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *  Employee Controller
 *
 * @author ys
 * @date 2025-05-21
 */
@RestController
@RequestMapping("/employees")
public class HrEmployeesController extends BaseController
{
    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Autowired
    private IHrDeptService deptService;

    @Autowired
    private IHrPositionService positionService;

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;
    /**
     * Query THE Employee list.
     */
    @GetMapping("/list")
    public TableDataInfo list(HrEmployees hrEmployees)
    {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrEmployees> list = hrEmployeesService.selectHrEmployeesList(hrEmployees);
        return getDataTable(list);
    }

    @GetMapping("/listAll")
    public TableDataInfo listAll(HrEmployees hrEmployees)
    {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrEmployees> list = hrEmployeesService.selectHrEmployeesList(hrEmployees);
        return getDataTable(list);
    }

    /**
     * Export Employee   list
     */
    @RequiresPermissions("hr:employees:export")
    @Log(title = "Employee", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmployees hrEmployees)
    {
        hrEmployees.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrEmployeesExcel> list = hrEmployeesService.selectHrEmployeesExcelList(hrEmployees);
        ExcelUtil<HrEmployeesExcel> util = new ExcelUtil<>(HrEmployeesExcel.class);
        util.exportExcel(response, list, " Employee data");
    }

    /**
     * Get Employee Details
     */
    @RequiresPermissions("hr:employees:query")
    @GetMapping(value = "/{employeeId}")
    public AjaxResult getInfo(@PathVariable("employeeId") Long employeeId) {
        HrEmployees byId = hrEmployeesService.selectHrEmployeesById(employeeId);
        if (!Objects.equals(byId.getEnterpriseId(), SecurityUtils.getUserEnterpriseId())) {
            return AjaxResult.error("Employee does not exist");
        }
        HrPosition byId1 = positionService.getById(byId.getPosition());
        if (ObjectUtils.isNotEmpty(byId1)){
            byId.setPositionName(byId1.getPositionName());
        }
        return success(byId);
    }

    @GetMapping(value = "/getUserId")
    public AjaxResult getEmployeesByUserId() {
        HrEmployees byId = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());
        return success(byId);
    }

    /**
     * Add Employee
     */
    @RequiresPermissions("hr:employees:add")
    @Log(title = "Employee", businessType = BusinessType.INSERT)
    @PostMapping
    @RateLimit(limit = 5, prefix = "employees:add")
    public AjaxResult add(@Validated @RequestBody HrEmployees hrEmployees) {
        if (hrEmployees.getAccessLevel()==null) {
            Long roleId = hrEmployeesMapper.selectRoleByKey("user", SecurityUtils.getUserEnterpriseId());
            hrEmployees.setAccessLevel(String.valueOf(roleId));
        }else {
            SysRole sysRole = hrEmployeesMapper.selectSysRoleById(Long.valueOf(hrEmployees.getAccessLevel()));
            if (sysRole==null||!sysRole.getEnterpriseId().equals(SecurityUtils.getUserEnterpriseId())) {
                return AjaxResult.error("AccessLevel does not exist");
            }
        }
        return success(hrEmployeesService.insertEmployees(hrEmployees));
    }

    /**
     * Add Employee
     */
    @RequiresPermissions("system:enterprise:add")
    @Log(title = "Employee", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @RateLimit(limit = 5, prefix = "employees:add")
    public AjaxResult addEmployees(@Validated @RequestBody HrEmployees hrEmployees) {
        return success(hrEmployeesService.insertEmployees(hrEmployees));
    }

    /**
     * Update Employee
     */
    @RequiresPermissions("hr:employees:edit")
    @Log(title = "Employee", businessType = BusinessType.UPDATE)
    @PutMapping
    @RateLimit(limit = 5, prefix = "employees:edit")
    public AjaxResult edit(@RequestBody HrEmployees hrEmployees) {
        if(ObjectUtils.isNotEmpty(hrEmployees.getPayrollId())){
            HrEmployees hrEmployees1 = new HrEmployees();
            hrEmployees1.setPayrollId(hrEmployees.getPayrollId());
            hrEmployees1.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            List<HrEmployees> hrEmployees2 = hrEmployeesService.selectHrEmployeesList(hrEmployees1);
            if(ObjectUtils.isNotEmpty(hrEmployees2) && !Objects.equals(hrEmployees2.get(0).getUserId(), hrEmployees.getUserId())){
                return AjaxResult.warn("The payroll ID has been used");
            }
        }
        return AjaxResult.success(hrEmployeesService.updateEmployees(hrEmployees));
    }

    @PutMapping("/init")
    public AjaxResult editByFirst(@RequestBody HrEmployees hrEmployees) {
        HrEmployees employees = new HrEmployees();
        if(ObjectUtils.isNotEmpty(hrEmployees.getUserId())){
            employees  = hrEmployeesService.selectHrEmployeesByUserId(hrEmployees.getUserId());
        }
        hrEmployees.setEmployeeId(employees.getEmployeeId());
        return AjaxResult.success(hrEmployeesService.updateEmployees(hrEmployees));
    }

    /**
     * Delete Employee
     */
    @RequiresPermissions("hr:employees:remove")
    @Log(title = "Employee", businessType = BusinessType.DELETE)
    @DeleteMapping("/{employeeId}")
    public AjaxResult remove(@PathVariable Long employeeId) {
        HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesById(employeeId);
        if (!Objects.equals(hrEmployees.getEnterpriseId(), SecurityUtils.getUserEnterpriseId())) {
            return AjaxResult.error("Employee does not exist");
        }
        HrDept hrDept = new HrDept();
        hrDept.setLeader(employeeId);
        List<HrDept> hrDepts = deptService.selectHrDeptList(hrDept);
        if (!hrDepts.isEmpty()) {
            return AjaxResult.error("This staff member is currently assigned as the Department Head for "+hrDepts.get(0).getDeptName()+". Please assign a new Department Head before proceeding with the resignation.");
        }
        return toAjax(hrEmployeesService.deleteById(employeeId));
    }

    /**
     * Delete Employee
     */
    @RequiresPermissions("hr:employees:remove")
    @Log(title = "Employee", businessType = BusinessType.DELETE)
    @PutMapping("/resign")
    public AjaxResult resign(@RequestBody HrEmployees employees) {
        HrDept hrDept = new HrDept();
        hrDept.setLeader(employees.getEmployeeId());
        List<HrDept> hrDepts = deptService.selectHrDeptList(hrDept);
        if (!hrDepts.isEmpty()) {
            return AjaxResult.error("This staff member is currently assigned as the Department Head for "+hrDepts.get(0).getDeptName()+". Please assign a new Department Head before proceeding with the resignation.");
        }
        Date resignationDate = employees.getResignationDate();
        if (resignationDate!=null) {
            LocalDate resignationLocalDate = resignationDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate currentLocalDate = LocalDate.now();

            if (currentLocalDate.isAfter(resignationLocalDate)) {
                return AjaxResult.error("Resignation Date not immediate");
            }
        }
        return toAjax(hrEmployeesService.resignEmployees(employees));
    }

    @RequiresPermissions("hr:employees:remove")
    @PutMapping("/resignCancel")
    public AjaxResult resignCancel(@RequestBody HrEmployees employees) {
        return toAjax(hrEmployeesService.resignCancelEmployees(employees));
    }

    @PutMapping("/restore/{employeeId}")
    public AjaxResult restore(@PathVariable Long employeeId) {
        return toAjax(hrEmployeesService.restoreEmployees(employeeId));
    }

    @GetMapping("/selectByEmail")
    public AjaxResult selectByEmail(String email)
    {
        HrEmployees hrEmployees = new HrEmployees();
        hrEmployees.setEmail(email);
        List<HrEmployees> list = hrEmployeesService.selectHrEmployeesList(hrEmployees);
        if (list.isEmpty()){
            return AjaxResult.error("The email user does not exist.");
        }
        HrEmployees hrEmployees1 = list.get(0);
        if (hrEmployees1.getUserId().equals(SecurityUtils.getUserId())) {
            return AjaxResult.error("You cannot invite yourself.");
        }
        HrEmployees hrEmployees2 = new HrEmployees();
        hrEmployees2.setEmail(hrEmployees1.getEmail());
        hrEmployees2.setUserId(hrEmployees1.getUserId());
        hrEmployees2.setFullName(hrEmployees1.getFullName());
        hrEmployees2.setAvatarUrl(hrEmployees1.getAvatarUrl());
        return AjaxResult.success(hrEmployees2);
    }
}
