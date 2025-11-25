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
//    @RequiresPermissions("hr:targets:list")
    @GetMapping("/list")
    public TableDataInfo list(HrTargets hrTargets)
    {
        startPage();
        hrTargets.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        if (!isAdmin()) {
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
        checkParticipants(id);
        HrTargets data = hrTargetsService.selectHrTargetsById(id);
        String enterpriseId = data.getEnterpriseId();
        if (!enterpriseId.equals(SecurityUtils.getUserEnterpriseId())) {
            return AjaxResult.error("No authority to access this data");
        }
        QueryWrapper<HrTargetTasks> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_id",id);
        long count = hrTargetTasksService.count(queryWrapper);
        QueryWrapper<HrTargetTasks> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("target_id",id);
        queryWrapper1.in("status","Complete","Confirm");
        long count1 = hrTargetTasksService.count(queryWrapper1);
        if (count!=0){
            data.setProgress((int) (((double) count1 / (double) count) * 100));
        }else {
            data.setProgress(0);
        }
        Integer progress = data.getProgress();
        String status = data.getStatus();
        if (progress>0&&progress!=100&&"Not Started".equals(status)){
            data.setStatus("In Progress");
            hrTargetsService.updateHrTargets(data);
        }
        if (progress==100&&!"Complete".equals(status)){
            data.setStatus("Complete");
            completeTargets(id);
        }
        return success(data);
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
        checkParticipants(hrTargets.getId());
        hrTargets.setStatus(null);
        HrTargets hrTargets1 = hrTargetsService.updateHrTargets(hrTargets);
        return toAjax(hrTargets1!=null);
    }

    @RequiresPermissions("hr:targets:list")
    @PutMapping("complete/{id}")
    public AjaxResult complete(@PathVariable Long id) {
        completeTargets(id);
        return AjaxResult.success();
    }

    public void completeTargets(Long id) {
        checkParticipants(id);
        HrTargets hrTargets = new HrTargets();
        hrTargets.setId(id);
        hrTargets.setStatus("Complete");
        hrTargets.setCompleteTime(DateUtils.getNowDate());
        HrTargets hrTargets1 = hrTargetsService.selectHrTargetsById(id);
        HrTargetEmployee hrTargetEmployee = new HrTargetEmployee();
        hrTargetEmployee.setTargetId(id);
        List<HrTargetEmployee> hrTargetEmployees = hrTargetEmployeeService.selectHrTargetEmployeeList(hrTargetEmployee);
        if(ObjectUtils.isNotEmpty(hrTargetEmployees)){
            //使用stream留将 员工id变成数组
            for (HrTargetEmployee employeeId : hrTargetEmployees) {
                HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesById(employeeId.getEmployeeId());
                HrEmployees leader = hrEmployeesMapper.selectHrEmployeesByUserId(hrTargets1.getHead());
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient(hrEmployees.getUserId());
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(17);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                map2.put("id", id);
                if(ObjectUtils.isNotEmpty(leader)){
                    map1.put("HrName", leader.getFullName());
                }else{
                    map1.put("HrName", "The leader is not indicated");
                }
                map1.put("title", hrTargets1.getName());
                Date startTime = DateUtils.getNowDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(startTime);
                map1.put("data", formattedDate);
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        Long resignEmployeeId = hrTargets1.getResignEmployeeId();
        if (resignEmployeeId!=null){
            QueryWrapper<HrTargets> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("resign_employee_id",resignEmployeeId);
            queryWrapper.orderByDesc("create_time");
            queryWrapper.select("status");
            queryWrapper.last("limit 2");
            List<HrTargets> list = hrTargetsService.list(queryWrapper);
            int count = 0;
            for (HrTargets targets : list) {
                if (targets.getStatus().equals("Complete")) {
                    count++;
                }
            }
            if (count==1){
                hrEmployeesService.resignComplete(resignEmployeeId,hrTargets1.getHead());
            }
        }
        hrTargetsService.updateHrTargets(hrTargets);
    }

    @RequiresPermissions("hr:targets:list")
    @PutMapping("confirm/{id}")
    public AjaxResult confirm(@PathVariable Long id) {
        checkHead(id);
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
        checkHead(id);
        return toAjax(hrTargetsService.deleteHrTargetsById(id));
    }

    @PostMapping("/startTask")
    public AjaxResult startTask(@RequestBody HrTargetEmployee hrTargetEmployee) {
        HrTargetEmployee targetEmployee = hrTargetEmployeeService.selectHrTargetEmployeeById(hrTargetEmployee.getId());
        hrTargetEmployee.setIsStart("1");
        //Determine whether the current task is started
        if ("1".equals(targetEmployee.getIsStart())) {
            HrTargets targets = hrTargetsService.selectHrTargetsById(hrTargetEmployee.getTargetId());
            return AjaxResult.warn(targets.getName() + " executing");
        }
        HrTargetEmployee hrTargetEmployee1 = new HrTargetEmployee();
        hrTargetEmployee1.setEmployeeId(hrTargetEmployee.getEmployeeId());
        hrTargetEmployee1.setIsStart("1");
        List<HrTargetEmployee> hrTargetEmployees = hrTargetEmployeeService.selectHrTargetEmployeeList(hrTargetEmployee1);
        if (!hrTargetEmployees.isEmpty()) {
            return AjaxResult.warn("There is an start task, please stop it first");
        }
//        hrTargetEmployee.setExecutionTime(DateUtils.getNowDate());
        return toAjax(hrTargetEmployeeService.updateHrTargetEmployee(hrTargetEmployee));
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

    public void checkHead(Long id){
        if (isAdmin()){
            return;
        }
        if (isNotHr() &&!isHead(id)){
            throw new ServiceException("Non target creators have no authority to operate");
        }
    }

    public void checkParticipants(Long id){
        if (isAdmin()){
            return;
        }
        boolean b = isParticipants(id);
        boolean head = isHead(id);
        if (isNotHr() && !head && !b){
            throw new ServiceException("Non target participants have no authority to operate");
        }
    }

    public boolean isAdmin(){
        return "01".equals(SecurityUtils.getUserType()) || "00".equals(SecurityUtils.getUserType()) || SecurityUtils.getLoginUser().getRoles().contains("hr");
    }

    public boolean isNotHr(){
        return !SecurityUtils.getLoginUser().getRoles().contains("hr");
    }

    public boolean isHead(Long id){
        HrTargets hrTargets = hrTargetsService.selectHrTargetsById(id);
        if (ObjectUtils.isNotEmpty(hrTargets)){
            Long head = hrTargets.getHead();
            if (ObjectUtils.isNotEmpty(head)) {
                return SecurityUtils.getUserId().equals(head);
            }
        }
        return false;
    }

    public boolean isParticipants(Long id){
        QueryWrapper<HrTargetEmployee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_id",id);
        Long userId = SecurityUtils.getUserId();
        HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesByUserId(userId);
        queryWrapper.eq("employee_id", hrEmployees.getEmployeeId());
        long count = hrTargetEmployeeService.count(queryWrapper);
        return count != 0;
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
