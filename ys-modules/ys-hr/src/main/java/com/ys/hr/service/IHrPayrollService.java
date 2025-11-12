package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrAttendance;
import com.ys.hr.domain.HrPayroll;
import com.ys.hr.domain.vo.AnnualPayrollStatisticsVo;
import com.ys.hr.domain.vo.payRollVo;

import java.util.List;
import java.util.Map;

/**
 *  EMPLOYEE  SALARY  Service Interface
 *
 * @author ys
 * @date 2025-05-30
 */
public interface IHrPayrollService extends IService<HrPayroll>
{
    /**
     * Query  EMPLOYEE  SALARY
     *
     * @param payrollId  EMPLOYEE  SALARY  primary key
     * @return  EMPLOYEE  SALARY
     */
    public HrPayroll selectHrPayrollByPayrollId(String payrollId);

    /**
     * Query  EMPLOYEE  SALARY  list
     *
     * @param hrPayroll  EMPLOYEE  SALARY
     * @return  EMPLOYEE  SALARY  collection
     */
    public List<HrPayroll> selectHrPayrollList(HrPayroll hrPayroll);

    /**
     * Add  EMPLOYEE  SALARY
     *
     * @param hrPayroll  EMPLOYEE  SALARY
     * @return Result
     */
    public int insertHrPayroll(HrPayroll hrPayroll);

    /**
     * Update  EMPLOYEE  SALARY
     *
     * @param hrPayroll  EMPLOYEE  SALARY
     * @return Result
     */
    public int updateHrPayroll(HrPayroll hrPayroll);

    /**
     * Batch delete  EMPLOYEE  SALARY
     *
     * @param payrollIds  EMPLOYEE  SALARY  primary keys to be deleted
     * @return Result
     */
    public int deleteHrPayrollByPayrollIds(String[] payrollIds);

    /**
     * Delete  EMPLOYEE  SALARY  information
     *
     * @param payrollId  EMPLOYEE  SALARY  primary key
     * @return Result
     */
    public int deleteHrPayrollByPayrollId(String payrollId);

    HrPayroll getPayRollByUserId(HrPayroll hrPayroll);


    int updataByIds(List<String> list);

    Map<String, Object> getPayrollsCoutByHr(HrAttendance hrAttendance);

    int insertHrPayrolls(List<HrPayroll> hrPayrolls);

    List<AnnualPayrollStatisticsVo> selectAnnualPayrollStatisticsList(HrPayroll hrPayroll);

    Map<String, Object> getPayrollsCoutByEmp(HrAttendance hrAttendance);

    List<payRollVo> selectHrPayrollListVo(HrPayroll hrPayroll);

    List<payRollVo> selectHrPayrollListVoByIds(String payrollIds);

    Map<String, Object> lastPayrollByUserId(HrPayroll hrPayroll);
}
