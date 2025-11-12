package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
import com.ys.hr.domain.HrEnterpriseRealNameInfo;
import com.ys.hr.service.IHrEnterpriseRealNameInfoService;
import org.springframework.validation.annotation.Validated;

/**
 *  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  Controller
 *
 * @author ys
 * @date 2025-06-05
 */
@Slf4j
@RestController
@RequestMapping("/enterpriseRealNameInfo")
public class HrEnterpriseRealNameInfoController extends BaseController
{
    @Autowired
    private IHrEnterpriseRealNameInfoService hrEnterpriseRealNameInfoService;

    /**
     * Query  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  list
     */
    @RequiresPermissions("hr:enterpriseRealNameInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo)
    {
        startPage();
        List<HrEnterpriseRealNameInfo> list = hrEnterpriseRealNameInfoService.selectHrEnterpriseRealNameInfoList(hrEnterpriseRealNameInfo);
        return getDataTable(list);
    }

    /**
     * Export  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  list
     */
    @RequiresPermissions("hr:enterpriseRealNameInfo:export")
    @Log(title = " ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo)
    {
        List<HrEnterpriseRealNameInfo> list = hrEnterpriseRealNameInfoService.selectHrEnterpriseRealNameInfoList(hrEnterpriseRealNameInfo);
        ExcelUtil<HrEnterpriseRealNameInfo> util = new ExcelUtil<HrEnterpriseRealNameInfo>(HrEnterpriseRealNameInfo.class);
        util.exportExcel(response, list, " ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  Data");
    }

    /**
     * Get  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION  details
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        return success(hrEnterpriseRealNameInfoService.selectHrEnterpriseRealNameInfoByUserId(SecurityUtils.getUserEnterpriseId()));
    }

    /**
     * Add  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION 
     */
//    @RequiresPermissions("hr:enterpriseRealNameInfo:add")
    @Log(title = " ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo) {
        hrEnterpriseRealNameInfo.setUserId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrEnterpriseRealNameInfoService.insertHrEnterpriseRealNameInfo(hrEnterpriseRealNameInfo));
    }

    /**
     * Update  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION 
     */
//    @RequiresPermissions("hr:enterpriseRealNameInfo:edit")
    @Log(title = " ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo) {
        return toAjax(hrEnterpriseRealNameInfoService.updateHrEnterpriseRealNameInfo(hrEnterpriseRealNameInfo));
    }

    /**
     * Delete  ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION 
     */
    @RequiresPermissions("hr:enterpriseRealNameInfo:remove")
    @Log(title = " ENTERPRISE REAL-NAME AUTHENTICATION INFORMATION ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrEnterpriseRealNameInfoService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * Enterprise Certification
     * @param hrEnterpriseRealNameInfo
     * @return
     */
    @PostMapping("/certification")
    public AjaxResult certification(@RequestBody HrEnterpriseRealNameInfo hrEnterpriseRealNameInfo){
        hrEnterpriseRealNameInfo.setUserId(SecurityUtils.getUserEnterpriseId());
//        ApiRespBody<AuthOutput> outputApiRespBody = hrEnterpriseRealNameInfoService.certification(hrEnterpriseRealNameInfo);
//        if (outputApiRespBody.getCode() == 100000){
//            hrEnterpriseRealNameInfo.setCertificationStatus(outputApiRespBody.getData().getResult());
//            hrEnterpriseRealNameInfo.setCertificationType(outputApiRespBody.getData().getType());
//            hrEnterpriseRealNameInfo.setIdentifyUrl(outputApiRespBody.getData().getIdentifyUrl());
//            hrEnterpriseRealNameInfo.setSerialNo(outputApiRespBody.getData().getSerialNo());
//            if (ObjectUtils.isNotEmpty(hrEnterpriseRealNameInfo.getId())){
//                hrEnterpriseRealNameInfoService.updateHrEnterpriseRealNameInfo(hrEnterpriseRealNameInfo);
//            }else {
//                hrEnterpriseRealNameInfoService.save(hrEnterpriseRealNameInfo);
//            }
//        }else {
//            hrEnterpriseRealNameInfo.setCertificationStatus(outputApiRespBody.getData().getResult());
//            hrEnterpriseRealNameInfo.setMassage(outputApiRespBody.getMsg());
//            if (ObjectUtils.isNotEmpty(hrEnterpriseRealNameInfo.getId())){
//                hrEnterpriseRealNameInfoService.updateHrEnterpriseRealNameInfo(hrEnterpriseRealNameInfo);
//            }else {
//                hrEnterpriseRealNameInfoService.save(hrEnterpriseRealNameInfo);
//            }
//            throw new RuntimeException(outputApiRespBody.getMsg());
//        }
        return AjaxResult.success(hrEnterpriseRealNameInfo);
    }

    /**
     * Callback URL after real-name authentication is completed
     * @param request
     */
    @PostMapping("/certificationNotifyUrl")
    public String notifyUrl(HttpServletRequest request) {
        Integer result = Integer.valueOf(request.getParameter("result"));
        String serialNo = request.getParameter("serialNo");
        log.debug("Callback after real-name authentication is completed：result:{}, serialNo:{}", result, serialNo);
        HrEnterpriseRealNameInfo info = hrEnterpriseRealNameInfoService.selectBySerialNo(serialNo);
        if (ObjectUtils.isNotEmpty(info)){
            info.setCertificationStatus(result);
        }
        return "ok";
    }

}
