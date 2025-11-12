package com.ys.hr.service.impl;

import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.vo.*;
import com.ys.hr.mapper.ReportBenefitsStatisticsMapper;
import com.ys.hr.mapper.ReportCandidateStatisticsMapper;
import com.ys.hr.mapper.ReportDeptStatisticsMapper;
import com.ys.hr.mapper.ReportLeaveStatisticsMapper;
import com.ys.hr.service.IReportStatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/20
 */
@Service
public class ReportStatisticsServiceImpl implements IReportStatisticsService {

    @Resource
    private ReportBenefitsStatisticsMapper reportBenefitsStatisticsMapper;

    @Override
    public Map<String, Object> getBenefitsReport(ReportBenefitsQueryVo reportLeaveQueryVo) {
        reportLeaveQueryVo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        Map<String,Object> map = new HashMap<>();
        Double totalBenefitCost = reportBenefitsStatisticsMapper.selectTotalBenefitCost(reportLeaveQueryVo);
        map.put("totalBenefitCost",totalBenefitCost);
        Integer employeesEnrolled = reportBenefitsStatisticsMapper.selectEmployeesEnrolled(reportLeaveQueryVo);
        map.put("employeesEnrolled",employeesEnrolled);
        String mostEnrolledBenefit = reportBenefitsStatisticsMapper.selectMostEnrolledBenefit(reportLeaveQueryVo);
        map.put("mostEnrolledBenefit",mostEnrolledBenefit);
        Double avgCostPerEmployee = reportBenefitsStatisticsMapper.selectAvgCostPerEmployee(reportLeaveQueryVo);
        map.put("avgCostPerEmployee",avgCostPerEmployee);
        List<ReportChartDataVo> benefitCostBreakdown = reportBenefitsStatisticsMapper.selectBenefitCostBreakdown(reportLeaveQueryVo);
        map.put("benefitCostBreakdown",benefitCostBreakdown);
        List<ReportChartDataVo> enrollmentByLocation = reportBenefitsStatisticsMapper.selectEnrollmentByCountry(reportLeaveQueryVo);
        map.put("enrollmentByLocation",enrollmentByLocation);
        List<ReportBenefitsListVo> benefitsReportList = reportBenefitsStatisticsMapper.selectBenefitsReportList(reportLeaveQueryVo);
        map.put("benefitsReportList",benefitsReportList);
        return map;
    }

    @Resource
    private ReportLeaveStatisticsMapper reportLeaveStatisticsMapper;

    @Override
    public Map<String, Object> getLeaveReport(ReportLeaveQueryVo reportLeaveQueryVo) {
        reportLeaveQueryVo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        if ("0".equals(reportLeaveQueryVo.getLeaveType())) {
            reportLeaveQueryVo.setLeaveType(null);
        }
        Map<String,Object> map = new HashMap<>();
        Double totalLeaveLiability = reportLeaveStatisticsMapper.selectTotalLeaveLiability(reportLeaveQueryVo);
        map.put("totalLeaveLiability",totalLeaveLiability);
        Integer totalAccruedThisPeriod = reportLeaveStatisticsMapper.selectTotalAccruedThisPeriod(reportLeaveQueryVo);
        map.put("totalAccruedThisPeriod",totalAccruedThisPeriod);
        Integer totalTakenThisPeriod = reportLeaveStatisticsMapper.selectTotalTakenThisPeriod(reportLeaveQueryVo);
        map.put("totalTakenThisPeriod",totalTakenThisPeriod);
        String highestLeaveBalance = reportLeaveStatisticsMapper.selectHighestLeaveBalance(reportLeaveQueryVo);
        map.put("highestLeaveBalance",highestLeaveBalance);
        List<ReportChartDataVo> leaveLiabilityByDepartment = reportLeaveStatisticsMapper.selectLeaveLiabilityByDepartment(reportLeaveQueryVo);
        map.put("leaveLiabilityByDepartment",leaveLiabilityByDepartment);
        List<ReportLeaveListVo> leaveReportList = reportLeaveStatisticsMapper.selectLeaveReportList(reportLeaveQueryVo);
        map.put("leaveReportList",leaveReportList);
        return map;
    }

    @Resource
    private ReportDeptStatisticsMapper reportDeptStatisticsMapper;

    @Override
    public List<ReportDeptListVo> getDeptReport(ReportQueryVo reportQueryVo) {
        reportQueryVo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return reportDeptStatisticsMapper.selectDeptReportList(reportQueryVo);
    }

    @Resource
    private ReportCandidateStatisticsMapper reportCandidateStatisticsMapper;

    @Override
    public Map<String, Object> getCandidateReport(ReportCandidateQueryVo reportCandidateQueryVo) {
        reportCandidateQueryVo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        Map<String,Object> map = new HashMap<>();
        Integer totalApplicants = reportCandidateStatisticsMapper.selectTotalApplicants(reportCandidateQueryVo);
        map.put("totalApplicants",totalApplicants);
        Integer totalAccruedThisPeriod = reportCandidateStatisticsMapper.selectTotalAccruedThisPeriod(reportCandidateQueryVo);
        map.put("totalAccruedThisPeriod",totalAccruedThisPeriod);
        List<ReportCandidateListVo> reportCandidateListVos = reportCandidateStatisticsMapper.selectCandidateReportList(reportCandidateQueryVo);
        map.put("reportCandidateListVos",reportCandidateListVos);
        return map;
    }
}
