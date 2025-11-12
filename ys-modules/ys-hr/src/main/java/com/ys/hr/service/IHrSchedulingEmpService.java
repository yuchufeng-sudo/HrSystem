package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrSchedulingEmp;

import java.util.List;

/**
 *    EMPLOYEE SCHEDULING Service Interface
 *
 * @author ys
 * @date 2025-06-08
 */
public interface IHrSchedulingEmpService extends IService<HrSchedulingEmp>
{
    /**
     * Query    EMPLOYEE SCHEDULING
     *
     * @param schedulingEmpId    EMPLOYEE SCHEDULING primary key
     * @return    EMPLOYEE SCHEDULING
     */
    public HrSchedulingEmp selectHrSchedulingEmpBySchedulingEmpId(String schedulingEmpId);

    /**
     * Query    EMPLOYEE SCHEDULING list
     *
     * @param hrSchedulingEmp    EMPLOYEE SCHEDULING
     * @return    EMPLOYEE SCHEDULING collection
     */
    public List<HrSchedulingEmp> selectHrSchedulingEmpList(HrSchedulingEmp hrSchedulingEmp);

    /**
     * Add    EMPLOYEE SCHEDULING
     *
     * @param hrSchedulingEmp    EMPLOYEE SCHEDULING
     * @return Result
     */
    public int insertHrSchedulingEmp(HrSchedulingEmp hrSchedulingEmp);

    /**
     * Update    EMPLOYEE SCHEDULING
     *
     * @param hrSchedulingEmp    EMPLOYEE SCHEDULING
     * @return Result
     */
    public int updateHrSchedulingEmp(HrSchedulingEmp hrSchedulingEmp);

    /**
     * Batch delete    EMPLOYEE SCHEDULING
     *
     * @param schedulingEmpIds    EMPLOYEE SCHEDULING primary keys to be deleted
     * @return Result
     */
    public int deleteHrSchedulingEmpBySchedulingEmpIds(String[] schedulingEmpIds);

    /**
     * Delete    EMPLOYEE SCHEDULING information
     *
     * @param schedulingEmpId    EMPLOYEE SCHEDULING primary key
     * @return Result
     */
    public int deleteHrSchedulingEmpBySchedulingEmpId(String schedulingEmpId);

    HrSchedulingEmp getUserInfo(Long userId);
}
