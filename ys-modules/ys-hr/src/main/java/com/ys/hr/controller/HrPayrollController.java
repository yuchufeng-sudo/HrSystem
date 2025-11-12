package com.ys.hr.controller;

import com.opencsv.CSVWriter;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrAttendance;
import com.ys.hr.domain.HrPayroll;
import com.ys.hr.domain.vo.payRollVo;
import com.ys.hr.service.IHrPayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * EMPLOYEE SALARY Controller
 *
 * @author ys
 * @date 2025-05-30
 */
@RestController
@RequestMapping("/payroll")
public class HrPayrollController extends BaseController {
    @Autowired
    private IHrPayrollService hrPayrollService;

    /**
     * Query EMPLOYEE SALARY list
     */
    @RequiresPermissions("hr:payroll:list")
    @GetMapping("/list")
    public TableDataInfo list(HrPayroll hrPayroll) {
        if(Objects.equals(hrPayroll.getFlag(), "1")){
            hrPayroll.setPayrollStatus('1');
        }
        hrPayroll.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrPayroll> list = hrPayrollService.selectHrPayrollList(hrPayroll);
        return getDataTable(list);
    }


    @GetMapping("/listByUserId")
    public TableDataInfo listByUserId(HrPayroll hrPayroll) {
        hrPayroll.setUserId(SecurityUtils.getUserId());
        startPage();
        List<HrPayroll> list = hrPayrollService.selectHrPayrollList(hrPayroll);
        return getDataTable(list);
    }

    @GetMapping("/lastPayrollByUserId")
    public AjaxResult lastPayrollByUserId(HrPayroll hrPayroll) {
        hrPayroll.setUserId(SecurityUtils.getUserId());
        return AjaxResult.success(hrPayrollService.lastPayrollByUserId(hrPayroll));
    }

    /**
     * Export EMPLOYEE SALARY list
     */
    // @RequiresPermissions("hr:payroll:export")
    // @Log(title = " EMPLOYEE SALARY ", businessType = BusinessType.EXPORT)
    // @PostMapping("/export")
    // public void export(HttpServletResponse response, HrPayroll hrPayroll)
    // {
    // List<payRollVo> list = hrPayrollService.selectHrPayrollListVo(hrPayroll);
    // ExcelUtil<payRollVo> util = new ExcelUtil<payRollVo>(payRollVo.class);
    // util.exportExcel(response, list, " EMPLOYEE SALARY Data");
    // }
    @RequiresPermissions("hr:payroll:export")
    @Log(title = "EMPLOYEE SALARY", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrPayroll hrPayroll, String payrollIds) throws IOException {

        // System.out.println("payrollIds:"+payrollIds);
        List<payRollVo> listByIds = hrPayrollService.selectHrPayrollListVoByIds(payrollIds);
        // Determine the query method based on whether ID is selected
        // List<payRollVo> list = hrPayrollService.selectHrPayrollListVo(hrPayroll);

        // Set CSV response headers
        response.setContentType("text/csv; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("EMPLOYEE_SALARY", "UTF-8").replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".csv");

        // Create CSV writer
        try (CSVWriter writer = new CSVWriter(response.getWriter())) {
            // Write CSV header (generated based on @Excel annotations of payRollVo)
            writer.writeNext(new String[] { "bsb", "account_num", "name", "amount", "txn_reference" });

            // Write data rows
            for (payRollVo vo : listByIds) {
                writer.writeNext(new String[] {
                        vo.getBsb() != null ? vo.getBsb() : "",
                        vo.getBankNumber() != null ? vo.getBankNumber() : "",
                        vo.getFullName() != null ? vo.getFullName() : "",
                        vo.getAfterTaxSalary() != null ? vo.getAfterTaxSalary().toString() : "",
                        vo.getRemark() != null ? vo.getRemark() : ""
                });
            }
        }
    }

    /**
     * Get EMPLOYEE SALARY details
     */
    @GetMapping(value = "/{payrollId}")
    public AjaxResult getInfo(@PathVariable("payrollId") String payrollId) {
        return success(hrPayrollService.selectHrPayrollByPayrollId(payrollId));
    }

    @GetMapping("/getPayRollByUserId")
    public AjaxResult getPayRollByUserId(HrPayroll hrPayroll) {
        return AjaxResult.success(hrPayrollService.getPayRollByUserId(hrPayroll));
    }

    /**
     * Add EMPLOYEE SALARY
     */
    @RequiresPermissions("hr:payroll:add")
    @Log(title = " EMPLOYEE  SALARY ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrPayroll hrPayroll) {
        return toAjax(hrPayrollService.insertHrPayroll(hrPayroll));
    }

    @RequiresPermissions("hr:payroll:add")
    @PostMapping("/addList")
    public AjaxResult addList(@Validated @RequestBody List<HrPayroll> hrPayrolls) {
        return toAjax(hrPayrollService.insertHrPayrolls(hrPayrolls));
    }

    /**
     * Update EMPLOYEE SALARY
     */
    @RequiresPermissions("hr:payroll:edit")
    @Log(title = " EMPLOYEE  SALARY ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrPayroll hrPayroll) {
        return toAjax(hrPayrollService.updateHrPayroll(hrPayroll));
    }

    /**
     * Delete EMPLOYEE SALARY
     */
    @RequiresPermissions("hr:payroll:remove")
    @Log(title = " EMPLOYEE  SALARY ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{payrollIds}")
    public AjaxResult remove(@PathVariable String[] payrollIds) {
        return toAjax(hrPayrollService.removeByIds(Arrays.asList(payrollIds)));
    }

    @PutMapping("/updata/{payrollIds}")
    public AjaxResult updataByIds(@PathVariable String[] payrollIds) {
        return toAjax(hrPayrollService.updataByIds(Arrays.asList(payrollIds)));
    }

    @RequiresPermissions("hr:payroll:list")
    @GetMapping("/getPayrollsCoutByHr")
    public AjaxResult getPayrollsCoutByHr(HrAttendance hrAttendance) {
        hrAttendance.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        Map<String, Object> map = hrPayrollService.getPayrollsCoutByHr(hrAttendance);
        return AjaxResult.success(map);
    }

    @RequiresPermissions("hr:payroll:list")
    @GetMapping("/getPayrollsCoutByEmp")
    public AjaxResult getPayrollsCoutByEmp(HrAttendance hrAttendance) {
        hrAttendance.setUserId(SecurityUtils.getUserId());
        Map<String, Object> map = hrPayrollService.getPayrollsCoutByEmp(hrAttendance);
        return AjaxResult.success(map);
    }
}
