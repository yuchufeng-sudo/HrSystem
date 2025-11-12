package com.ys.hr.mapper;

import com.ys.hr.domain.vo.ReportDeptListVo;
import com.ys.hr.domain.vo.ReportQueryVo;

import java.util.List;

public interface ReportDeptStatisticsMapper {
    public List<ReportDeptListVo> selectDeptReportList(ReportQueryVo reportQueryVo);
}
