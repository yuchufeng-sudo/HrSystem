package com.ys.hr.mapper;

import com.ys.hr.domain.vo.EmployeeBirthdayReportVo;
import com.ys.hr.domain.vo.EmployeeContactsReportVo;

import java.util.List;

public interface HrReportMapper {
    List<EmployeeBirthdayReportVo> selectEmployeeBirthdayReportVo(String searchValue);
    List<EmployeeContactsReportVo> selectEmployeeContactsReportVo(String searchValue);
}
