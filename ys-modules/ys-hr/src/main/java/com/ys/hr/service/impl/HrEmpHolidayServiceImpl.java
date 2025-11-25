package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrEmpHoliday;
import com.ys.hr.mapper.HrEmpHolidayMapper;
import com.ys.hr.service.IHrEmpHolidayService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  Employee Holiday  ServiceBusiness layer processing
 *
 * @author ys
 * @date 2025-05-23
 */
@Service
public class HrEmpHolidayServiceImpl extends ServiceImpl<HrEmpHolidayMapper, HrEmpHoliday> implements IHrEmpHolidayService
{


    /**
     * Query Employee Holiday list
     *
     *
     * @param hrEmpHoliday  Employee Holiday  
     * @return  Employee Holiday  
     */
    @Override
    public List<HrEmpHoliday> selectHrEmpHolidayList(HrEmpHoliday hrEmpHoliday)
    {
        return baseMapper.selectHrEmpHolidayList(hrEmpHoliday);
    }

    @Override
    public HrEmpHoliday selectHrEmpHolidayById(Long empleHolidayId) {
        return baseMapper.selectHrEmpHolidayById(empleHolidayId);
    }

    @Override
    public int updateByHrEmpHolidayById(HrEmpHoliday hrEmpHoliday) {
        return baseMapper.updateByHrEmpHolidayById(hrEmpHoliday);
    }

}
