package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrHoliday;

import java.util.List;

/**
 *  HOLIDAY   Mapper Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface HrHolidayMapper extends BaseMapper<HrHoliday>
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

    int updateByHolidayId(HrHoliday hrHoliday);
}
