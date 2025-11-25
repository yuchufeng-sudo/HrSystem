package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrSchedulingEmp;

import java.util.List;

/**
 *    Employee scheduling Service Interface
 *
 * @author ys
 * @date 2025-06-08
 */
public interface IHrSchedulingEmpService extends IService<HrSchedulingEmp>
{
    /**
     * Query  Employee scheduling
     *
     * @param schedulingEmpId    Employee scheduling primary key
     * @return    Employee scheduling
     */
    public HrSchedulingEmp selectHrSchedulingEmpBySchedulingEmpId(String schedulingEmpId);

    /**
     * Query  Employee scheduling list
     *
     * @param hrSchedulingEmp    Employee scheduling
     * @return    Employee scheduling collection
     */
    public List<HrSchedulingEmp> selectHrSchedulingEmpList(HrSchedulingEmp hrSchedulingEmp);

    /**
     * Add Employee scheduling
     *
     * @param hrSchedulingEmp    Employee scheduling
     * @return Result
     */
    public int insertHrSchedulingEmp(HrSchedulingEmp hrSchedulingEmp);

    /**
     * Update Employee scheduling
     *
     * @param hrSchedulingEmp    Employee scheduling
     * @return Result
     */
    public int updateHrSchedulingEmp(HrSchedulingEmp hrSchedulingEmp);

    /**
     * Batch delete    Employee scheduling
     *
     * @param schedulingEmpIds    Employee scheduling primary keys to be deleted
     * @return Result
     */
    public int deleteHrSchedulingEmpBySchedulingEmpIds(String[] schedulingEmpIds);

    /**
     * Delete Employee scheduling information
     *
     * @param schedulingEmpId    Employee scheduling primary key
     * @return Result
     */
    public int deleteHrSchedulingEmpBySchedulingEmpId(String schedulingEmpId);

    HrSchedulingEmp getUserInfo(Long userId);
}
