package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmpTime;

import java.util.List;

/**
 *  EMPLOYEE  TIME  Service Interface
 *
 * @author ys
 * @date 2025-06-03
 */
public interface IHrEmpTimeService extends IService<HrEmpTime>
{
    /**
     * Query  EMPLOYEE  TIME
     *
     * @param empTimeId  EMPLOYEE  TIME  primary key
     * @return  EMPLOYEE  TIME
     */
    public HrEmpTime selectHrEmpTimeByEmpTimeId(String empTimeId);

    /**
     * Query  EMPLOYEE  TIME  list
     *
     * @param hrEmpTime  EMPLOYEE  TIME
     * @return  EMPLOYEE  TIME  collection
     */
    public List<HrEmpTime> selectHrEmpTimeList(HrEmpTime hrEmpTime);

    /**
     * Add  EMPLOYEE  TIME
     *
     * @param hrEmpTime  EMPLOYEE  TIME
     * @return Result
     */
    public int insertHrEmpTime(HrEmpTime hrEmpTime);

    /**
     * Update  EMPLOYEE  TIME
     *
     * @param hrEmpTime  EMPLOYEE  TIME
     * @return Result
     */
    public int updateHrEmpTime(HrEmpTime hrEmpTime);

    /**
     * Batch delete  EMPLOYEE  TIME
     *
     * @param empTimeIds  EMPLOYEE  TIME  primary keys to be deleted
     * @return Result
     */
    public int deleteHrEmpTimeByEmpTimeIds(String[] empTimeIds);

    /**
     * Delete  EMPLOYEE  TIME  information
     *
     * @param empTimeId  EMPLOYEE  TIME  primary key
     * @return Result
     */
    public int deleteHrEmpTimeByEmpTimeId(String empTimeId);

    HrEmpTime getTimeByUserId(HrEmpTime hrEmpTime);

    int insertHrEmpTimes(List<HrEmpTime> hrEmpTimes);
}
