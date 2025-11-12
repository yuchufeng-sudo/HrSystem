package com.ys.hr.service;

import com.ys.hr.domain.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/20
 */
public interface IReportStatisticsService {
    public Map<String,Object> getBenefitsReport(ReportBenefitsQueryVo reportBenefitsQueryVo);

    public Map<String,Object> getLeaveReport(ReportLeaveQueryVo reportLeaveQueryVo);

    public List<ReportDeptListVo> getDeptReport(ReportQueryVo reportQueryVo);

    public Map<String,Object> getCandidateReport(ReportCandidateQueryVo reportCandidateQueryVo);
}
