package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrBackgroundCheckOrder;
import com.ys.hr.service.IHrBackgroundCheckOrderService;
import org.springframework.validation.annotation.Validated;

/**
 * Background Check Order Controller
 *
 * @author ys
 * @date 2025-06-25
 */
@RestController
@RequestMapping("/backgroundCheckOrder")
public class HrBackgroundCheckOrderController extends BaseController
{
    @Autowired
    private IHrBackgroundCheckOrderService hrBackgroundCheckOrderService;

    /**
     * Query Background Check Order list
     */
    @RequiresPermissions("hr:backgroundCheckOrder:list")
    @GetMapping("/list")
    public TableDataInfo list(HrBackgroundCheckOrder hrBackgroundCheckOrder)
    {
        startPage();
        List<HrBackgroundCheckOrder> list = hrBackgroundCheckOrderService.selectHrBackgroundCheckOrderList(hrBackgroundCheckOrder);
        return getDataTable(list);
    }

    /**
     * Export Background Check Order list
     */
    @RequiresPermissions("hr:backgroundCheckOrder:export")
    @Log(title = "Background Check Order", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrBackgroundCheckOrder hrBackgroundCheckOrder)
    {
        List<HrBackgroundCheckOrder> list = hrBackgroundCheckOrderService.selectHrBackgroundCheckOrderList(hrBackgroundCheckOrder);
        ExcelUtil<HrBackgroundCheckOrder> util = new ExcelUtil<HrBackgroundCheckOrder>(HrBackgroundCheckOrder.class);
        util.exportExcel(response, list, "Background Check Order Data");
    }

    /**
     * Get Background Check Order details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrBackgroundCheckOrderService.selectHrBackgroundCheckOrderById(id));
    }

    /**
     * Add Background Check Order
     */
    @RequiresPermissions("hr:backgroundCheckOrder:add")
    @Log(title = "Background Check Order", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrBackgroundCheckOrder hrBackgroundCheckOrder) {
        return toAjax(hrBackgroundCheckOrderService.insertHrBackgroundCheckOrder(hrBackgroundCheckOrder));
    }

    /**
     * Update Background Check Order
     */
    @RequiresPermissions("hr:backgroundCheckOrder:edit")
    @Log(title = "Background Check Order", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrBackgroundCheckOrder hrBackgroundCheckOrder) {
        return toAjax(hrBackgroundCheckOrderService.updateHrBackgroundCheckOrder(hrBackgroundCheckOrder));
    }

    /**
     * Delete Background Check Order
     */
    @RequiresPermissions("hr:backgroundCheckOrder:remove")
    @Log(title = "Background Check Order", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrBackgroundCheckOrderService.removeByIds(Arrays.asList(ids)));
    }

    @PostMapping("/refreshStatus")
    public AjaxResult refreshStatus(@RequestBody HrBackgroundCheckOrder hrBackgroundCheckOrder) {
        return toAjax(hrBackgroundCheckOrderService.refreshStatus(hrBackgroundCheckOrder));
    }
}
