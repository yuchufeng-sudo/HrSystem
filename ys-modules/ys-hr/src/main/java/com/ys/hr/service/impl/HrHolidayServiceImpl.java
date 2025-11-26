package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrHolidayMapper;
import com.ys.hr.domain.HrHoliday;
import com.ys.hr.service.IHrHolidayService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 *  Holiday Service Implementation
 *
 * @author ys
 * @date 2025-06-18
 */
@Service
public class HrHolidayServiceImpl extends ServiceImpl<HrHolidayMapper, HrHoliday> implements IHrHolidayService
{

    /**
     * Query Holiday
     *
     * @param holidayId  Holiday primary key
     * @return Holiday
     */
    @Override
    public HrHoliday selectHrHolidayByHolidayId(Long holidayId)
    {
        return baseMapper.selectHrHolidayByHolidayId(holidayId);
    }

    /**
     * Query Holiday list
     *
     * @param hrHoliday  Holiday
     * @return Holiday
     */
    @Override
    public List<HrHoliday> selectHrHolidayList(HrHoliday hrHoliday)
    {
        return baseMapper.selectHrHolidayList(hrHoliday);
    }

    /**
     * Add Holiday
     *
     * @param hrHoliday  Holiday
     * @return Result
     */
    @Override
    public int insertHrHoliday(HrHoliday hrHoliday)
    {
        hrHoliday.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrHoliday);
    }

    /**
     * Update Holiday
     *
     * @param hrHoliday  Holiday
     * @return Result
     */
    @Override
    public int updateHrHoliday(HrHoliday hrHoliday)
    {
        hrHoliday.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateByHolidayId(hrHoliday);
    }

    /**
     * Batch delete  Holiday
     *
     * @param holidayIds  Holiday primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrHolidayByHolidayIds(Long[] holidayIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(holidayIds));
    }

    /**
     * Delete Holiday information
     *
     * @param holidayId  Holiday primary key
     * @return Result
     */
    @Override
    public int deleteHrHolidayByHolidayId(Long holidayId)
    {
        return baseMapper.deleteById(holidayId);
    }
}
