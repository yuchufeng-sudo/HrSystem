package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrHoliday;

import java.util.List;

/**
 *  Holiday   Mapper Interface
 *
 * @author ys
 * @date 2025-06-18
 */
public interface HrHolidayMapper extends BaseMapper<HrHoliday>
{
    /**
     * Query Holiday
     *
     * @param holidayId  Holiday   primary key
     * @return  Holiday
     */
    public HrHoliday selectHrHolidayByHolidayId(Long holidayId);

    /**
     * Query Holiday   list
     *
     * @param hrHoliday  Holiday
     * @return  Holiday   collection
     */
    public List<HrHoliday> selectHrHolidayList(HrHoliday hrHoliday);

    int updateByHolidayId(HrHoliday hrHoliday);
}
