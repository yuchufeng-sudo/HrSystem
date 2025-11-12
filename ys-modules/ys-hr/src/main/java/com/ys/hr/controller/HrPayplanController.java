package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.hr.domain.HrPayplan;
import com.ys.hr.service.IHrPayplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Pay Plan Controller
 *
 * @author ys
 * @date 2025-06-17
 */
@RestController
@RequestMapping("/payplan")
public class HrPayplanController extends BaseController
{
    @Autowired
    private IHrPayplanService hrPayplanService;

    /**
     * Query Pay Plan list
     */

    @GetMapping("/list")
    public TableDataInfo list(HrPayplan hrPayplan)
    {
        startPage();
        List<HrPayplan> list = hrPayplanService.selectHrPayplanList(hrPayplan);
        return getDataTable(list);
    }

    /**
     * Export Pay Plan list
     */

    @Log(title = "Pay Plan", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrPayplan hrPayplan)
    {
        List<HrPayplan> list = hrPayplanService.selectHrPayplanList(hrPayplan);
        ExcelUtil<HrPayplan> util = new ExcelUtil<HrPayplan>(HrPayplan.class);
        util.exportExcel(response, list, "Pay Plan Data");
    }

    /**
     * Get Pay Plan details
     */
    @GetMapping(value = "/{payId}")
    public AjaxResult getInfo(@PathVariable("payId") String payId) {
        return success(hrPayplanService.selectHrPayplanByPayId(payId));
    }

    /**
     * Add Pay Plan
     */

    @Log(title = "Pay Plan", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrPayplan hrPayplan) {
        return toAjax(hrPayplanService.insertHrPayplan(hrPayplan));
    }

    /**
     * Update Pay Plan
     */

    @Log(title = "Pay Plan", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrPayplan hrPayplan) {
        return toAjax(hrPayplanService.updateHrPayplan(hrPayplan));
    }

    /**
     * Delete Pay Plan
     */

    @Log(title = "Pay Plan", businessType = BusinessType.DELETE)
    @DeleteMapping("/{payIds}")
    public AjaxResult remove(@PathVariable String[] payIds) {
        return toAjax(hrPayplanService.removeByIds(Arrays.asList(payIds)));
    }
}
