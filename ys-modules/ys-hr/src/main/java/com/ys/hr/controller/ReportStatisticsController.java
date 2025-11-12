package com.ys.hr.controller;

import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.hr.domain.vo.*;
import com.ys.hr.service.IReportStatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/20
 */
@RestController
@RequestMapping("reportStatistics")
public class ReportStatisticsController extends BaseController {

    @Resource
    private IReportStatisticsService reportStatisticsService;

    @GetMapping("benefitsReport")
    public AjaxResult benefitsReport(ReportBenefitsQueryVo benefitsQueryVo){
        return AjaxResult.success(reportStatisticsService.getBenefitsReport(benefitsQueryVo));
    }

    @GetMapping("leaveReport")
    public AjaxResult leaveReport(ReportLeaveQueryVo leaveQueryVo){
        return AjaxResult.success(reportStatisticsService.getLeaveReport(leaveQueryVo));
    }

    @GetMapping("deptReport")
    public TableDataInfo deptReport(ReportQueryVo reportQueryVo){
        startPage();
        List<ReportDeptListVo> deptReport = reportStatisticsService.getDeptReport(reportQueryVo);
        return getDataTable(deptReport);
    }

    @GetMapping("candidateReport")
    public AjaxResult candidateReport(ReportCandidateQueryVo reportCandidateQueryVo){
        Map<String, Object> candidateReport = reportStatisticsService.getCandidateReport(reportCandidateQueryVo);
        return success(candidateReport);
    }

}
