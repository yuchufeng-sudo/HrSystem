package com.ys.hr.service;

import com.ys.hr.domain.vo.EmployeeBirthdayReportVo;
import com.ys.hr.domain.vo.EmployeeContactsReportVo;

import java.util.List;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/5/26
 */
public interface IHrReportService {
    List<EmployeeBirthdayReportVo> selectEmployeeBirthdayReportVo(String searchValue);
    List<EmployeeContactsReportVo> selectEmployeeContactsReportVo(String searchValue);
}
