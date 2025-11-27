package com.ys.hr.controller;

import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.hr.domain.HrCandidateInfo;
import com.ys.hr.domain.HrJobListings;
import com.ys.hr.domain.HrQuestionAnswer;
import com.ys.hr.service.IHrCareersService;
import com.ys.hr.service.IHrJobListingsService;
import com.ys.hr.service.IHrQuestionAnswerService;
import com.ys.hr.service.IHrCandidateInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/9/25
 */
@RestController
@RequestMapping("/recruitmentExternal")
public class HrRecruitmentExternalController extends BaseController {
    @Resource
    private IHrCandidateInfoService candidateInfoService;
    @Resource
    private IHrJobListingsService jobListingsService;
    @Resource
    private IHrCareersService careersService;

    @GetMapping("getJobListings/{id}")
    public AjaxResult getJobListings(@PathVariable("id")String id){
        HrJobListings hrJobListings = jobListingsService.selectHrJobListingsById(id);
        return AjaxResult.success(hrJobListings);
    }

    @GetMapping("jobListings")
    public TableDataInfo jobListings(HrJobListings hrJobListings){
        startPage();
        List<HrJobListings> listings = jobListingsService.selectHrJobListingsList(hrJobListings);
        return getDataTable(listings);
    }

    @PostMapping("applyJobListings")
    public AjaxResult applyJobListings(@RequestBody HrCandidateInfo candidateInfo){
        return toAjax(candidateInfoService.applyCandidateInfo(candidateInfo));
    }

    @GetMapping("getCareers/{id}")
    public AjaxResult getCareers(@PathVariable("id")String id){
        return AjaxResult.success(careersService.selectHrCareersById(id));
    }
}
