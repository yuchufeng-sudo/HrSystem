package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmpHoliday;

import java.util.List;

/**
 *  Employee Holiday  Service Interface
 *
 * @author ys
 * @date 2025-05-23
 */
public interface IHrEmpHolidayService extends IService<HrEmpHoliday>
{

    /**
     * Query Employee Holiday list
     *
     * @param hrEmpHoliday  Employee Holiday
     * @return Employee Holiday  Set
     */
    public List<HrEmpHoliday> selectHrEmpHolidayList(HrEmpHoliday hrEmpHoliday);

    HrEmpHoliday selectHrEmpHolidayById(Long empleHolidayId);

    boolean insertHrEmpHoliday(HrEmpHoliday hrEmpHoliday);

    void addHolidays(HrEmpHoliday hrEmpHoliday);

    int updateByHrEmpHolidayById(HrEmpHoliday hrEmpHoliday);
}
