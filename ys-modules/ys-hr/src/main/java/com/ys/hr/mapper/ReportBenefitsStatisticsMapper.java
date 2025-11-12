package com.ys.hr.mapper;

import com.ys.hr.domain.vo.ReportBenefitsListVo;
import com.ys.hr.domain.vo.ReportBenefitsQueryVo;
import com.ys.hr.domain.vo.ReportChartDataVo;

import java.util.List;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/21
 */
public interface ReportBenefitsStatisticsMapper {

    /*Total Benefit Cost*/
    public Double selectTotalBenefitCost(ReportBenefitsQueryVo benefitsStatisticsVo);
    /*Employees Enrolled*/
    public Integer selectEmployeesEnrolled(ReportBenefitsQueryVo benefitsStatisticsVo);
    /*Most Enrolled Benefit*/
    public String selectMostEnrolledBenefit(ReportBenefitsQueryVo benefitsStatisticsVo);
    /*Avg. Cost Per Employee*/
    public Double selectAvgCostPerEmployee(ReportBenefitsQueryVo benefitsStatisticsVo);
    /*Benefit Cost Breakdown*/
    public List<ReportChartDataVo> selectBenefitCostBreakdown(ReportBenefitsQueryVo benefitsStatisticsVo);
    /*Enrollment by Location*/
    public List<ReportChartDataVo> selectEnrollmentByCountry(ReportBenefitsQueryVo benefitsStatisticsVo);
    /*Benefits Report List*/
    public List<ReportBenefitsListVo> selectBenefitsReportList(ReportBenefitsQueryVo benefitsStatisticsVo);
}
