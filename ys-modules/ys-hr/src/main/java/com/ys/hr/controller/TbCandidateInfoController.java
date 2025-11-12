package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.*;
import com.ys.hr.service.*;
import com.ys.utils.email.EmailUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *  CANDIDATE  INFORMATIONController
 *
 * @author ys
 * @date 2025-05-20
 */
@RestController
@RequestMapping("/candidateInfo")
public class TbCandidateInfoController extends BaseController
{
    @Autowired
    private ITbCandidateInfoService tbCandidateInfoService;

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Resource
    private EmailUtils emailUtils;

    @Resource
    private IHrQuestionAnswerService questionAnswerService;


    /**
     * QUERY CANDIDATE  INFORMATION  LIST
     */
    @RequiresPermissions("hr:candidateInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(HrCandidateInfo tbCandidateInfo)
    {
        tbCandidateInfo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrCandidateInfo> list = tbCandidateInfoService.selectTbCandidateInfoList(tbCandidateInfo);
        return getDataTable(list);
    }

    /**
     * OBTAIN  CANDIDATE  INFORMATIONDETAILEDLY INFORMATION
     */
    @GetMapping(value = "/{candidateId}")
    public AjaxResult getInfo(@PathVariable("candidateId") Long candidateId) {
        HrCandidateInfo byId = tbCandidateInfoService.getById(candidateId);
        if (!byId.getEnterpriseId().equals(SecurityUtils.getUserEnterpriseId())) {
            return AjaxResult.error("No permission to view");
        }
        return success(byId);
    }

    /**
     * EXPORT CANDIDATE  INFORMATION  LIST
     */
    @RequiresPermissions("hr:candidateInfo:export")
    @Log(title = " CANDIDATE  INFORMATION", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrCandidateInfo tbCandidateInfo)
    {
        List<HrCandidateInfo> list = tbCandidateInfoService.selectTbCandidateInfoList(tbCandidateInfo);
        ExcelUtil<HrCandidateInfo> util = new ExcelUtil<HrCandidateInfo>(HrCandidateInfo.class);
        util.exportExcel(response, list, "CANDIDATE INFORMATION Data");
    }

    /**
     * ADD CANDIDATE  INFORMATION
     */
    @RequiresPermissions("hr:candidateInfo:add")
    @Log(title = " CANDIDATE  INFORMATION", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrCandidateInfo tbCandidateInfo) {
        tbCandidateInfo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        boolean save = tbCandidateInfoService.insertHrCandidateInfo(tbCandidateInfo);
        return toAjax(save);
    }

    /**
     * MODIFY CANDIDATE  INFORMATION
     */
    @RequiresPermissions("hr:candidateInfo:edit")
    @Log(title = " CANDIDATE  INFORMATION", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrCandidateInfo tbCandidateInfo) {
        return toAjax(tbCandidateInfoService.updateHrCandidateInfo(tbCandidateInfo));
    }


    @PutMapping("/sendEmailHired")
    public AjaxResult sendEmailHired(@RequestBody HrCandidateInfo tbCandidateInfo) {
//        HrCandidateInfo candidateInfo = new HrCandidateInfo();
//        candidateInfo.setCandidateId(tbCandidateInfo.getCandidateId());
//        List<HrCandidateInfo> hrCandidateInfos1 = tbCandidateInfoService.selectTbCandidateInfoList(candidateInfo);
//        if(ObjectUtils.isEmpty(hrCandidateInfos1)){
//            return AjaxResult.warn("The Candidate anomalies!");
//        }
            Map<String, Object> map = new HashMap<>();
            HrEnterprise Company = tbCandidateInfoService.selectEid(SecurityUtils.getUserEnterpriseId());
            HrEmployees hrEmployees = hrEmployeesService.selectHrEmployeesByUserId(SecurityUtils.getUserId());
            if(ObjectUtils.isNotEmpty( Company)){
                map.put("CompanyName",Company.getEnterpriseName());
                map.put("SupportEmail",Company.getContactEmail());
            }else{
                map.put("CompanyName","No details yet");
                map.put("SupportEmail","No details yet");
            }
            if(ObjectUtils.isNotEmpty( hrEmployees)){
                map.put("HrName",hrEmployees.getFullName());
                map.put("HrEmail",hrEmployees.getEmail());
            }else{
                map.put("HrName","No details yet");
                map.put("HrEmail","No details yet");
            }
            map.put("FirstName",tbCandidateInfo.getCandidateName());
            emailUtils.sendEmailByTemplate(map,tbCandidateInfo.getContactEmail(),"CandidateHired");
        return toAjax(1);
    }

    @PutMapping("/sendEmail")
    public AjaxResult sendEmail(@RequestBody HrCandidateInfo tbCandidateInfo) {
                Map<String, Object> map = new HashMap<>();
                HrEnterprise Company = tbCandidateInfoService.selectEid(SecurityUtils.getUserEnterpriseId());
                if(ObjectUtils.isNotEmpty( Company)){
                    map.put("CompanyName",Company.getEnterpriseName());
                }else{
                    map.put("CompanyName","No details yet");
                }
                map.put("FirstName",tbCandidateInfo.getCandidateName());
                map.put("InviteUrl",tbCandidateInfo.getInviteUrl());
                map.put("HrisToolName","Shiftcare HR");
        emailUtils.sendEmailByTemplate(map, tbCandidateInfo.getContactEmail(), "Invite");
        return toAjax(1);
    }

    /**
     * DELETE CANDIDATE  INFORMATION
     */
    @RequiresPermissions("hr:candidateInfo:remove")
    @Log(title = " CANDIDATE  INFORMATION", businessType = BusinessType.DELETE)
    @DeleteMapping("/{candidateIds}")
    public AjaxResult remove(@PathVariable Long[] candidateIds) {
        return toAjax(tbCandidateInfoService.removeByIds(Arrays.asList(candidateIds)));
    }


    @GetMapping("/candidateCount")
    public AjaxResult candidateCount()
    {
        HrCandidateInfo tbCandidateInfo = new HrCandidateInfo();
        tbCandidateInfo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return AjaxResult.success(tbCandidateInfoService.candidateCount(tbCandidateInfo));
    }

    @GetMapping("/answer")
    private AjaxResult answer(Long candidateId)
    {
        HrQuestionAnswer questionAnswer = new HrQuestionAnswer();
        questionAnswer.setCandidateId(candidateId);
        List<HrQuestionAnswer> hrQuestionAnswers = questionAnswerService.selectHrQuestionAnswerList(questionAnswer);
        return AjaxResult.success(hrQuestionAnswers);
    }
}
