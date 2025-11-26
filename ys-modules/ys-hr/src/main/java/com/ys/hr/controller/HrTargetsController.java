package com.ys.hr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrTargetEmployee;
import com.ys.hr.domain.HrTargetTasks;
import com.ys.hr.domain.HrTargets;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IHrTargetEmployeeService;
import com.ys.hr.service.IHrTargetTasksService;
import com.ys.hr.service.IHrTargetsService;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Main table storing all target information Controller
 *
 * @author ys
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/targets")
public class HrTargetsController extends BaseController
{
    @Autowired
    private IHrTargetsService hrTargetsService;

    @Resource
    private IHrTargetEmployeeService hrTargetEmployeeService;

    @Resource
    private IHrTargetTasksService hrTargetTasksService;

    @Resource
    private IHrEmployeesService hrEmployeesService;

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private RemoteMessageService remoteMessageService;
    /**
     * Query Main table storing all target information list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrTargets hrTargets)
    {
        startPage();
        hrTargets.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        if (!hrTargetsService.isAdmin()) {
            hrTargets.setUserId(SecurityUtils.getUserId());
        }
        List<HrTargets> list = hrTargetsService.selectHrTargetsList(hrTargets);
        return getDataTable(list);
    }

    /**
     * Export Main table storing all target information list
     */
    @RequiresPermissions("hr:targets:export")
    @Log(title = "Main table storing all target information", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrTargets hrTargets)
    {
        hrTargets.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrTargets> list = hrTargetsService.selectHrTargetsList(hrTargets);
        ExcelUtil<HrTargets> util = new ExcelUtil<HrTargets>(HrTargets.class);
        util.exportExcel(response, list, "Main table storing all target information Data");
    }

    /**
     * Get Main table storing all target information details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrTargetsService.selectHrTargetsInfo(id));
    }

    /**
     * Add Main table storing all target information
     */
    @RequiresPermissions("hr:targets:add")
    @Log(title = "Main table storing all target information", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrTargets hrTargets) {
        hrTargets.setStatus("Not Started");
        hrTargets.setProgress(0);
        hrTargets.setHead(SecurityUtils.getUserId());
        hrTargets.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        HrTargets hrTargets1 = hrTargetsService.insertHrTargets(hrTargets);
        return toAjax(hrTargets1!=null);
    }

    /**
     * Update Main table storing all target information
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Main table storing all target information", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrTargets hrTargets) {
        hrTargetsService.checkParticipants(hrTargets.getId());
        hrTargets.setStatus(null);
        HrTargets hrTargets1 = hrTargetsService.updateHrTargets(hrTargets);
        return toAjax(hrTargets1!=null);
    }

    @RequiresPermissions("hr:targets:list")
    @PutMapping("complete/{id}")
    public AjaxResult complete(@PathVariable Long id) {
        hrTargetsService.completeTargets(id);
        return AjaxResult.success();
    }

    @RequiresPermissions("hr:targets:list")
    @PutMapping("confirm/{id}")
    public AjaxResult confirm(@PathVariable Long id) {
        hrTargetsService.checkHead(id);
        HrTargets hrTargets = new HrTargets();
        hrTargets.setId(id);
        hrTargets.setStatus("Confirm");
        hrTargets.setConfigTime(DateUtils.getNowDate());
        return toAjax(hrTargetsService.updateHrTargets(hrTargets)!=null);
    }

    /**
     * Delete Main table storing all target information
     */
    @RequiresPermissions("hr:targets:remove")
    @Log(title = "Main table storing all target information", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        hrTargetsService.checkHead(id);
        return toAjax(hrTargetsService.deleteHrTargetsById(id));
    }

    @PostMapping("/startTask")
    public AjaxResult startTask(@RequestBody HrTargetEmployee hrTargetEmployee) {

        return toAjax(hrTargetsService.startTask(hrTargetEmployee));
    }

    @GetMapping("/getExecutionTarget")
    public AjaxResult getExecutionTarget(){
        HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());
        if(hrEmployees!=null){
            HrTargetEmployee targets = hrTargetEmployeeService.selectExecutionTarget(hrEmployees.getEmployeeId());
            return AjaxResult.success(targets);
        }else {
            return AjaxResult.success();
        }
    }

    @PostMapping("/targetPause")
    public AjaxResult targetPause(@RequestBody HrTargetEmployee hrTargetEmployee) {
        hrTargetEmployee.setIsStart("2");
        return toAjax(hrTargetEmployeeService.updateHrTargetEmployee(hrTargetEmployee));
    }

    @PostMapping("/stopPause")
    public AjaxResult stopPause(@RequestBody HrTargetEmployee hrTargetEmployee) {
        return toAjax(hrTargetEmployeeService.stopPause(hrTargetEmployee));
    }

    @PostMapping("/finish")
    public AjaxResult finish(@RequestBody HrTargetEmployee hrTargetEmployee) {
        HrTargetEmployee targetEmployee = new HrTargetEmployee();
        targetEmployee.setId(hrTargetEmployee.getId());
        targetEmployee.setIsStart("3");
        return toAjax(hrTargetEmployeeService.finish(targetEmployee));
    }

    /**
     * Query Main table storing all target information list
     */
    @GetMapping("/targetList")
    public TableDataInfo targetList(HrTargets hrTargets)
    {
        if ("00".equals(SecurityUtils.getLoginUser().getSysUser().getUserType())) {
            return getDataTable(new ArrayList<>());
        }
        startPage();
        List<HrTargetEmployee> list = hrTargetsService.selectHrTargets(hrTargets);
        return getDataTable(list);
    }
}
