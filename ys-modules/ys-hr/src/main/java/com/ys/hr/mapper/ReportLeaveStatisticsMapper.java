package com.ys.hr.mapper;

import com.ys.hr.domain.vo.ReportChartDataVo;
import com.ys.hr.domain.vo.ReportLeaveListVo;
import com.ys.hr.domain.vo.ReportLeaveQueryVo;

import java.util.List;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/21
 */
public interface ReportLeaveStatisticsMapper {

    /*Total Leave Liability (Days)*/
    public Double selectTotalLeaveLiability(ReportLeaveQueryVo benefitsStatisticsVo);
    /*Total Accrued This Period*/
    public Integer selectTotalAccruedThisPeriod(ReportLeaveQueryVo benefitsStatisticsVo);
    /*Total Taken This Period*/
    public Integer selectTotalTakenThisPeriod(ReportLeaveQueryVo benefitsStatisticsVo);
    /*Highest Leave Balance*/
    public String selectHighestLeaveBalance(ReportLeaveQueryVo benefitsStatisticsVo);
    /*Leave Liability by Department (Days)*/
    public List<ReportChartDataVo> selectLeaveLiabilityByDepartment(ReportLeaveQueryVo benefitsStatisticsVo);
    /*Leave Report List*/
    public List<ReportLeaveListVo> selectLeaveReportList(ReportLeaveQueryVo benefitsStatisticsVo);
}
