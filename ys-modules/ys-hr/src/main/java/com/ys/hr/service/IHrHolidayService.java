package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrHoliday;

import java.util.List;

/**
 *  Holiday Service Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface IHrHolidayService extends IService<HrHoliday>
{
    /**
     * Query Holiday
     *
     * @param holidayId  Holiday primary key
     * @return Holiday
     */
    public HrHoliday selectHrHolidayByHolidayId(Long holidayId);

    /**
     * Query Holiday list
     *
     * @param hrHoliday  Holiday
     * @return Holiday collection
     */
    public List<HrHoliday> selectHrHolidayList(HrHoliday hrHoliday);

    /**
     * Add Holiday
     *
     * @param hrHoliday  Holiday
     * @return Result
     */
    public int insertHrHoliday(HrHoliday hrHoliday);

    /**
     * Update Holiday
     *
     * @param hrHoliday  Holiday
     * @return Result
     */
    public int updateHrHoliday(HrHoliday hrHoliday);

    /**
     * Batch delete  Holiday
     *
     * @param holidayIds  Holiday primary keys to be deleted
     * @return Result
     */
    public int deleteHrHolidayByHolidayIds(Long[] holidayIds);

    /**
     * Delete Holiday information
     *
     * @param holidayId  Holiday primary key
     * @return Result
     */
    public int deleteHrHolidayByHolidayId(Long holidayId);
}
