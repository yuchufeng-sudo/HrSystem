package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmpHoliday;
import com.ys.hr.mapper.HrEmpHolidayMapper;
import com.ys.hr.service.IHrEmpHolidayService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
     * @return Employee Holiday
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
    @Transactional
    public boolean insertHrEmpHoliday(HrEmpHoliday hrEmpHoliday) {
        HrEmpHoliday holiday = new HrEmpHoliday();
        Date stateTime = hrEmpHoliday.getStateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(stateTime);
        holiday.setTime(formattedDate);
        holiday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrEmpHoliday> hrEmpHolidays = selectHrEmpHolidayList(holiday);
        if(ObjectUtils.isNotEmpty(hrEmpHolidays)){
            throw new ServiceException("Holidays already exist under this date");
        }
        hrEmpHoliday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrEmpHoliday.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        hrEmpHoliday.setUserId(SecurityUtils.getUserId());
        hrEmpHoliday.setCreateTime(DateUtils.getNowDate());
        hrEmpHoliday.setEndTime(hrEmpHoliday.getStateTime());
        return save(hrEmpHoliday);
    }

    @Override
    @Transactional
    public void addHolidays(HrEmpHoliday hrEmpHoliday) {
        Date stateTime = hrEmpHoliday.getStateTime();
        Date endTime = hrEmpHoliday.getEndTime();
        LocalDate startDate = stateTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate endDate = endTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Date currentDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            HrEmpHoliday holiday = new HrEmpHoliday();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(currentDate);
            holiday.setTime(formattedDate);
            holiday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            List<HrEmpHoliday> hrEmpHolidays = selectHrEmpHolidayList(holiday);
            if(ObjectUtils.isNotEmpty(hrEmpHolidays)){
                continue;
            }
            HrEmpHoliday hrHoliday = new HrEmpHoliday();
            BeanUtils.copyProperties(hrEmpHoliday,hrHoliday);
            hrHoliday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            hrHoliday.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
            hrHoliday.setUserId(SecurityUtils.getUserId());
            hrHoliday.setStateTime(currentDate);
            hrHoliday.setCreateTime(DateUtils.getNowDate());
            hrHoliday.setEndTime(hrHoliday.getStateTime());
            save(hrHoliday);
        }
    }

    @Override
    public int updateByHrEmpHolidayById(HrEmpHoliday hrEmpHoliday) {
        return baseMapper.updateByHrEmpHolidayById(hrEmpHoliday);
    }

}
