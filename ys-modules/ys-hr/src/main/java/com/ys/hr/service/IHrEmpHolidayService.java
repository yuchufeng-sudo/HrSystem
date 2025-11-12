package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmpHoliday;

import java.util.List;

/**
 *  EMPLOYEE  HOLIDAY  Service Interface
 *
 * @author ys
 * @date 2025-05-23
 */
public interface IHrEmpHolidayService extends IService<HrEmpHoliday>
{

    /**
     * QUERY EMPLOYEE  HOLIDAY    LIST
     *
     * @param hrEmpHoliday  EMPLOYEE  HOLIDAY  
     * @return  EMPLOYEE  HOLIDAY  Set
     */
    public List<HrEmpHoliday> selectHrEmpHolidayList(HrEmpHoliday hrEmpHoliday);

    HrEmpHoliday selectHrEmpHolidayById(Long empleHolidayId);

    int updateByHrEmpHolidayById(HrEmpHoliday hrEmpHoliday);
}
