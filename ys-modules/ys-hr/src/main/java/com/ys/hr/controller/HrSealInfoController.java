package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrSealInfo;
import com.ys.hr.service.IHrSealInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *  seal Information  Controller
 *
 * @author ys
 * @date 2025-06-05
 */
@RestController
@RequestMapping("/sealInfo")
public class HrSealInfoController extends BaseController
{
    @Autowired
    private IHrSealInfoService hrSealInfoService;

    /**
     * Query seal Information list
     */
    @RequiresPermissions("hr:sealInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(HrSealInfo hrSealInfo)
    {
        hrSealInfo.setAccount(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrSealInfo> list = hrSealInfoService.selectHrSealInfoList(hrSealInfo);
        return getDataTable(list);
    }

    /**
     * Export seal Information list
     */
    @RequiresPermissions("hr:sealInfo:export")
    @Log(title = "seal Information", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrSealInfo hrSealInfo)
    {
        List<HrSealInfo> list = hrSealInfoService.selectHrSealInfoList(hrSealInfo);
        ExcelUtil<HrSealInfo> util = new ExcelUtil<HrSealInfo>(HrSealInfo.class);
        util.exportExcel(response, list, " seal Information  Data");
    }

    /**
     * Get seal Information  details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrSealInfoService.selectHrSealInfoById(id));
    }

    /**
     * Add seal Information
     */
    @RequiresPermissions("hr:sealInfo:add")
    @Log(title = "seal Information", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrSealInfo hrSealInfo) {
        return toAjax(hrSealInfoService.insertHrSealInfo(hrSealInfo));
    }

    /**
     * Update seal Information
     */
    @RequiresPermissions("hr:sealInfo:edit")
    @Log(title = "seal Information", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrSealInfo hrSealInfo) {
        return toAjax(hrSealInfoService.updateHrSealInfo(hrSealInfo));
    }

    /**
     * Delete seal Information
     */
    @RequiresPermissions("hr:sealInfo:remove")
    @Log(title = "seal Information", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
//        HrSealInfo hrSealInfo = hrSealInfoService.selectHrSealInfoById(id);
//        ContractUtils contractUtils = new ContractUtils();
//        if (ObjectUtils.isNotEmpty(hrSealInfo)) {
//            UserSealInput userSealInput = new UserSealInput();
//            userSealInput.setAccount(hrSealInfo.getAccount());
//            userSealInput.setSealNo(hrSealInfo.getSealNo());
//            ApiRespBody apiRespBody = contractUtils.removeSeal(userSealInput);
//            if (apiRespBody.getCode() != 100000){
//                throw new RuntimeException(apiRespBody.getMsg());
//            }
//        }
        return toAjax(hrSealInfoService.removeById(id));
    }
}
