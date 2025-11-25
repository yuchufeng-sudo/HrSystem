package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrAttendance;
import com.ys.hr.domain.HrPayroll;
import com.ys.hr.domain.vo.AnnualPayrollStatisticsVo;
import com.ys.hr.domain.vo.payRollVo;

import java.util.List;
import java.util.Map;

/**
 *  Employee salary  Service Interface
 *
 * @author ys
 * @date 2025-05-30
 */
public interface IHrPayrollService extends IService<HrPayroll>
{
    /**
     * Query Employee salary
     *
     * @param payrollId  Employee salary  primary key
     * @return  Employee salary
     */
    public HrPayroll selectHrPayrollByPayrollId(String payrollId);

    /**
     * Query Employee salary  list
     *
     * @param hrPayroll  Employee salary
     * @return  Employee salary  collection
     */
    public List<HrPayroll> selectHrPayrollList(HrPayroll hrPayroll);

    /**
     * Add Employee salary
     *
     * @param hrPayroll  Employee salary
     * @return Result
     */
    public int insertHrPayroll(HrPayroll hrPayroll);

    /**
     * Update Employee salary
     *
     * @param hrPayroll  Employee salary
     * @return Result
     */
    public int updateHrPayroll(HrPayroll hrPayroll);

    /**
     * Batch delete  Employee salary
     *
     * @param payrollIds  Employee salary  primary keys to be deleted
     * @return Result
     */
    public int deleteHrPayrollByPayrollIds(String[] payrollIds);

    /**
     * Delete Employee salary  information
     *
     * @param payrollId  Employee salary  primary key
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
