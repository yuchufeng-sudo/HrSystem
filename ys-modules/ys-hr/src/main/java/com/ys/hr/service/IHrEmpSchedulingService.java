package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmpScheduling;

import java.util.List;

/**
 *  EMPLOYEE  LAYOUT  Service Interface
 *
 * @author ys
 * @date 2025-06-04
 */
public interface IHrEmpSchedulingService extends IService<HrEmpScheduling>
{
    /**
     * Query  EMPLOYEE  LAYOUT
     *
     * @param schedulingId  EMPLOYEE  LAYOUT  primary key
     * @return  EMPLOYEE  LAYOUT
     */
    public HrEmpScheduling selectHrEmpSchedulingBySchedulingId(String schedulingId);

    /**
     * Query  EMPLOYEE  LAYOUT  list
     *
     * @param hrEmpScheduling  EMPLOYEE  LAYOUT
     * @return  EMPLOYEE  LAYOUT  collection
     */
    public List<HrEmpScheduling> selectHrEmpSchedulingList(HrEmpScheduling hrEmpScheduling);

    /**
     * Add  EMPLOYEE  LAYOUT
     *
     * @param hrEmpScheduling  EMPLOYEE  LAYOUT
     * @return Result
     */
    public int insertHrEmpScheduling(HrEmpScheduling hrEmpScheduling);

    /**
     * Update  EMPLOYEE  LAYOUT
     *
     * @param hrEmpScheduling  EMPLOYEE  LAYOUT
     * @return Result
     */
    public int updateHrEmpScheduling(HrEmpScheduling hrEmpScheduling);

    /**
     * Batch delete  EMPLOYEE  LAYOUT
     *
     * @param schedulingIds  EMPLOYEE  LAYOUT  primary keys to be deleted
     * @return Result
     */
    public int deleteHrEmpSchedulingBySchedulingIds(String[] schedulingIds);

    /**
     * Delete  EMPLOYEE  LAYOUT  information
     *
     * @param schedulingId  EMPLOYEE  LAYOUT  primary key
     * @return Result
     */
    public int deleteHrEmpSchedulingBySchedulingId(String schedulingId);
}
