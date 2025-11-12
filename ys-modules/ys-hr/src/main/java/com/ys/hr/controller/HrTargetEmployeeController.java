package com.ys.hr.controller;

import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrTargetEmployee;
import com.ys.hr.service.IHrTargetEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Target allocation of employees Controller
 *
 * @author ys
 * @date 2025-06-23
 */
@RestController
@RequestMapping("/targetEmployee")
public class HrTargetEmployeeController extends BaseController
{
    @Autowired
    private IHrTargetEmployeeService hrTargetEmployeeService;

    /**
     * Query Target allocation of employees list
     */
    @RequiresPermissions("hr:targets:list")
    @GetMapping("/list")
    public TableDataInfo list(HrTargetEmployee hrTargetEmployee)
    {
        startPage();
        if (hrTargetEmployee.getIsNotFeedback()!=null) {
            hrTargetEmployee.setUserId(SecurityUtils.getUserId());
        }
        List<HrTargetEmployee> list = hrTargetEmployeeService.selectHrTargetEmployeeList(hrTargetEmployee);
        return getDataTable(list);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(hrTargetEmployeeService.selectHrTargetEmployeeById(id));
    }
}
