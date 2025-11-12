package com.ys.hr.service.impl;

import com.ys.hr.domain.vo.EmployeeBirthdayReportVo;
import com.ys.hr.domain.vo.EmployeeContactsReportVo;
import com.ys.hr.mapper.HrReportMapper;
import com.ys.hr.service.IHrReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/5/26
 */
@Service
public class HrReportServiceImpl implements IHrReportService {
    @Resource
    private HrReportMapper hrReportMapper;
    @Override
    public List<EmployeeBirthdayReportVo> selectEmployeeBirthdayReportVo(String searchValue) {
        return hrReportMapper.selectEmployeeBirthdayReportVo(searchValue);
    }

    @Override
    public List<EmployeeContactsReportVo> selectEmployeeContactsReportVo(String searchValue) {
        return hrReportMapper.selectEmployeeContactsReportVo(searchValue);
    }
}
