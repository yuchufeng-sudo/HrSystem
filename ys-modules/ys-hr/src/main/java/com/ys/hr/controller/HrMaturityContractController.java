package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.security.utils.SecurityUtils;
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
import com.ys.hr.domain.HrMaturityContract;
import com.ys.hr.service.IHrMaturityContractService;
import org.springframework.validation.annotation.Validated;

/**
 *  THE CONTRACT EXPIRES  Controller
 *
 * @author ys
 * @date 2025-06-20
 */
@RestController
@RequestMapping("/maturityContract")
public class HrMaturityContractController extends BaseController
{
    @Autowired
    private IHrMaturityContractService hrMaturityContractService;

    /**
     * Query  THE CONTRACT EXPIRES  list
     */
    @RequiresPermissions("hr:maturityContract:list")
    @GetMapping("/list")
    public TableDataInfo list(HrMaturityContract hrMaturityContract)
    {
        hrMaturityContract.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrMaturityContract> list = hrMaturityContractService.selectHrMaturityContractList(hrMaturityContract);
        return getDataTable(list);
    }

    /**
     * Export  THE CONTRACT EXPIRES  list
     */
    @RequiresPermissions("hr:maturityContract:export")
    @Log(title = " THE CONTRACT EXPIRES ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrMaturityContract hrMaturityContract)
    {
        List<HrMaturityContract> list = hrMaturityContractService.selectHrMaturityContractList(hrMaturityContract);
        ExcelUtil<HrMaturityContract> util = new ExcelUtil<HrMaturityContract>(HrMaturityContract.class);
        util.exportExcel(response, list, " THE CONTRACT EXPIRES  Data");
    }

    /**
     * Get  THE CONTRACT EXPIRES  details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrMaturityContractService.selectHrMaturityContractById(id));
    }

    /**
     * Add  THE CONTRACT EXPIRES 
     */
    @RequiresPermissions("hr:maturityContract:add")
    @Log(title = " THE CONTRACT EXPIRES ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrMaturityContract hrMaturityContract) {
        return toAjax(hrMaturityContractService.insertHrMaturityContract(hrMaturityContract));
    }

    /**
     * Update  THE CONTRACT EXPIRES 
     */
    @RequiresPermissions("hr:maturityContract:edit")
    @Log(title = " THE CONTRACT EXPIRES ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrMaturityContract hrMaturityContract) {
        return toAjax(hrMaturityContractService.updateHrMaturityContract(hrMaturityContract));
    }

    /**
     * Delete  THE CONTRACT EXPIRES 
     */
    @RequiresPermissions("hr:maturityContract:remove")
    @Log(title = " THE CONTRACT EXPIRES ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrMaturityContractService.removeByIds(Arrays.asList(ids)));
    }
}
