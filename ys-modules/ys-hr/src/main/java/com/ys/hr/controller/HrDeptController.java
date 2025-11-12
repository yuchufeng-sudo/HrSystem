package com.ys.hr.controller;

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
import com.ys.hr.service.IHrDeptService;
import com.ys.hr.service.IHrEmployeesService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Dept Controller
 *
 * @author ys
 * @date 2025-06-04
 */
@RestController
@RequestMapping("/dept")
public class HrDeptController extends BaseController
{
    @Autowired
    private IHrDeptService hrDeptService;

    @Autowired
    private IHrEmployeesService hrEmployeesService;
    /**
     * Query Dept list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrDept hrDept)
    {
        startPage();
        hrDept.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrDept> list = hrDeptService.selectHrDeptList(hrDept);
        return getDataTable(list);
    }

    /**
     * Export Dept list
     */
    @RequiresPermissions("hr:dept:export")
    @Log(title = "Dept", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrDept hrDept)
    {
        hrDept.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrDept> list = hrDeptService.selectHrDeptList(hrDept);
        ExcelUtil<HrDept> util = new ExcelUtil<HrDept>(HrDept.class);
        util.exportExcel(response, list, "Dept Data");
    }

    /**
     * Get Dept details
     */
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable("deptId") Long deptId) {
        return success(hrDeptService.selectHrDeptByDeptId(deptId));
    }

    /**
     * Add Dept
     */
    @RequiresPermissions("hr:dept:add")
    @Log(title = "Dept", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrDept hrDept) {
        hrDept.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrDeptService.insertHrDept(hrDept));
    }

    /**
     * Update Dept
     */
    @RequiresPermissions("hr:dept:edit")
    @Log(title = "Dept", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrDept hrDept) {
        return toAjax(hrDeptService.updateHrDept(hrDept));
    }

    /**
     * Delete Dept
     */
    @RequiresPermissions("hr:dept:remove")
    @Log(title = "Dept", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        return toAjax(hrDeptService.deleteHrDeptByDeptId(deptId));
    }

    /**
     * To query the joined DEPARTMENT.

     * @return
     */
    @GetMapping("/getJoinDeptList")
    public TableDataInfo getJoinDeptList(HrDept hrDept){
        Long userId = SecurityUtils.getUserId();
        startPage();
        HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesByUserId(userId);
        if(ObjectUtils.isNotEmpty(hrEmployees)){
            hrDept.setEmpId(hrEmployees.getEmployeeId());
            List<HrDept> list = hrDeptService.getDeptList(hrDept);
            return getDataTable(list);
        }
        return getDataTable(new ArrayList<>());
    }
}
