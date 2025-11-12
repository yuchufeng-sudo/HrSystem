package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrHoliday;

import java.util.List;

/**
 *  HOLIDAY   Service Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface IHrHolidayService extends IService<HrHoliday>
{
    /**
     * Query  HOLIDAY
     *
     * @param holidayId  HOLIDAY   primary key
     * @return  HOLIDAY
     */
    public HrHoliday selectHrHolidayByHolidayId(Long holidayId);

    /**
     * Query  HOLIDAY   list
     *
     * @param hrHoliday  HOLIDAY
     * @return  HOLIDAY   collection
     */
    public List<HrHoliday> selectHrHolidayList(HrHoliday hrHoliday);

    /**
     * Add  HOLIDAY
     *
     * @param hrHoliday  HOLIDAY
     * @return Result
     */
    public int insertHrHoliday(HrHoliday hrHoliday);

    /**
     * Update  HOLIDAY
     *
     * @param hrHoliday  HOLIDAY
     * @return Result
     */
    public int updateHrHoliday(HrHoliday hrHoliday);

    /**
     * Batch delete  HOLIDAY
     *
     * @param holidayIds  HOLIDAY   primary keys to be deleted
     * @return Result
     */
    public int deleteHrHolidayByHolidayIds(Long[] holidayIds);

    /**
     * Delete  HOLIDAY   information
     *
     * @param holidayId  HOLIDAY   primary key
     * @return Result
     */
    public int deleteHrHolidayByHolidayId(Long holidayId);
}
