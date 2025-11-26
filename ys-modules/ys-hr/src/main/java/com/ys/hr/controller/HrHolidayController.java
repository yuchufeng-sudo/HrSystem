package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrHoliday;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.service.IHrHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *  Holiday Controller
 *
 * @author ys
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/holidayLeave")
public class HrHolidayController extends BaseController
{
    @Autowired
    private IHrHolidayService hrHolidayService;

    @Autowired
    private HrEmployeesMapper hrEmployeesMapper;

    /**
     * Query Holiday list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrHoliday hrHoliday)
    {
        hrHoliday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrHoliday> list = hrHolidayService.selectHrHolidayList(hrHoliday);
        list.stream()
                .forEach(person -> {
                    if ("1".equals(person.getCarrayForward())) {
                        person.setCarray(true);
                    } else if ("2".equals(person.getCarrayForward())) {
                        person.setCarray(false);
                    }
                    if("1".equals(person.getPaidLeave())){
                        person.setPaid(true);
                    } else if("2".equals(person.getPaidLeave())){
                        person.setPaid(false);
                    }                });
        return getDataTable(list);
    }


    @GetMapping("/holidayList")
    public AjaxResult holidayList(HrHoliday hrHoliday)
    {
        hrHoliday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrHoliday> list = hrHolidayService.selectHrHolidayList(hrHoliday);
        return AjaxResult.success(list);
    }

    /**
     * Export Holiday list
     */
    @RequiresPermissions("hr:holiday:export")
    @Log(title = "Holiday", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrHoliday hrHoliday)
    {
        List<HrHoliday> list = hrHolidayService.selectHrHolidayList(hrHoliday);
        ExcelUtil<HrHoliday> util = new ExcelUtil<HrHoliday>(HrHoliday.class);
        util.exportExcel(response, list, " Holiday Data");
    }

    /**
     * Get Holiday details
     */
    @GetMapping(value = "/{holidayId}")
    public AjaxResult getInfo(@PathVariable("holidayId") Long holidayId) {
        return success(hrHolidayService.selectHrHolidayByHolidayId(holidayId));
    }

    /**
     * Add Holiday
     */
    @Log(title = "Holiday", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrHoliday hrHoliday) {
        hrHoliday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrHolidayService.insertHrHoliday(hrHoliday));
    }

    /**
     * Update Holiday
     */
    @Log(title = "Holiday", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrHoliday hrHoliday) {
        return toAjax(hrHolidayService.updateHrHoliday(hrHoliday));
    }

    /**
     * Delete Holiday
     */
    @Log(title = "Holiday", businessType = BusinessType.DELETE)
    @DeleteMapping("/{holidayIds}")
    public AjaxResult remove(@PathVariable Long[] holidayIds) {
        List<HrEmployees> hrEmployees = hrEmployeesMapper.selectHrEmployeesListByEid(SecurityUtils.getUserEnterpriseId());
        HrHoliday hrHoliday = hrHolidayService.selectHrHolidayByHolidayId(holidayIds[0]);
        String holidayType = hrHoliday.getHolidayType();
        boolean anyHas = hrEmployees.stream()
                .map(HrEmployees::getLeaveEntitlements)
                .filter(Objects::nonNull)
                .flatMap(ent -> Arrays.stream(ent.split(","))
                        .map(String::trim))
                .anyMatch(s -> s.equals(holidayType));
        if (!anyHas) {
            return AjaxResult.success(hrHolidayService.deleteHrHolidayByHolidayId(holidayIds[0]));
        } else {
            return AjaxResult.error("An employee has already set up this leave benefit and it cannot be deleted.");
        }
    }
}
