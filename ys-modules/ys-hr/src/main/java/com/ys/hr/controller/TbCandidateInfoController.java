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
 *  Candidate INFORMATIONController
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

    @Resource
    private IHrQuestionAnswerService questionAnswerService;


    /**
     * Query Candidate Information list
     */
    @RequiresPermissions("hr:candidateInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(HrCandidateInfo hrCandidateInfo)
    {
        hrCandidateInfo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrCandidateInfo> list = tbCandidateInfoService.selectTbCandidateInfoList(hrCandidateInfo);
        return getDataTable(list);
    }

    /**
     * Get Candidate Details
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
     * Export Candidate Information list
     */
    @RequiresPermissions("hr:candidateInfo:export")
    @Log(title = "Candidate Information", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrCandidateInfo hrCandidateInfo)
    {
        List<HrCandidateInfo> list = tbCandidateInfoService.selectTbCandidateInfoList(hrCandidateInfo);
        ExcelUtil<HrCandidateInfo> util = new ExcelUtil<HrCandidateInfo>(HrCandidateInfo.class);
        util.exportExcel(response, list, "Candidate Information Data");
    }

    /**
     * Add Candidate Information
     */
    @RequiresPermissions("hr:candidateInfo:add")
    @Log(title = "Candidate Information", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrCandidateInfo hrCandidateInfo) {
        hrCandidateInfo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        boolean save = tbCandidateInfoService.insertHrCandidateInfo(hrCandidateInfo);
        return toAjax(save);
    }

    /**
     * Update Candidate Information
     */
    @RequiresPermissions("hr:candidateInfo:edit")
    @Log(title = "Candidate Information", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrCandidateInfo hrCandidateInfo) {
        return toAjax(tbCandidateInfoService.updateHrCandidateInfo(hrCandidateInfo));
    }


    @PutMapping("/sendEmailHired")
    public AjaxResult sendEmailHired(@RequestBody HrCandidateInfo hrCandidateInfo) {
        tbCandidateInfoService.sendEmailHired(hrCandidateInfo);
        return success();
    }

    @PutMapping("/sendEmail")
    public AjaxResult sendEmail(@RequestBody HrCandidateInfo hrCandidateInfo) {
        tbCandidateInfoService.sendEmail(hrCandidateInfo);
        return success();
    }

    /**
     * Delete Candidate Information
     */
    @RequiresPermissions("hr:candidateInfo:remove")
    @Log(title = "Candidate Information", businessType = BusinessType.DELETE)
    @DeleteMapping("/{candidateIds}")
    public AjaxResult remove(@PathVariable Long[] candidateIds) {
        return toAjax(tbCandidateInfoService.removeByIds(Arrays.asList(candidateIds)));
    }


    @GetMapping("/candidateCount")
    public AjaxResult candidateCount()
    {
        HrCandidateInfo hrCandidateInfo = new HrCandidateInfo();
        hrCandidateInfo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return AjaxResult.success(tbCandidateInfoService.candidateCount(hrCandidateInfo));
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
