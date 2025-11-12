package com.ys.hr.mapper;

import com.ys.hr.domain.vo.*;

import java.util.List;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/21
 */
public interface ReportCandidateStatisticsMapper {
    /*Total Applicants*/
    public Integer selectTotalApplicants(ReportCandidateQueryVo reportCandidateQueryVo);
    /*Total Accrued This Period*/
    public Integer selectTotalAccruedThisPeriod(ReportCandidateQueryVo reportCandidateQueryVo);
    /*Candidate Report List*/
    public List<ReportCandidateListVo> selectCandidateReportList(ReportCandidateQueryVo reportCandidateQueryVo);
}
