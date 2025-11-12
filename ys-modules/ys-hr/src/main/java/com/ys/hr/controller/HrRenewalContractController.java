package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrRenewalContract;
import com.ys.hr.service.IHrRenewalContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 *  RENEWAL CONTRACT  Controller
 *
 * @author ys
 * @date 2025-06-20
 */
@RestController
@RequestMapping("/renewalContract")
public class HrRenewalContractController extends BaseController
{
    @Autowired
    private IHrRenewalContractService hrRenewalContractService;

    /**
     * Query  RENEWAL CONTRACT  list
     */
    @RequiresPermissions("hr:renewalContract:list")
    @GetMapping("/list")
    public TableDataInfo list(HrRenewalContract hrRenewalContract)
    {
        hrRenewalContract.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrRenewalContract> list = hrRenewalContractService.selectHrRenewalContractList(hrRenewalContract);
        return getDataTable(list);
    }

    /**
     * Export  RENEWAL CONTRACT  list
     */
    @RequiresPermissions("hr:renewalContract:export")
    @Log(title = " RENEWAL CONTRACT ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrRenewalContract hrRenewalContract)
    {
        List<HrRenewalContract> list = hrRenewalContractService.selectHrRenewalContractList(hrRenewalContract);
        ExcelUtil<HrRenewalContract> util = new ExcelUtil<HrRenewalContract>(HrRenewalContract.class);
        util.exportExcel(response, list, " RENEWAL CONTRACT  Data");
    }

    /**
     * Get  RENEWAL CONTRACT  details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(hrRenewalContractService.selectHrRenewalContractById(id));
    }

    /**
     * Add  RENEWAL CONTRACT
     */
    @RequiresPermissions("hr:renewalContract:add")
    @Log(title = " RENEWAL CONTRACT ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrRenewalContract hrRenewalContract) {
        return toAjax(hrRenewalContractService.insertHrRenewalContract(hrRenewalContract));
    }

    /**
     * Update  RENEWAL CONTRACT
     */
    @RequiresPermissions("hr:renewalContract:edit")
    @Log(title = " RENEWAL CONTRACT ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrRenewalContract hrRenewalContract) {
        return toAjax(hrRenewalContractService.updateHrRenewalContract(hrRenewalContract));
    }

    /**
     * Delete  RENEWAL CONTRACT
     */
    @RequiresPermissions("hr:renewalContract:remove")
    @Log(title = " RENEWAL CONTRACT ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(hrRenewalContractService.removeByIds(Arrays.asList(ids)));
    }
}
