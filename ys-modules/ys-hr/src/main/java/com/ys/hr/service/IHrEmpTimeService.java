package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmpTime;

import java.util.List;

/**
 *  Employee time  Service Interface
 *
 * @author ys
 * @date 2025-06-03
 */
public interface IHrEmpTimeService extends IService<HrEmpTime>
{
    /**
     * Query Employee time
     *
     * @param empTimeId  Employee time  primary key
     * @return  Employee time
     */
    public HrEmpTime selectHrEmpTimeByEmpTimeId(String empTimeId);

    /**
     * Query Employee time  list
     *
     * @param hrEmpTime  Employee time
     * @return  Employee time  collection
     */
    public List<HrEmpTime> selectHrEmpTimeList(HrEmpTime hrEmpTime);

    /**
     * Add Employee time
     *
     * @param hrEmpTime  Employee time
     * @return Result
     */
    public int insertHrEmpTime(HrEmpTime hrEmpTime);

    /**
     * Update Employee time
     *
     * @param hrEmpTime  Employee time
     * @return Result
     */
    public int updateHrEmpTime(HrEmpTime hrEmpTime);

    /**
     * Batch delete  Employee time
     *
     * @param empTimeIds  Employee time  primary keys to be deleted
     * @return Result
     */
    public int deleteHrEmpTimeByEmpTimeIds(String[] empTimeIds);

    /**
     * Delete Employee time  information
     *
     * @param empTimeId  Employee time  primary key
     * @return Result
     */
    public int deleteHrEmpTimeByEmpTimeId(String empTimeId);

    HrEmpTime getTimeByUserId(HrEmpTime hrEmpTime);

    int insertHrEmpTimes(List<HrEmpTime> hrEmpTimes);
}
