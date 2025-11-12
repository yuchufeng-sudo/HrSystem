package com.ys.hr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
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
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IHrTargetEmployeeService;
import com.ys.hr.service.IHrTargetTasksService;
import com.ys.hr.service.IHrTargetsService;
import com.ys.system.api.model.LoginUser;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Manages task assignments and progress tracking for employee targets Controller
 *
 * @author ys
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/targetTasks")
public class HrTargetTasksController extends BaseController
{
    @Autowired
    private IHrTargetTasksService hrTargetTasksService;
    @Autowired
    private IHrTargetsService hrTargetsService;
    @Resource
    private IHrTargetEmployeeService hrTargetEmployeeService;
    @Resource
    private IHrEmployeesService hrEmployeesService;

    /**
     * Query Manages task assignments and progress tracking for employee targets list
     */
    @RequiresPermissions("hr:targets:list")
    @GetMapping("/list")
    public TableDataInfo list(HrTargetTasks hrTargetTasks)
    {
        startPage();
        List<HrTargetTasks> list = hrTargetTasksService.selectHrTargetTasksList(hrTargetTasks);
        return getDataTable(list);
    }

    /**
     * Get Manages task assignments and progress tracking for employee targets details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        HrTargetTasks data = hrTargetTasksService.selectHrTargetTasksById(id);
        checkParticipants(data.getId());
        return success(data);
    }

    /**
     * Add Manages task assignments and progress tracking for employee targets
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Manages task assignments and progress tracking for employee targets", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrTargetTasks hrTargetTasks) {
        return toAjax(hrTargetTasksService.insertHrTargetTasks(hrTargetTasks));
    }

    /**
     * Update Manages task assignments and progress tracking for employee targets
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Manages task assignments and progress tracking for employee targets", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrTargetTasks hrTargetTasks) {
        checkParticipants(hrTargetTasks.getId());
        return toAjax(hrTargetTasksService.updateHrTargetTasks(hrTargetTasks));
    }

    @PutMapping("complete")
    public AjaxResult complete(@RequestBody HrTargetTasks hrTargetTasks) {
        checkParticipants(hrTargetTasks.getId());
        hrTargetTasks.setStatus("Complete");
        hrTargetTasks.setCompleteTime(DateUtils.getNowDate());
        return toAjax(hrTargetTasksService.updateHrTargetTasks(hrTargetTasks));
    }

    @PutMapping("confirm/{id}")
    public AjaxResult confirm(@PathVariable Long id) {
        checkHead(id);
        HrTargetTasks hrTargetTasks = new HrTargetTasks();
        hrTargetTasks.setId(id);
        hrTargetTasks.setStatus("Confirm");
        hrTargetTasks.setConfigTime(DateUtils.getNowDate());
        return toAjax(hrTargetTasksService.updateHrTargetTasks(hrTargetTasks));
    }

    @PutMapping("refuse/{id}")
    public AjaxResult refuse(@PathVariable Long id) {
        checkHead(id);
        HrTargetTasks hrTargetTasks = new HrTargetTasks();
        hrTargetTasks.setId(id);
        hrTargetTasks.setStatus("Refuse");
        return toAjax(hrTargetTasksService.updateHrTargetTasks(hrTargetTasks));
    }

    /**
     * Delete Manages task assignments and progress tracking for employee targets
     */
    @RequiresPermissions("hr:targets:list")
    @Log(title = "Manages task assignments and progress tracking for employee targets", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrTargetTasksService.removeByIds(Arrays.asList(ids)));
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
        boolean isParticipants = isParticipants(id);
        boolean isHead = isHead(id);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        System.out.println(loginUser.getRoles().contains("hr"));
        if (isNotHr() && !isHead && !isParticipants){
            throw new ServiceException("Non target participants have no authority to operate");
        }
    }

    public boolean isAdmin(){
        return "01".equals(SecurityUtils.getUserType()) || "00".equals(SecurityUtils.getUserType()) || SecurityUtils.getLoginUser().getRoles().contains("hr") || SecurityUtils.getLoginUser().getRoles().contains("admin");
    }

    public boolean isNotHr(){
        return !SecurityUtils.getLoginUser().getRoles().contains("hr");
    }

    public boolean isHead(Long id){
        HrTargetTasks data = hrTargetTasksService.selectHrTargetTasksById(id);
        HrTargets hrTargets = hrTargetsService.selectHrTargetsById(data.getTargetId());
        if (ObjectUtils.isNotEmpty(hrTargets)){
            Long head = hrTargets.getHead();
            if (ObjectUtils.isNotEmpty(head)) {
                return SecurityUtils.getUserId().equals(head);
            }
        }
        return false;
    }

    public boolean isParticipants(Long id){
        HrTargetTasks data = hrTargetTasksService.selectHrTargetTasksById(id);
        Long targetId = data.getTargetId();
        QueryWrapper<HrTargetEmployee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_id",targetId);
        Long userId = SecurityUtils.getUserId();
        HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesByUserId(userId);
        queryWrapper.eq("employee_id", hrEmployees.getEmployeeId());
        queryWrapper.eq("type",1);
        long count = hrTargetEmployeeService.count(queryWrapper);
        return count != 0;
    }
}
