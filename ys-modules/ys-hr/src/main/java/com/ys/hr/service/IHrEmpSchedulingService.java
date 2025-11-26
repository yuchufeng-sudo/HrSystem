package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmpScheduling;

import java.util.List;

/**
 *  Employee Scheduling Service Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface IHrEmpSchedulingService extends IService<HrEmpScheduling>
{
    /**
     * Query Employee Scheduling
     *
     * @param schedulingId  Employee Scheduling primary key
     * @return Employee Scheduling
     */
    public HrEmpScheduling selectHrEmpSchedulingBySchedulingId(String schedulingId);

    /**
     * Query Employee Scheduling list
     *
     * @param hrEmpScheduling Employee Scheduling
     * @return Employee Scheduling collection
     */
    public List<HrEmpScheduling> selectHrEmpSchedulingList(HrEmpScheduling hrEmpScheduling);

    /**
     * Add Employee Scheduling
     *
     * @param hrEmpScheduling Employee Scheduling
     * @return Result
     */
    public int insertHrEmpScheduling(HrEmpScheduling hrEmpScheduling);

    /**
     * Update Employee Scheduling
     *
     * @param hrEmpScheduling Employee Scheduling
     * @return Result
     */
    public int updateHrEmpScheduling(HrEmpScheduling hrEmpScheduling);

    /**
     * Batch delete  Employee Scheduling
     *
     * @param schedulingIds  Employee Scheduling primary keys to be deleted
     * @return Result
     */
    public int deleteHrEmpSchedulingBySchedulingIds(String[] schedulingIds);

    /**
     * Delete Employee Scheduling information
     *
     * @param schedulingId  Employee Scheduling primary key
     * @return Result
     */
    public int deleteHrEmpSchedulingBySchedulingId(String schedulingId);
}
